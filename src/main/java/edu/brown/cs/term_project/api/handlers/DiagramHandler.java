package edu.brown.cs.term_project.api.handlers;

import edu.brown.cs.term_project.database.NewsData;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles delivering home page HTML to user.
 */
public class DiagramHandler implements TemplateViewRoute {

  private NewsData db;

  /**
   * Constructor for a DiagramHandler.
   * @param db the database
   */
  public DiagramHandler(NewsData db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    Map<String, Object> variables = new HashMap<>();
    return new ModelAndView(variables, "diagram.ftl");
  }


}
