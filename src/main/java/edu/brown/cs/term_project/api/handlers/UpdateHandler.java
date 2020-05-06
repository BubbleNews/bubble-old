package edu.brown.cs.term_project.api.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.api.response.StandardResponse;
import edu.brown.cs.term_project.database.NewsData;
import spark.Request;
import spark.Response;
/**
 * Class that handles calling the Python API to get updated news headlines
 * and entity information from scraper/algorithm, will be called by scheduled
 * cron job.
 */
public final class UpdateHandler {

  /**
   * Constructor should not be called.
   */
  private UpdateHandler() {
    // not called
  }
  /**
   * Handles a request to the /update API.
   *
   * @param request  the request
   * @param response the response
   * @param db the database
   * @return a JSON response with status and message.
   */
  public static String handle(Request request, Response response, NewsData db) throws Exception {
    StandardResponse updateResponse = new StandardResponse(0, "");
    try {
      updateResponse.setMessage("We are not currently supporting Cron job updating.");
    } catch (Exception e) {
      // there has been an error so update response to reflect that
      updateResponse.setStatus(1);
      updateResponse.setMessage(e.getMessage());
    }
    // convert to particles.json and return
    return new Gson().toJson(updateResponse);
  }
}
