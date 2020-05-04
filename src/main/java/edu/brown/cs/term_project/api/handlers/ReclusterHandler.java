package edu.brown.cs.term_project.api.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.NewsData;
import edu.brown.cs.term_project.api.response.ChartCluster;
import edu.brown.cs.term_project.Graph.ClusterParameters;
import edu.brown.cs.term_project.api.response.ChartResponse;
import edu.brown.cs.term_project.api.pipeline.NewsClusterer;
import spark.Request;
import spark.Response;
import java.util.Comparator;
import java.util.List;

public final class ReclusterHandler {

  /**
   * Constructor - not called.
   */
  private ReclusterHandler() {
    // not called
  }

  /**
   * Handles a request to the /recluster API.
   * @param request the request
   * @param response the response
   * @param db the news database
   * @return a JSON response with a list of new clusters
   */
  public static String handle(Request request, Response response, NewsData db) {
    ChartResponse chartResponse = new ChartResponse(0, "");
    try {
      // process parameters
      String dateString = request.queryParams("date");
      double textWeight = Double.parseDouble(request.queryParams("textWeight"));
      double entityWeight = Double.parseDouble(request.queryParams("entityWeight"));
      double titleWeight = Double.parseDouble(request.queryParams("titleWeight"));
      int clusterMethod = Integer.parseInt(request.queryParams("clusterMethod"));
      double percentageEdgesToConsider = Double.parseDouble(request.queryParams("edgeThreshold"));
      int numArticles = Integer.parseInt(request.queryParams("numArticles"));
      // create a parameters object to store parameters
      ClusterParameters params = new ClusterParameters(dateString,
          false, textWeight, entityWeight, titleWeight, clusterMethod,
          percentageEdgesToConsider, numArticles);
      // make a new clusterer and cluster
      NewsClusterer clusterer = new NewsClusterer(db);
      List<ChartCluster> newClusters = clusterer.clusterArticles(params);

      // sort by size
      Comparator<ChartCluster> compareBySize =
          (ChartCluster c1, ChartCluster c2) -> c2.getSize() - c1.getSize();
      newClusters.sort(compareBySize);

      chartResponse.setClusters(newClusters);
    } catch (Exception e) {
      chartResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(chartResponse);
  }
}
