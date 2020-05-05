package edu.brown.cs.term_project.api.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.bubble.*;
import edu.brown.cs.term_project.api.response.StandardResponse;
import edu.brown.cs.term_project.database.NewsData;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.util.*;

/**
 * Class for handling requests to the /details API
 */
public class EdgeHandler {

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
      String a1 = qm.value("id1");
      String a2 = qm.value("id2");
      if (a1 == null || a1.equals("") || a2 == null || a2.equals("")) {
        detailResponse.setErrorMessage("No cluster id given.");
      } else {
        int id1 = Integer.parseInt(a1);
        int id2 = Integer.parseInt(a2);
        // get set of articles of cluster with id clusterId
        Set<ArticleVertex> articlesFromCluster = db.getDataRead().getArticlePair(id1, id2);
//        // fill article map
//        HashMap<Integer, ArticleVertex> articleMap = new HashMap<>();
//        for (ArticleVertex a: articlesFromCluster) {
//          articleMap.put(a.getId(), a);
//        }
        // get edges between articles
        Set<Similarity> clusterEdges = ClusterDetailHandler.calculateImportance(db,
            articlesFromCluster);
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

