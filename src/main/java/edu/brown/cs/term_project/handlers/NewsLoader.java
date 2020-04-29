package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import edu.brown.cs.term_project.Bubble.ArticleJSON;
import edu.brown.cs.term_project.Bubble.Entity;
import edu.brown.cs.term_project.Bubble.NewsData;
import edu.brown.cs.term_project.nlp.TextProcessing;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class NewsLoader {
  private NewsData db;
  String pythonEndpoint;

  public NewsLoader(NewsData db, String pythonEndpoint) {
    this.db = db;
    this.pythonEndpoint = pythonEndpoint;
  }

  public void executeBatches(int numBatches, int articlesPerBatch, int step) throws Exception {
    Date initialEndTime = new Date();
    Date initialStartTime = DateUtils.addHours(initialEndTime, -1 * step);
    for (int i = 0; i < numBatches; i++) {
      Date endTime = DateUtils.addHours(initialEndTime, -1 * i * step);
      Date startTime = DateUtils.addHours(initialStartTime, -1 * i * step);
      System.out.println("Start: " + startTime);
      System.out.println("End: " + endTime);
      System.out.println("Getting batch...");
      loadArticlesBatch(startTime, endTime, articlesPerBatch);
    }
  }

  public void loadArticlesBatch(Date startTime, Date endTime, int numArticles) throws Exception {
    // make request
    HashMap<String, String> requestParams = new HashMap<>();
    requestParams.put("startTime", formatDate(startTime));
    requestParams.put("endTime", formatDate(endTime));
    requestParams.put("numArticles", Integer.toString(numArticles));
    String url = addParameters(pythonEndpoint, requestParams);
    // send request
    System.out.println("Calling python scraper to get news...");
    String pythonResponse = sendGet(url);
    // parse the response
    System.out.println("Parsing articles received from scraper...");
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

  private String formatDate(Date date) {
    System.out.println(date.toString());
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    String strDate = dateFormat.format(date);
    System.out.println(strDate);
    return strDate.replace(' ', 'T');
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
    NewsLoader loader = new NewsLoader(new NewsData("data/backloaded.db"), "http://127.0.0.1:5000/scrape");
//    Date now = new Date();
//    Date dayAgo = DateUtils.addDays(now, -1);
//    loader.loadArticlesBatch(dayAgo, now, 10);
    loader.executeBatches(20, 100, 2);
  }
}
