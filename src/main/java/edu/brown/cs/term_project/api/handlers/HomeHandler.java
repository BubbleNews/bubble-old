package edu.brown.cs.term_project.api.handlers;

import edu.brown.cs.term_project.database.NewsData;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that handles delivering home page HTML to user.
 */
public class HomeHandler implements TemplateViewRoute {

  private NewsData db;

  /**
   * Constructor for handler for home page.
   * @param db the database
   */
  public HomeHandler(NewsData db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    Set<String> sources = db.getDataRead().getSources();
    List<String> sourceList = new ArrayList<>(sources);
    Collections.sort(sourceList);
    Map<String, Object> variables = new HashMap<>();
    variables.put("sourceList", sourceList);
    return new ModelAndView(variables, "home.ftl");
  }
}
