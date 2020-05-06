package edu.brown.cs.term_project.api.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.api.pipeline.NewsClusterer;
import edu.brown.cs.term_project.api.pipeline.NewsLoader;
import edu.brown.cs.term_project.api.response.StandardResponse;
import edu.brown.cs.term_project.clustering.ClusterParameters;
import edu.brown.cs.term_project.database.NewsData;
import spark.Request;
import spark.Response;

/**
 * Class that handles calling the Python API to get updated news headlines
 * and entity information from scraper/algorithm, will be called by scheduled
 * cron job.
 */
public final class UpdateHandler {
  private static final int ARTICLES_PER_BATCH = 100;
  private static final int LOADER_NUMBER_OF_BATCHES = 1;
  private static final int LOADER_STEP_BACK = 0;
  private static final int LOADER_TIME_STEP = 1;
  private static final int CLUSTER_HOURS = 24;

  /**
   * Constructor should not be called.
   */
  private UpdateHandler() {
    // Should not be called
  }
  /**
   * Handles a request to the /update API.
   *
   * @param request  the request
   * @param response the response
   * @param db the database
   * @return a JSON response with status and message.
   */
  public static String handle(Request request, Response response, NewsData db) {
    StandardResponse updateResponse = new StandardResponse(0, "");
    try {
      NewsLoader loadData = new NewsLoader(new NewsData("data/final_data.db"),
            "http://127.0.0.1:5000/scrape");
      loadData.executeBatches(LOADER_NUMBER_OF_BATCHES, ARTICLES_PER_BATCH, LOADER_TIME_STEP, LOADER_STEP_BACK);
      ClusterParameters params = new ClusterParameters(CLUSTER_HOURS, true);
      NewsClusterer clusterer = new NewsClusterer(new NewsData("data/final_data.db"));
      clusterer.clusterArticles(params);
    } catch (Exception e) {
      // there has been an error so update response to reflect that
      updateResponse.setErrorMessage(e.getMessage());
    }
    // convert to particles.json and return
    return new Gson().toJson(updateResponse);
  }
}
