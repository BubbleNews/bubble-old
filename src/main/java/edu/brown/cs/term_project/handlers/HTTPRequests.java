package edu.brown.cs.term_project.handlers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class HTTPRequests {

  /**
   * Sends an HTTP GET request to the python endpoint.
   *
   ** @return a json string response
   * @throws Exception
   */
  public static String sendGet(String url) throws Exception {

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();
  }
}
