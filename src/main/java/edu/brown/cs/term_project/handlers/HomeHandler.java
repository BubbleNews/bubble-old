package edu.brown.cs.term_project.handlers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles delivering home page HTML to user.
 */
public class HomeHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    Map<String, String> variables = new HashMap<>();
    return new ModelAndView(variables, "home.ftl");
  }
}
