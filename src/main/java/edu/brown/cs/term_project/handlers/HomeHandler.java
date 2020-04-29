package edu.brown.cs.term_project.handlers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;
import java.util.Map;

import static edu.brown.cs.term_project.handlers.HTTPRequests.sendGet;

/**
 * Class that handles delivering home page HTML to user.
 */
public class HomeHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    String sourcesUrl = "http://127.0.0.1:5000/sources";
    String sourcesString = sendGet(sourcesUrl);
    String[] sources = sourcesString.split(",");
    Map<String, Object> variables = new HashMap<>();
    variables.put("sourceList", sources);
    return new ModelAndView(variables, "home.ftl");
  }


}
