package edu.brown.cs.term_project.handlers;

import edu.brown.cs.term_project.Bubble.Article;
import edu.brown.cs.term_project.Bubble.Cluster;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

/**
 * Class for handling requests to the /cluster API.
 */
public class ClusterHandler {

  /**
   * Handles a request to the /cluster API.
   * @param request the request
   * @param response the response
   * @return a JSON response with the list of articles in a cluster
   */
  public static String handle(Request request, Response response) {
    ClusterResponse clusterResponse = new ClusterResponse(0, "");
    try {
      QueryParamsMap qm = request.queryMap();
      String clusterId = qm.value("id");
      // TODO: need to use Database class to get the cluster of id 'clusterId'
      Cluster cluster = null;
      clusterResponse.setCluster(cluster);
    } catch (Exception e) {
      clusterResponse.setStatus(1);
      clusterResponse.setMessage(e.getMessage());
    }
    return null;
  }

  /**
   * Class for a response from the ClusterHandler endpoint.
   */
   private static class ClusterResponse extends StandardResponse {
    private Cluster cluster;
    /**
     * Constructor for the response.
     *
     * @param status  0 successful, 1 error
     * @param message error message if error
     */
    public ClusterResponse(int status, String message) {
      super(status, message);
    }

    /**
     * Sets the cluster of the response.
     * @param cluster the cluster to return in the response
     */
    public void setCluster(Cluster cluster) {
      this.cluster = cluster;
    }
  }
}
