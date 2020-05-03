package edu.brown.cs.term_project.handlers;

import edu.brown.cs.term_project.Bubble.NewsData;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.brown.cs.term_project.handlers.HTTPRequests.sendGet;

/**
 * Class that handles delivering home page HTML to user.
 */
public class DiagramHandler implements TemplateViewRoute {

  private NewsData db;

  public DiagramHandler(NewsData db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    Map<String, Object> variables = new HashMap<>();
    return new ModelAndView(variables, "diagram.ftl");
  }


}
