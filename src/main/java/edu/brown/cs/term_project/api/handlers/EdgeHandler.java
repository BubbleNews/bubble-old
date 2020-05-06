package edu.brown.cs.term_project.api.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.api.response.StandardResponse;
import edu.brown.cs.term_project.bubble.ArticleVertex;
import edu.brown.cs.term_project.bubble.Similarity;
import edu.brown.cs.term_project.clustering.ClusterParameters;
import edu.brown.cs.term_project.database.NewsData;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.util.Set;

/**
 * Class for handling requests to the /details API.
 */
public final class EdgeHandler {

  /**
   * Constructor for edge handler - should not be called.
   */
  private EdgeHandler() {
    // not called
  }

  /**
   * Handles a request to the /details API.
   *
   * @param request  the request
   * @param response the response
   * @param db       the news database
   * @return a JSON response with the list of articles in a cluster
   */
  public static String handle(Request request, Response response, NewsData db) {
    EdgeResponse detailResponse = new EdgeResponse(0, "");
    try {
      QueryParamsMap qm = request.queryMap();
      ClusterParameters params = new ClusterParameters(qm, false);
      String a1 = qm.value("id1");
      String a2 = qm.value("id2");
      if (a1 == null || a1.equals("") || a2 == null || a2.equals("")) {
        detailResponse.setErrorMessage("No cluster id given.");
      } else {
        // get set of articles of cluster with id clusterId
        Set<ArticleVertex> articlesFromCluster = db.getDataRead()
                .getArticleVerticesFromArticleIds(new String[] {a1, a2});
        // calculate edges
        Set<Similarity> clusterEdges = ClusterDetailHandler.calculateImportance(db,
            articlesFromCluster, params);
        for (Similarity s: clusterEdges) {
          detailResponse.setEdge(s);
        }
      }
    } catch (Exception e) {
      detailResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(detailResponse);
  }



  /**
   * Class for a response from the ClusterHandler endpoint.
   */
  static class EdgeResponse extends StandardResponse {
    private Similarity edge;

    /**
     * Constructor for the response.
     *
     * @param status  0 successful, 1 error
     * @param message error message if error
     */
    EdgeResponse(int status, String message) {
      super(status, message);
    }

    public void setEdge(Similarity newEdge) {
      this.edge = newEdge;
    }
  }

  public static void main(String[] args) throws Exception {

  }
}

