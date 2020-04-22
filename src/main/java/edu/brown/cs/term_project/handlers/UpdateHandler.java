package edu.brown.cs.term_project.handlers;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * Class that handles calling the Python API to get updated news headlines
 * and entity information from scraper/algorithm, will be called by scheduled
 * cron job.
 */
public class UpdateHandler {

  /**
   * Handles a request to the /update API.
   *
   * @param request  the request
   * @param response the response
   * @return a JSON response with status and message.
   */
  public static String handle(Request request, Response response) {
    String pythonEndpoint = "http://127.0.0.1:5000/scrape";
    StandardResponse updateResponse = new StandardResponse(0, "");
    try {
      String pythonResponse = sendGet(pythonEndpoint);

      Gson gson = new Gson();
      JsonParser parser = new JsonParser();
      JsonArray jsonArray = parser.parse(pythonResponse).getAsJsonArray();
      List<ArticleJSON> articles = new ArrayList<>();
      for (int i = 0; i < jsonArray.size(); i++) {
        articles.add(gson.fromJson(jsonArray.get(i), ArticleJSON.class));
      }

      for (int i = 0; i < jsonArray.size(); i++) {
        System.out.println(articles.get(i).url);
      }

      // stem words
      // add to database

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
   *
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


  private static class ArticleJSON {

    private String[] authors;
    private String title;
    private String description;
    public String url;
    private String timePublished;
    private String content;

    private ArticleJSON(String[] authors, String title, String description, String url,
                        String timePublished, String content) {
      this.authors = authors;
      this.title = title;
      this.description = description;
      this.url = url;
      this.timePublished = timePublished;
      this.content = content;
    }
  }

}
