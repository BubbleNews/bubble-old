package edu.brown.cs.term_project.api.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.database.NewsData;
import edu.brown.cs.term_project.api.response.ChartCluster;
import edu.brown.cs.term_project.api.response.ChartResponse;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import java.util.Comparator;
import java.util.List;

/**
 * A class for handling requests to the /chart API.
 */
public final class ChartHandler {

  private static final int HOURS_PER_DAY = 24;

  /**
   * Constructor - should never be called.
   */
  private ChartHandler() {
  }

  /**
   * Handles a request to the /chart API by getting the clusters for the inputted
   * day.
   * @param request the request
   * @param response the response
   * @param db the news database
   * @return a JSON response with chart made up of list of clusters
   */
  public static String handle(Request request, Response response, NewsData db) {
    ChartResponse chartResponse = new ChartResponse(0, "");
    try {
      // get date object
      QueryParamsMap qm = request.queryMap();
      String year = qm.value("year");
      String month = qm.value("month");
      String day = qm.value("day");
      int offset = Integer.parseInt(qm.value("offset"));
      boolean today = Boolean.parseBoolean(qm.value("isToday"));

      // get clusters for the correct date, adjusting for time zone
      String date = year + "-" + month + "-" + day;
      List<ChartCluster> clusters;
      if (today) {
        clusters = db.getDataRead().getNewestClusters();
      } else {
        clusters = db.getDataRead().getClusterByDay(date);
      }
      // sort by size
      Comparator<ChartCluster> compareBySize =
          (ChartCluster c1, ChartCluster c2) -> c2.getSize() - c1.getSize();
      clusters.sort(compareBySize);
      // pass to front handler
      chartResponse.setClusters(clusters);
    } catch (Exception e) {
      chartResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(chartResponse);
  }
}

