package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.Cluster;
import spark.Request;
import spark.Response;

import java.util.Date;
import java.util.List;

/**
 * A class for handling requests to the /chart API.
 */
public class ChartHandler {

  /**
   * Handles a request to the /chart API.
   * @param request the request
   * @param response the response
   * @return a JSON response with chart made up of list of clusters
   */
  public static String handle(Request request, Response response) {
    ChartResponse chartResponse = new ChartResponse(0, "");

    // TODO: get chart

    return new Gson().toJson(chartResponse);
  }

  private static class ChartResponse extends StandardResponse {
    private Date date;
    private List<Cluster> clusters; // list of clusters in a chart

    /**
     * Constructor for the response.
     *
     * @param status  0 successful, 1 error
     * @param message error message if error
     */
    public ChartResponse(int status, String message) {
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
    public void setClusters(List<Cluster> clusters) {
      this.clusters = clusters;
    }
  }
}
