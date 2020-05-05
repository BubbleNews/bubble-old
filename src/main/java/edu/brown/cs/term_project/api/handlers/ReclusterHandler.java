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
      // create a parameters object to store parameters
      ClusterParameters params = new ClusterParameters();
      params.setDate(request.queryParams("date"));
      params.setTextWeight(Double.parseDouble(request.queryParams("textWeight")));
      params.setEntityWeight(Double.parseDouble(request.queryParams("entityWeight")));
      params.setTitleWeight(Double.parseDouble(request.queryParams("titleWeight")));
      params.setClusterMethod(Integer.parseInt(request.queryParams("clusterMethod")));
      params.setPercentageEdgesToConsider(Double.parseDouble(request.queryParams("edgeThreshold")));
      params.setNumArticles(Integer.parseInt(request.queryParams("numArticles")));
      params.setDoInsert(false);

      // make a new clusterer and cluster
      NewsClusterer clusterer = new NewsClusterer(db);
      List<ChartCluster> newClusters = clusterer.clusterArticles(params);
      // sort by size
      Comparator<ChartCluster> compareBySize =
          (ChartCluster c1, ChartCluster c2) -> c2.getSize() - c1.getSize();
      newClusters.sort(compareBySize);
      // set clusters in response
      chartResponse.setClusters(newClusters);
    } catch (Exception e) {
      chartResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(chartResponse);
  }
}
