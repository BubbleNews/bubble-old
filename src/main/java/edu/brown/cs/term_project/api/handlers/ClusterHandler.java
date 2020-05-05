package edu.brown.cs.term_project.api.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.bubble.Article;
import edu.brown.cs.term_project.database.NewsData;
import edu.brown.cs.term_project.api.response.StandardResponse;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling requests to the /cluster API.
 */
public final class ClusterHandler {

  /**
   * Constructor - never called.
   */
  private ClusterHandler() {
    // never called
  }

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
        List<Article> articlesFromCluster = db.getDataRead().getArticlesFromCluster(clusterId);
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

    /**
     * Constructor for the response.
     *
     * @param status  0 successful, 1 error
     * @param message error message if error
     */
    ClusterResponse(int status, String message) {
      super(status, message);
      this.articles = new ArrayList<>();
    }

    public void setArticles(List<Article> articles) {
      this.articles = articles;
    }
  }
}
