package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.NewsData;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A class for handling requests to the /chart API.
 */
public final class ChartHandler {

  /**
   * Handles a request to the /chart API.
   * @param request the request
   * @param response the response
   * @param db the news databse
   * @return a JSON response with chart made up of list of clusters
   */
  public static String handle(Request request, Response response, NewsData db) {
    ChartResponse chartResponse = new ChartResponse(0, "");
    try {
      QueryParamsMap qm = request.queryMap();
      String dateString = qm.value("date");
      if (dateString == null || dateString.equals("")) {
        // no date passed in, so get most recent/current chart
        List<ChartCluster> clusters = mockClusters();
        // sort by size
        Comparator<ChartCluster> compareBySize =
            (ChartCluster c1, ChartCluster c2) -> c2.getSize() - c1.getSize();

        clusters.sort(compareBySize);

        chartResponse.setClusters(clusters);

      } else {
        // get finalized clusters from a certain date
        List<ChartCluster> clusters = mockClusters();
      }

    } catch (Exception e) {
      chartResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(chartResponse);
  }

  private static List<ChartCluster> mockClusters() {
    List<ChartCluster> toReturn = new ArrayList<>();
    return toReturn;
  }

  private static class ChartCluster {
    private int clusterId;
    private String headline;
    private int size;

    ChartCluster(int clusterId, String headline, int size) {
      this.clusterId = clusterId;
      this.headline = headline;
      this.size = size;
    }

    public int getSize() {
      return size;
    }
  }

  private static class ChartResponse extends StandardResponse {
    private Date date;
    private List<ChartCluster> clusters;

    /**
     * Constructor for the response.
     *
     * @param status  0 successful, 1 error
     * @param message error message if error
     */
    ChartResponse(int status, String message) {
      super(status, message);
    }

    /**
     * Sets the date of the chart.
     * @param date the date
     */
    public void setDate(Date date) {
      this.date = date;
    }

    /**
     * Sets the list of clusters in the chart.
     * @param clusters the clusters
     */
    public void setClusters(List<ChartCluster> clusters) {
      this.clusters = clusters;
    }
  }
}

