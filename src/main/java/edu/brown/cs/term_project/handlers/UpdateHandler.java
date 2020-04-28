package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.*;
import edu.brown.cs.term_project.Graph.Graph;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;
import edu.brown.cs.term_project.nlp.TextProcessing;
import spark.Request;
import spark.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * Class that handles calling the Python API to get updated news headlines
 * and entity information from scraper/algorithm, will be called by scheduled
 * cron job.
 */
public class UpdateHandler {

  /**
   * Handles a request to the /update API.
   *
   * @param request  the request
   * @param response the response
   * @param db the database
   * @return a JSON response with status and message.
   */
  public static String handle(Request request, Response response, NewsData db) throws Exception {
    StandardResponse updateResponse = new StandardResponse(0, "");
//    try {

//    } catch (Exception e) {
//      // there has been an error so update response to reflect that
//      updateResponse.setStatus(1);
//      updateResponse.setMessage(e.getMessage());
//    }
//    // convert to json and return
    return new Gson().toJson(updateResponse);
  }

  private static void getAndProcessNews(NewsData db) throws Exception {
    String pythonEndpoint = "http://127.0.0.1:5000/scrape";
    // send the get request to the python endpoint
    System.out.println("Calling python scraper to get news...");
    String pythonResponse = sendGet(pythonEndpoint);
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
    processJSONArticles(articles, db);

  }

  /**
   * Sends an HTTP GET request to a url.
   *
   * @param uri the url to send the get request to
   * @return a json string response
   * @throws Exception
   */
  private static String sendGet(String uri) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uri))
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();
  }

  /**
   * Method that processes the JSONArticles, adding the data to the appropriate
   * tables in the database.
   * @param jsonArticles a list of articles in the form ArticleJSON∂
   * @param db the database to add to
   * @throws SQLException if error occurred adding to database
   */
  private static void processJSONArticles(List<ArticleJSON> jsonArticles, NewsData db) throws SQLException {
    System.out.println("Processing JSON articles...");
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

  private static void clusterArticles(NewsData db) throws SQLException {
    Set<ArticleVertex> pulledArticles = db.getArticleVertices(24);
    Map<ArticleWord, Double> vocabMap = db.getVocabFreq();
    Map<Entity, Double> entityMap = db.getEntityFreq();
    int maxCount = db.getMaxVocabCount();
    TextCorpus<ArticleWord, ArticleVertex> wordCorpus =
        new TextCorpus<>(vocabMap, maxCount);
    TextCorpus<Entity, ArticleVertex> entityCorpus =
        new TextCorpus<>(entityMap, maxCount);
    ArrayList<Similarity> edges = new ArrayList<>();
    System.out.println("Article Size: " + pulledArticles.size());
    for (ArticleVertex a1: pulledArticles) {
      for (ArticleVertex a2: pulledArticles) {
        if (a1.getId() < a2.getId()) {

          Similarity tempEdge = new Similarity(a1, a2, wordCorpus, entityCorpus);
          a1.addEdge(tempEdge);
          a2.addEdge(tempEdge);
          edges.add(tempEdge);
          System.out.println(a1.getId() + " - " + a2.getId() + " : " + tempEdge.getDistance());
        }
      }
    }

    Graph<ArticleVertex, Similarity> graph = new Graph(pulledArticles, edges);
    graph.runClusters(1);
    db.insertClusters(graph.getClusters());
  }

  public static void main(String[] args) throws Exception {
    ArticleJSON testArticle = new ArticleJSON(new String[]{"Kayla Suazo"},
        "23 Top-Rated Cleaning Products That Are Popular For A Reason",
        "So good, they have *a ton* of 4- and 5-star reviews.",
        "https://www.buzzfeed.com/kaylasuazo/top-rated-cleaning-products-"
            + "that-are-popular-for-a-reason",
        "2020-04-27T17:22:24.963019Z",
        "all you have to do be let it sit for 15 minute and wipe -- minimal work on you "
            + "part . check out BuzzFeed 's full write-up on this Feed-N-Wax Wood Polish to learn "
            + "more!and ! it have 4,700 + positive review on amazon.promising review : `` OMG ! "
            + "this be the most amazing product ! we inherit some antique furniture from the '30s"
            + " that have be in storage forever ... it be dry and dirty and not much to look at ."
            + " I use this product on it and the oak wood literally come alive show the beautiful"
            + " grain and texture of the wood . I have since use it on my oak kitchen cabinet and"
            + " they look AMAZING ! I will never use anything else other than this product on my "
            + "wood surface ! no greasy feel -- and a fantastic smell ! '' -- Tiffany SadowskiGet"
            + " it from Amazon for $ 8.48 + -lrb- available in eight size -rrb- ."
    );
    ArrayList<ArticleJSON> testList = new ArrayList<>();
    testList.add(testArticle);
    NewsData db = new NewsData("data/bubble.db");
    //processJSONArticles(testList, db);
    clusterArticles(db);
  }
}
