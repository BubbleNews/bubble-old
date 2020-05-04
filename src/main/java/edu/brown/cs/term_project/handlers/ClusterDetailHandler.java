package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.*;
import edu.brown.cs.term_project.TextSimilarity.IWord;
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
        Set<ArticleVertex> articlesFromCluster = db.getArticleVerticesFromCluster(clusterId);
//        // fill article map
//        HashMap<Integer, ArticleVertex> articleMap = new HashMap<>();
//        for (ArticleVertex a: articlesFromCluster) {
//          articleMap.put(a.getId(), a);
//        }
        // get edges between articles
        Set<Similarity> clusterEdges = calculateImportance(db, articlesFromCluster);
        Set<Map<IWord, Double>> entityHash = new HashSet<>();
        Set<Map<IWord, Double>> wordHash = new HashSet<>();
        Set<Map<IWord, Double>> titleHash = new HashSet<>();
        for (Similarity s: clusterEdges) {
          entityHash.add(s.getEntitySim());
          wordHash.add(s.getWordSim());
          titleHash.add(s.getTitleSim());
        }
        Map<IWord, Double> aggEntities = aggregate(entityHash);
        Map<IWord, Double> aggWords = aggregate(wordHash);
        Map<IWord, Double> aggTitle = aggregate(titleHash);
        // put edges in response
        detailResponse.setEdges(clusterEdges);
        detailResponse.setNumVertices(articlesFromCluster.size());
        detailResponse.setTotals(aggEntities, aggWords, aggTitle);
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
  public static Set<Similarity> calculateImportance(NewsData db, Set<ArticleVertex> articles) throws SQLException {
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

  private static Map<IWord, Double> aggregate(Set<Map<IWord, Double>> maps) {
    Map<IWord, Double> agg = new HashMap<>();
    for (Map<IWord, Double> m: maps) {
      m.forEach((k, v) -> agg.merge(k, v, Double::sum));
    }
    return agg;
  }

  /**
   * Class for serializing a similarity edge;
   */
  static class SimilarityJSON {
    private int articleId1;
    private int articleId2;
    private String articleTitle1;
    private String articleTitle2;
    private double totalDistance;

    SimilarityJSON(int articleId1, int articleId2, String title1, String title2,
                   String source1, String source2, double totalDistance) {
      this.articleId1 = articleId1;
      this.articleId2 = articleId2;
      this.articleTitle1 = source1 + ": " + title1;
      this.articleTitle2 = source2 + ": " + title2;
      this.totalDistance = totalDistance;
    }
  }

  /**
   * Class for a response from the ClusterHandler endpoint.
   */
  private static class ClusterDetailResponse extends StandardResponse {
    private Set<SimilarityJSON> edges;
    private int numVertices;
    private Map<IWord, Double> entitySim;
    private Map<IWord, Double> wordSim;
    private Map<IWord, Double> titleSim;

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
      Set<SimilarityJSON> finalEdges = new HashSet<>();
      for (Similarity s: edges) {
        Article a1 = s.getSource().getArticle();
        Article a2 = s.getDest().getArticle();
        finalEdges.add(new SimilarityJSON(a1.getId(), a2.getId(), a1.getTitle(),
            a2.getTitle(), a1.getSourceName(), a2.getSourceName(), s.getDistance()));
      }
      this.edges = finalEdges;
    }

    public void setTotals(Map<IWord, Double> entities, Map<IWord, Double> words,
                          Map<IWord, Double> title) {
      this.entitySim = entities;
      this.wordSim = words;
      this.titleSim = title;
    }
    public void setNumVertices(int numVertices) {
      this.numVertices = numVertices;
    }
  }
}
