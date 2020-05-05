package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.NewsData;
import edu.brown.cs.term_project.Graph.ChartCluster;
import freemarker.template.SimpleDate;
import org.apache.commons.lang3.time.DateUtils;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
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
      // get date object
      String year = request.queryParams("year");
      String month = request.queryParams("month");
      String day = request.queryParams("day");
      Integer offset = Integer.parseInt(request.queryParams("offset"));
      String today = request.queryParams("today");


      String date = year + "-" + month + "-" + day;

      if (today.equals("true")) {
        db.getMostRecent()
      } else if (offset > 0) {
        db.getClusters(date, offset, 1)
      } else {
        db.getClusters(date, 24 + offset, 0)
      }




      // query database for clusters from given date
      List<ChartCluster> clusters = db.getClusters(dateString);

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

