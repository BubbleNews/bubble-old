package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.*;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.*;

/**
 * Class for handling requests to the /details API
 */
public class ClusterDetailHandler {

  /**
   * Handles a request to the /details API.
   *
   * @param request  the request
   * @param response the response
   * @param db       the news database
   * @return a JSON response with the list of articles in a cluster
   */
  public static String handle(Request request, Response response, NewsData db) {
    ClusterDetailResponse detailResponse = new ClusterDetailResponse(0, "");
    try {
      QueryParamsMap qm = request.queryMap();
      String clusterIdStr = qm.value("id");
      if (clusterIdStr == null || clusterIdStr.equals("")) {
        detailResponse.setErrorMessage("No cluster id given.");
      } else {
        int clusterId = Integer.parseInt(clusterIdStr);
        // get set of articles of cluster with id clusterId
        Set<ArticleVertex> articlesFromCluster = db.getArticleVerticesFromCluster(clusterId, true);
//        // fill article map
//        HashMap<Integer, ArticleVertex> articleMap = new HashMap<>();
//        for (ArticleVertex a: articlesFromCluster) {
//          articleMap.put(a.getId(), a);
//        }
        // get edges between articles
        Set<Similarity> clusterEdges = calculateImportance(db, articlesFromCluster);
        Set<SimilarityJSON> jsonEdges = new HashSet<>();
        for (Similarity s: clusterEdges) {
          jsonEdges.add(s.toSimilarityJSON());
        }
        // put edges in response
        detailResponse.setEdges(clusterEdges);
        detailResponse.setNumVertices(articlesFromCluster.size());
      }
    } catch (Exception e) {
      detailResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(detailResponse);
  }

  /**
   * Gets a list of similarity edges between every article and every other article in a cluster.
   * @param db the database containing the articles and clusters
   * @param articles the set of articles in the cluster
   * @return list of edges between articles
   * @throws SQLException should never be thrown
   */
  private static Set<Similarity> calculateImportance(NewsData db, Set<ArticleVertex> articles) throws SQLException {
    final double textWeight = 1;
    final double entityWeight = 1;
    final double titleWeight = 1;
    Set<Similarity> edges = new HashSet<>();
    Map<ArticleWord, Double> vocabMap = db.getVocabFreq();
    Map<Entity, Double> entityMap = db.getEntityFreq();
    int maxCount = db.getMaxVocabCount();

    TextCorpus<Entity, ArticleVertex> entityCorpus =
        new TextCorpus<>(entityMap, maxCount, 0);
    TextCorpus<ArticleWord, ArticleVertex> wordCorpus =
        new TextCorpus<>(vocabMap, maxCount, 1);
    TextCorpus<ArticleWord, ArticleVertex> titleCorpus =
        new TextCorpus<>(vocabMap, maxCount, 2);
    System.out.println("Article Size: " + articles.size());

    for (ArticleVertex a: articles) {
      a.setImportance(entityCorpus, wordCorpus, titleCorpus);
    }
    // loop through the connection between every article and every other article
    for (ArticleVertex a1 : articles) {
      for (ArticleVertex a2 : articles) {
        if (a1.getId() < a2.getId()) {
          Similarity tempEdge = new Similarity(a1, a2, wordCorpus, entityCorpus, titleCorpus,
              textWeight,
              entityWeight, titleWeight);
          tempEdge.setImportance(entityCorpus, wordCorpus, titleCorpus);
          edges.add(tempEdge);
        }
      }
    }
    return edges;
  }

  /**
   * Class for a response from the ClusterHandler endpoint.
   */
  private static class ClusterDetailResponse extends StandardResponse {
    private Set<Similarity> edges;
    private int numVertices;

    /**
     * Constructor for the response.
     *
     * @param status  0 successful, 1 error
     * @param message error message if error
     */
    ClusterDetailResponse(int status, String message) {
      super(status, message);
    }

    public void setEdges(Set<Similarity> edges) {
      this.edges = edges;
    }
    public void setNumVertices(int numVertices) {
      this.numVertices = numVertices;
    }
  }
}
