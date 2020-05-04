package edu.brown.cs.term_project.api.handlers;

import com.google.gson.Gson;
import edu.brown.cs.term_project.Bubble.*;
import edu.brown.cs.term_project.api.response.StandardResponse;
import spark.Request;
import spark.Response;

import java.util.*;

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
   * @param db the database
   * @return a JSON response with status and message.
   */
  public static String handle(Request request, Response response, NewsData db) throws Exception {
    StandardResponse updateResponse = new StandardResponse(0, "");
    try {


    } catch (Exception e) {
      // there has been an error so update response to reflect that
      updateResponse.setStatus(1);
      updateResponse.setMessage(e.getMessage());
    }
    // convert to particles.json and return
    return new Gson().toJson(updateResponse);
  }

  public static void main(String[] args) throws Exception {
    Article testArticle = new Article("BuzzFeed", new String[]{"Kayla Suazo"},
        "23 Top-Rated Cleaning Products That Are Popular For A Reason",
        "So good, they have *a ton* of 4- and 5-star reviews.",
        "https://www.buzzfeed.com/kaylasuazo/top-rated-cleaning-products-"
            + "that-are-popular-for-a-reason",
        "2020-04-27T17:22:24.963019Z",
        "all you have to do be let it sit for 15 minute and wipe -- minimal work on you "
            + "part . check out BuzzFeed 's full write-up on this Feed-N-Wax Wood Polish to learn "
            + "more!and ! it have 4,700 + positive review on amazon.promising review : `` OMG ! "
            + "this be the most amazing product ! we inherit some antique furniture from the '30s"
            + " that have be in storage forever ... it be dry and dirty and not much to look at ."
            + " I use this product on it and the oak wood literally come alive show the beautiful"
            + " grain and texture of the wood . I have since use it on my oak kitchen cabinet and"
            + " they look AMAZING ! I will never use anything else other than this product on my "
            + "wood surface ! no greasy feel -- and a fantastic smell ! '' -- Tiffany SadowskiGet"
            + " it from Amazon for $ 8.48 + -lrb- available in eight size -rrb- ."
    );

    NewsData db = new NewsData("data/backloaded.db");
    db.insertArticleAndEntities(testArticle, new HashMap<>());

//    ArrayList<ArticleJSON> testList = new ArrayList<>();
//    testList.add(testArticle);
//    //processJSONArticles(testList, db);
//    clusterArticles(db);
  }
}
