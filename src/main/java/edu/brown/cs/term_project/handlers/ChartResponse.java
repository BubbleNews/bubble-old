package edu.brown.cs.term_project.handlers;

import edu.brown.cs.term_project.Graph.ChartCluster;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartResponse extends StandardResponse {
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
    clusters = new ArrayList<>();
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