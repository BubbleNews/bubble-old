package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.NewsData;
import edu.brown.cs.term_project.Graph.ChartCluster;
import freemarker.template.SimpleDate;
import org.apache.commons.lang3.time.DateUtils;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A class for handling requests to the /chart API.
 */
public final class ChartHandler {

  /**
   * Handles a request to the /chart API.
   * @param request the request
   * @param response the response
   * @param db the news database
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

        // This is what Kshitij did
        // convert date from string to Java.util.Date object
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormatter.parse(dateString);
        boolean intermediate_clusters = isToday(date);

        // query database for clusters from given date
        Set<ChartCluster> relevantClusters = db.getClusters(new java.sql.Date(date.getTime()), intermediate_clusters);
      }

    } catch (Exception e) {
      chartResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(chartResponse);
  }

  // Checks if input date is the same as today's date
  private static boolean isToday(Date date) {
    return DateUtils.isSameDay(date, Calendar.getInstance().getTime());
  }

  private static List<ChartCluster> mockClusters() {
    List<ChartCluster> toReturn = new ArrayList<>();
    return toReturn;
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

