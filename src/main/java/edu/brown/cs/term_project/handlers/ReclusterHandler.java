package edu.brown.cs.term_project.handlers;

import edu.brown.cs.term_project.Bubble.NewsData;
import spark.Request;
import spark.Response;

public class ReclusterHandler {
  /**
   * Handles a request to the /recluster API.
   * @param request the request
   * @param response the response
   * @param db the news database
   * @return a JSON response with a list of new clusters
   */
  public static String handle(Request request, Response response, NewsData db) {
    return "";
  }
}
