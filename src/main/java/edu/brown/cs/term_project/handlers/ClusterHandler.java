package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.ArticleJSON;
import edu.brown.cs.term_project.Bubble.NewsData;
import edu.brown.cs.term_project.Graph.Cluster;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.util.List;

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
//      if (clusterIdStr == null || clusterIdStr.equals("")) {
//        clusterResponse.setErrorMessage("No cluster id given.");
//      } else {
//        int clusterId = Integer.parseInt(clusterIdStr);
        // TODO: CHANGE FROM HARDCODED
        List<ArticleJSON> articlesFromCluster = db.getArticlesFromCluster(3, false);
        clusterResponse.setArticles(articlesFromCluster);
//      }
    } catch (Exception e) {
      clusterResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(clusterResponse);
  }

  /**
   * Class for a response from the ClusterHandler endpoint.
   */
   private static class ClusterResponse extends StandardResponse {
    private List<ArticleJSON> articles;
    /**
     * Constructor for the response.
     *
     * @param status  0 successful, 1 error
     * @param message error message if error
     */
    ClusterResponse(int status, String message) {
      super(status, message);
    }

    public void setArticles(List<ArticleJSON> articles) {
      this.articles = articles;
    }
  }
}
