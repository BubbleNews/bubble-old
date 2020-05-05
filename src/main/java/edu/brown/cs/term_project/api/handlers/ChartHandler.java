package edu.brown.cs.term_project.api.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.database.NewsData;
import edu.brown.cs.term_project.api.response.ChartCluster;
import edu.brown.cs.term_project.api.response.ChartResponse;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A class for handling requests to the /chart API.
 */
public final class ChartHandler {

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
      Integer offset = Integer.parseInt(qm.value("offset"));
      String today = qm.value("today");


      String date = year + "-" + month + "-" + day;
      List<ChartCluster> clusters = new ArrayList<>();

      if (today.equals("true")) {
        clusters = db.getDataRead().getNewestClusters();
      } else if (offset > 0) {
        clusters = db.getDataRead().getClusters(date, offset, 1);
      } else {
        clusters = db.getDataRead().getClusters(date, 24 + offset, 0);
      }


      // query database for clusters from given date

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

