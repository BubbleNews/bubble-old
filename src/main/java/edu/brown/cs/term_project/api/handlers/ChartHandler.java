package edu.brown.cs.term_project.api.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.database.NewsData;
import edu.brown.cs.term_project.api.response.ChartCluster;
import edu.brown.cs.term_project.api.response.ChartResponse;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

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
      String dateString = qm.value("date");
      // query database for clusters from given date
      List<ChartCluster> clusters = db.getDataRead().getClusters(dateString);
      // pass to front handler
      chartResponse.setClusters(clusters);
    } catch (Exception e) {
      chartResponse.setErrorMessage(e.getMessage());
    }
    return new Gson().toJson(chartResponse);
  }
}

