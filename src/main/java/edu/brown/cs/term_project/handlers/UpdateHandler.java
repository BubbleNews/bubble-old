package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
    String pythonEndpoint = "http://127.0.0.1:5000/scrape";
    StandardResponse updateResponse = new StandardResponse(0, "");
    try {
      String pythonResponse = sendGet(pythonEndpoint);

      // WE NEED TO PARSE THE JSON STRING INTO A LIST OF SOMETHING THEN ADD TO DB


    } catch (Exception e) {
      // there has been an error so update response to reflect that
      updateResponse.setStatus(1);
      updateResponse.setMessage(e.getMessage());
    }
    // convert to json and return
    return new Gson().toJson(updateResponse);
  }

  /**
   * Sends an HTTP GET request to a url.
   * @param uri the url to send the get request to
   * @return a json string response
   * @throws Exception
   */
  private static String sendGet(String uri) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uri))
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();
  }
}
