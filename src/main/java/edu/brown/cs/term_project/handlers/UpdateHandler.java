package edu.brown.cs.term_project.handlers;

import spark.Request;
import spark.Response;

/**
 * Class that handles calling the Python API to get updated news headlines
 * and entity information from scraper/algorithm, will be called by scheduled
 * cron job.
 */
public class UpdateHandler {

  /**
   * Handles a request to the /update API.
   * @param request the request
   * @param response the response
   * @return a JSON response with status and message.
   */
  public static String handle(Request request, Response response) {
    // TODO: implement update handle()
    return "update: unimplemented";
  }
}
