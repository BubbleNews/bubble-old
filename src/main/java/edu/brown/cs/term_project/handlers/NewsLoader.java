package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import edu.brown.cs.term_project.Bubble.ArticleJSON;
import edu.brown.cs.term_project.Bubble.Entity;
import edu.brown.cs.term_project.Bubble.NewsData;
import edu.brown.cs.term_project.nlp.TextProcessing;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.time.*;
import java.util.HashMap;
import java.util.List;

import static edu.brown.cs.term_project.handlers.HTTPRequests.sendGet;


public class NewsLoader {
  private NewsData db;
  String pythonEndpoint;

  public NewsLoader(NewsData db, String pythonEndpoint) {
    this.db = db;
    this.pythonEndpoint = pythonEndpoint;
  }

  public void executeBatches(int numBatches, int articlesPerBatch, int step, int stepBack) throws Exception {
    Instant endTime = Instant.now().minus(stepBack, ChronoUnit.HOURS);
    Duration hour = Duration.ofHours(1);
    Instant startTime = endTime.minus(step, ChronoUnit.HOURS);
    for (int i = 0; i < numBatches; i++) {
      System.out.println("Start: " + startTime);
      System.out.println("End: " + endTime);
      System.out.println("Getting batch...");
      loadArticlesBatch(startTime, endTime, articlesPerBatch);
      endTime = startTime;
      startTime = endTime.minus(step, ChronoUnit.HOURS);
    }
  }

  public void loadArticlesBatch(Instant startTime, Instant endTime, int numArticles) throws Exception {
    // make request
    HashMap<String, String> requestParams = new HashMap<>();
    requestParams.put("startTime", formatDate(startTime));
    requestParams.put("endTime", formatDate(endTime));
    requestParams.put("numArticles", Integer.toString(numArticles));
    String url = addParameters(pythonEndpoint, requestParams);
    // send request
    String pythonResponse = sendGet(url);
    // parse the response
    Gson gson = new Gson();
    JsonParser parser = new JsonParser();
    JsonArray jsonArray = parser.parse(pythonResponse).getAsJsonArray();
    List<ArticleJSON> articles = new ArrayList<>();
    for (int i = 0; i < jsonArray.size(); i++) {
      articles.add(gson.fromJson(jsonArray.get(i), ArticleJSON.class));
    }
    // process the articles
    processJSONArticles(articles);
  }

  private String addParameters(String baseUrl, HashMap<String, String> params) {
    StringBuilder sb = new StringBuilder(baseUrl);
    sb.append('?');
    for (String key: params.keySet()) {
      sb.append(key);
      sb.append('=');
      sb.append(params.get(key));
      sb.append('&');
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  private String formatDate(Instant date) {
    return date.truncatedTo(ChronoUnit.SECONDS).toString().replace("Z", "");
  }

  /**
   * Sends an HTTP GET request to the python endpoint.
   *
   ** @return a json string response
   * @throws Exception
   */
  private String sendGet(String url) throws Exception {

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();
  }

  /**
   * Method that processes the JSONArticles, adding the data to the appropriate
   * tables in the database.
   * @param jsonArticles a list of articles in the form ArticleJSON∂
   * @throws SQLException if error occurred adding to database
   */
  private void processJSONArticles(List<ArticleJSON> jsonArticles) throws SQLException {
    System.out.println("Processing " + jsonArticles.size() + " JSON articles...");
    // keep track of total number of articles each word has occurred in
    HashMap<String, Integer> occurenceMap = new HashMap<>();
    for (ArticleJSON article: jsonArticles) {
      System.out.println("Processing article: " + article.getUrl());
      System.out.println("Getting entities...");
      // get entities for the given article body
      HashMap<Entity, Integer> entityFrequencies =
          TextProcessing.getEntityFrequencies(article.getContent());
      // lemmize text
      System.out.println("Lemmizing...");
      String[] lemmizedText = TextProcessing.lemmizeText(article.getContent());
      // change the content field of the article object to be the lemmized text
      article.setContent(String.join(" ", lemmizedText));
      // insert article and its entities into the database
      System.out.println("Inserting article and entities...");
      db.insertArticleAndEntities(article, entityFrequencies);
      // add to the total batch word occurrence map
      TextProcessing.updateOccurrenceMap(occurenceMap, lemmizedText);
    }
    // update vocab counts in database
    System.out.println("Updating vocab occurrences in database...");
    db.updateVocabCounts(occurenceMap);
  }

  public static void main(String[] args) throws Exception {
    NewsLoader loader = new NewsLoader(new NewsData("data/backloaded.db"), "http://127.0.0" +
        ".1:5000/scrape");
//    Date now = new Date();
//    Date dayAgo = DateUtils.addDays(now, -1);
//    loader.loadArticlesBatch(dayAgo, now, 10);
    loader.executeBatches(24, 100, 1, 39);
  }
}
