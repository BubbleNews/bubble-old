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
 * Class for handling requests to the /cluster API.
 */
public class ClusterDetailedHandler {

  /**
   * Handles a request to the /cluster API.
   *
   * @param request  the request
   * @param response the response
   * @param db       the news database
   * @return a JSON response with the list of articles in a cluster
   */
  public static String handle(Request request, Response response, NewsData db) {
    ClusterDetailedResponse clusterResponse = new ClusterDetailedResponse(0, "");
    try {
      QueryParamsMap qm = request.queryMap();
      String clusterIdStr = qm.value("id");
      if (clusterIdStr == null || clusterIdStr.equals("")) {
        clusterResponse.setErrorMessage("No cluster id given.");
      } else {
        int clusterId = Integer.parseInt(clusterIdStr);
        // TODO: CHANGE FROM HARDCODED
        Set<ArticleVertex> articlesFromCluster = db.getArticleVerticiesFromCluster(clusterId,
            true);
        clusterResponse.setArticles(articlesFromCluster, db);
      }
    } catch (Exception e) {
      clusterResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(clusterResponse);
  }

  /**
   * Class for a response from the ClusterHandler endpoint.
   */
  private static class ClusterDetailedResponse extends StandardResponse {
    private Set<ArticleVertex> articles;
    private List<Similarity> edges;

    /**
     * Constructor for the response.
     *
     * @param status  0 successful, 1 error
     * @param message error message if error
     */
    ClusterDetailedResponse(int status, String message) {
      super(status, message);
    }

    public void setArticles(Set<ArticleVertex> clusterArticles, NewsData db) {
      this.articles = clusterArticles;
      calculateImportance(db);
    }

    public void calculateImportance(NewsData db) {
      final double textWeight = 1;
      final double entityWeight = 1;
      final double titleWeight = 1;
      try {
        Map<ArticleWord, Double> vocabMap = db.getVocabFreq();
        Map<Entity, Double> entityMap = db.getEntityFreq();
        int maxCount = db.getMaxVocabCount();
        TextCorpus<Entity, ArticleVertex> entityCorpus =
            new TextCorpus<>(entityMap, maxCount, 0);
        TextCorpus<ArticleWord, ArticleVertex> wordCorpus =
            new TextCorpus<>(vocabMap, maxCount, 1);
        TextCorpus<ArticleWord, ArticleVertex> titleCorpus =
            new TextCorpus<>(vocabMap, maxCount, 2);
        ArrayList<Similarity> edges = new ArrayList<>();
        System.out.println("Article Size: " + articles.size());
        for (ArticleVertex a: articles) {
          a.setImportance(entityCorpus, wordCorpus, titleCorpus);
        }
        for (ArticleVertex a1 : articles) {
          for (ArticleVertex a2 : articles) {
            if (a1.getId() < a2.getId()) {
              Similarity tempEdge = new Similarity(a1, a2, wordCorpus, entityCorpus, titleCorpus,
                  textWeight,
                  entityWeight, titleWeight);
              tempEdge.setImportance(entityCorpus, wordCorpus, titleCorpus);
              a1.addEdge(tempEdge);
              a2.addEdge(tempEdge);
              edges.add(tempEdge);
            }
          }
        }
        this.edges = edges;
//        edges.sort(Comparator.comparingDouble(Similarity::getDistance));
//        int size = edges.size();
//        for (int i = 0; i < size; i++) {
//          if (i < articles.size() || i > (size - 25)) {
//            Similarity tempEdge = edges.get(i);
//            System.out.println(tempEdge.getSource().getArticle().getTitle() + " - "
//                + tempEdge.getDest().getArticle().getTitle() + " : " + edges.get(i).getDistance());
//          }
//        }
      } catch (SQLException e) {
        System.out.println("SQLException");
      }
    }
  }
}
