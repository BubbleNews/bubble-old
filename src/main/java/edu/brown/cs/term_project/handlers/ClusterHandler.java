package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.Article;
import edu.brown.cs.term_project.Bubble.ArticleVertex;
import edu.brown.cs.term_project.Bubble.ArticleWord;
import edu.brown.cs.term_project.Bubble.Entity;
import edu.brown.cs.term_project.Bubble.NewsData;
import edu.brown.cs.term_project.Bubble.Similarity;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class for handling requests to the /cluster API.
 */
public class ClusterHandler {

  /**
   * Handles a request to the /cluster API.
   * @param request the request
   * @param response the response
   * @param db the news database
   * @return a JSON response with the list of articles in a cluster
   */
  public static String handle(Request request, Response response, NewsData db) {
    ClusterResponse clusterResponse = new ClusterResponse(0, "");
    try {
      QueryParamsMap qm = request.queryMap();
      String clusterIdStr = qm.value("id");
      // check for cluster id
      if (clusterIdStr == null || clusterIdStr.equals("")) {
        clusterResponse.setErrorMessage("No cluster id given.");
      } else {
        // get list of articles for cluster
        int clusterId = Integer.parseInt(clusterIdStr);
        // TODO: CHANGE FROM HARDCODED
        List<Article> articlesFromCluster = db.getArticlesFromCluster(clusterId, true);
        clusterResponse.setArticles(articlesFromCluster);
      }
    } catch (Exception e) {
      clusterResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(clusterResponse);
  }

  /**
   * Class for a response from the ClusterHandler endpoint.
   */
   private static class ClusterResponse extends StandardResponse {
    private List<Article> articles;
    private List<Similarity> edges;
    /**
     * Constructor for the response.
     *
     * @param status  0 successful, 1 error
     * @param message error message if error
     */
    ClusterResponse(int status, String message) {
      super(status, message);
      this.articles = new ArrayList<>();
      this.edges = new ArrayList<>();
    }

    public void setArticles(List<Article> articles) {
      this.articles = articles;
    }

    public void setEdges(List<Similarity> edges) {
      this.edges = edges;
    }
  }
}
