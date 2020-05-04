package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.NewsData;
import edu.brown.cs.term_project.Graph.ChartCluster;
import edu.brown.cs.term_project.Graph.Cluster;
import edu.brown.cs.term_project.Graph.ClusterParameters;
import spark.Request;
import spark.Response;

import java.util.List;

public class ReclusterHandler {
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
      String textWeightStr = request.queryParams("textWeight");
      String entityWeightStr = request.queryParams("entityWeight");
      String titleWeightStr = request.queryParams("titleWeight");
      String clusterMethodStr = request.queryParams("clusterMethod");
      String percentageEdgesToConsiderStr = request.queryParams("edgeThreshold");
      double textWeight = Double.parseDouble(textWeightStr);
      double entityWeight = Double.parseDouble(entityWeightStr);
      double titleWeight = Double.parseDouble(titleWeightStr);
      int clusterMethod = Integer.parseInt(clusterMethodStr);
      double percentageEdgesToConsider = Double.parseDouble(percentageEdgesToConsiderStr);
      // create a parameters object to store parameters
      ClusterParameters params = new ClusterParameters(dateString,
          false, textWeight, entityWeight, titleWeight, clusterMethod,
          percentageEdgesToConsider);
      // make a new clusterer and cluster
      NewsClusterer clusterer = new NewsClusterer(db);
      List<ChartCluster> newClusters = clusterer.clusterArticles(params);
      chartResponse.setClusters(newClusters);
    } catch (Exception e) {
      chartResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(chartResponse);
  }
}
