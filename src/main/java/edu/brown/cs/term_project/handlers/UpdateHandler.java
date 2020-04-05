package edu.brown.cs.term_project.handlers;

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
    // TODO: implement update handle()
    String uri = "http://127.0.0.1:5000/data";
    String toReturn = "";

    try {
      toReturn = sendGet(uri);
    } catch (Exception e) {
      toReturn = "update: error connecting to python api.";
    }
    return toReturn;
  }

  private static String sendGet(String uri) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uri))
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();
  }
}
