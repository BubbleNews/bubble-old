package edu.brown.cs.term_project.api.pipeline;

import com.google.common.collect.ObjectArrays;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import edu.brown.cs.term_project.bubble.Article;
import edu.brown.cs.term_project.bubble.Entity;
import edu.brown.cs.term_project.database.NewsData;
import edu.brown.cs.term_project.language.TextProcessing;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.*;

public class NewsLoader {
  private NewsData db;
  String pythonEndpoint;

  public NewsLoader(NewsData db, String pythonEndpoint) {
    this.db = db;
    this.pythonEndpoint = pythonEndpoint;
  }

  /**
   * Gets articles from the python endpoint and stores them in the database.
   * @param numBatches number of calls to the python endpoint
   * @param articlesPerBatch number of articles to retrieve in call
   * @param step number of hours range to search in
   * @param stepBack start scraping articles published this many hours in the past
   * @throws Exception
   */
  public void executeBatches(int numBatches, int articlesPerBatch, int step, int stepBack) throws Exception {
    Instant endTime = Instant.now().minus(stepBack, ChronoUnit.HOURS);
    Instant startTime = endTime.minus(step, ChronoUnit.HOURS);
    for (int i = 0; i < numBatches; i++) {
      System.out.println("Batch " + (i + 1) + "/" + numBatches+ ": Getting " + articlesPerBatch + " articles from "
              + startTime + " to " + endTime);
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
    List<Article> articles = new ArrayList<>();
    for (int i = 0; i < jsonArray.size(); i++) {
      articles.add(gson.fromJson(jsonArray.get(i), Article.class));
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
  private void processJSONArticles(List<Article> jsonArticles) throws SQLException {
    System.out.println("-----Scraped " + jsonArticles.size() + " articles-----");
    // keep track of total number of articles each word has occurred in
    HashMap<String, Integer> occurenceMap = new HashMap<>();
    for (Article article: jsonArticles) {
      System.out.println("Getting entities and lemmizing: " + article.getUrl());
      // get entities for the given article body
      HashMap<Entity, Integer> entityFrequencies =
          TextProcessing.getEntityFrequencies(article.getContent());
      // lemmize text and title
      String[] lemmizedText = TextProcessing.lemmatizeText(article.getContent());
      String[] lemmizedTitle = TextProcessing.lemmatizeText(article.getTitle());

      String[] lemmizedTextAndTitle = ObjectArrays.concat(lemmizedTitle, lemmizedText, String.class);
      // change the content field of the article object to be the lemmized text
      article.setContent(String.join("~^", lemmizedText));
      System.out.println("Updating database");
      // insert article and its entities into the database
      db.getDataWrite().insertArticleAndEntities(article, entityFrequencies);
      // add to the total batch word occurrence map
      Set<String> uniqueWords = new HashSet<>(Arrays.asList(lemmizedTextAndTitle));
      for (String uniqueWord: uniqueWords) {
        if (occurenceMap.containsKey(uniqueWord)) {
          occurenceMap.replace(uniqueWord, occurenceMap.get(uniqueWord) + 1);
        } else {
          occurenceMap.put(uniqueWord, 1);
        }
      }
//      TextProcessing.updateOccurrenceMap(occurenceMap, lemmizedTextAndTitle);
    }
    // update vocab counts in database
    db.getDataWrite().updateVocabCounts(occurenceMap);
  }

  public static void main(String[] args) throws Exception {
    NewsLoader loader = new NewsLoader(new NewsData("data/mock_data.db"), "http://127.0.0" +
        ".1:5000/scrape");
    loader.executeBatches(5, 5, 5, 1);
  }
}
