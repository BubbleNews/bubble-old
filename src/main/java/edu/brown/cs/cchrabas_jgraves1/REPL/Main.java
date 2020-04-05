package edu.brown.cs.cchrabas_jgraves1.REPL;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheLoader;
import edu.brown.cs.cchrabas_jgraves1.TIMDB.Actor;
import edu.brown.cs.cchrabas_jgraves1.TIMDB.Connect;
import edu.brown.cs.cchrabas_jgraves1.TIMDB.Movie;
import edu.brown.cs.cchrabas_jgraves1.TIMDB.TIMDBMain;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;

import spark.QueryParamsMap;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {
  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  /**
   * Method to run entire program.
   */
  private void run() {
    // Parse command line arguments
    try {
      OptionParser parser = new OptionParser();
      parser.accepts("gui");
      parser.accepts("port").withRequiredArg().ofType(Integer.class)
          .defaultsTo(DEFAULT_PORT);
      OptionSet options = parser.parse(args);

      if (options.has("gui")) {
        runSparkServer((int) options.valueOf("port"));
      }

      // Starts REPL
      StartREPL start = new StartREPL();
      start.runREPL();
    } catch (OptionException e) {
      System.out.println("Not a correct flag");
    }

  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
              templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**.
   * Start gui page
   * @param port - port to go to
   */
  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/index", new FrontHandler(), freeMarker);
    Spark.get("/stars", new StarsHandler(), freeMarker);
    Spark.get("/timdb", new TimdbHandler(), freeMarker);
    Spark.post("/neighbors1", new SubmitNpoint(), freeMarker);
    Spark.post("/neighbors2", new SubmitNname(), freeMarker);
    Spark.post("/radius1", new SubmitRpoint(), freeMarker);
    Spark.post("/radius2", new SubmitRname(), freeMarker);
    Spark.post("/connectActors", new SubmitConnect(), freeMarker);
    Spark.get("/movies/:id", new MovieHandler(), freeMarker);
    Spark.get("/actors/:id", new ActorHandler(), freeMarker);
  }

  /**
   * Handle requests to the index page of our quering website.
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
              "Index: Query the databases");
      return new ModelAndView(variables, "index.ftl");
    }
  }

  /**
   * Handle requests to the query page of our Stars website.
   */
  private static class StarsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
              "Stars: Query the database", "result", "");
      return new ModelAndView(variables, "starsQuery.ftl");
    }
  }

  /**
   * Handle requests to the query page of our TIMDB website.
   */
  private static class TimdbHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Timdb: Query the database",
              "result", "Input two actors to check for a path between them!");
      return new ModelAndView(variables, "timdbQuery.ftl");
    }
  }

  /**
   * Handle requests to personal actor pages.
   */
  private static class ActorHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String id = "/m/" + req.params(":id");
      try {
        String name = TIMDBMain.getActorExplorer().lookupName(id);
        List<Movie> actorsMovies = TIMDBMain.getActorExplorer().findMovies(id);
        String output = "";
        for (Movie filmOfActor : actorsMovies) {
          output = output + "<li><a href=\"/movies/" + filmOfActor.getIDNoM() + "\">" + filmOfActor.getMovie() + "</a></li>";
        }
        Map<String, String> variables = ImmutableMap.of("title", name, "itemName",
                name, "links", output);
        return new ModelAndView(variables, "personalPage.ftl");
      } catch (SQLException e) {
        throw new IllegalArgumentException();
      } catch (NullPointerException e) {
        Map<String, String> variables = ImmutableMap.of("title", "Not Found", "itemName",
                "Please load database!", "links", "");
        return new ModelAndView(variables, "personalPage.ftl");
      }
    }
  }

  /**
   * Handle requests to personal movie pages.
   */
  private static class MovieHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String id = "/m/" + req.params(":id");
      try {
        String name = TIMDBMain.getActorExplorer().lookupMovie(id);
        List<Actor> actorsInFilm = TIMDBMain.getActorExplorer().findActors(id);
        String output = "";
        for (Actor actorInFilm: actorsInFilm) {
          output = output + "<li><a href=\"/actors/" + actorInFilm.getIDNoM() + "\">" + actorInFilm.getName() + "</a></li>";
        }
        Map<String, String> variables = ImmutableMap.of("title", name, "itemName",
                name, "links", output);
        return new ModelAndView(variables, "personalPage.ftl");
      } catch (SQLException e) {
        throw new IllegalArgumentException();
      } catch (NullPointerException e) {
        Map<String, String> variables = ImmutableMap.of("title", "Not Found", " itemName",
                "Please load database!", "links", "");
        return new ModelAndView(variables, "personalPage.ftl");

      }
    }
  }

  /**
   * Handle connect command.
   */
  private static class SubmitConnect implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String startName = qm.value("a1");
      String endName = qm.value("a2");
      String toReturn = "";
      if (startName.equals("") || endName.equals("")) {
        toReturn = "ERROR: Please input two names";
      } else if (startName.equals(endName)) {
        toReturn = "ERROR: The search must contain two unique actor names.";
      } else {
        try {
          List<Movie> movies = Connect.connectedEdges(startName, endName);
          try {
            toReturn = "<p>";
            for (Movie m : movies) {
              toReturn += "<a href=\"/actors/" + m.getStart().getIDNoM() + "\"  style=\"color: white\">" + m.getStart().getName() + "</a>" + " -> "
                      + "<a href=\"/actors/" + m.getEnd().getIDNoM() + "\"  style=\"color: white\">" + m.getEnd().getName() + "</a>" + " : "
                      + "<a href=\"/movies/" + m.getIDNoM() + "\"  style=\"color: white\">" + m.getMovie() + "</a><br>\n";
              toReturn += "</p>";
            }
          } catch (NullPointerException e) {
            String idA = TIMDBMain.getIdCache().get(startName);
            String idB = TIMDBMain.getIdCache().get(endName);
            toReturn = "<p><a href=\"/actors/" + idA.replace("/m/", "") + "\">" + startName + "</a>" + " -/- "
                    + "<a href=\"/actors/" + idB.replace("/m/", "") + "\">" + endName + "</a></p>";
          }
        } catch (CacheLoader.InvalidCacheLoadException e) {
          toReturn = "ERROR: Name not found";
        } catch (ExecutionException e) {
          throw new IllegalArgumentException();
        } catch (NullPointerException e) {
          toReturn = "ERROR: Database Not Loaded";
        }
      }
      Map<String, String> variables = ImmutableMap.of("title",
              "TIMDB: Query the database", "result", toReturn);
      return new ModelAndView(variables, "timdbQuery.ftl");
    }
  }

  /**.
   * Handle Neighbors command from a point
   */
  private static class SubmitNpoint implements TemplateViewRoute {
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String k = qm.value("nK1");
      String x = qm.value("nX");
      String y = qm.value("nY");
      String z = qm.value("nZ");
      String command = "neighbors " + k + " " + x + " " + y + " " + z;
      String output = StartREPL.getStartCM().action(command, new PrintWriter(System.out));
      Map<String, String> variables = ImmutableMap.of("title",
              "Stars: Query the database", "result", output);
      return new ModelAndView(variables, "starsQuery.ftl");
    }
  }

  /**.
   * Handle neighbors command from a star name
   */
  private static class SubmitNname implements TemplateViewRoute {
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String k = qm.value("nK2");
      String name = qm.value("nName");
      String command = "neighbors " + k + " \"" + name + "\"";
      String output = StartREPL.getStartCM().action(command, new PrintWriter(System.out));
      Map<String, String> variables = ImmutableMap.of("title",
              "Stars: Query the database", "result", output);
      return new ModelAndView(variables, "starsQuery.ftl");
    }
  }

  /**.
   * Handle radius command from a point
   */
  private static class SubmitRpoint implements TemplateViewRoute {
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String k = qm.value("rK1");
      String x = qm.value("rX");
      String y = qm.value("rY");
      String z = qm.value("rZ");
      String command = "radius " + k + " " + x + " " + y + " " + z;
      String output = StartREPL.getStartCM().action(command, new PrintWriter(System.out));
      Map<String, String> variables = ImmutableMap.of("title",
              "Stars: Query the database", "result", output);
      return new ModelAndView(variables, "starsQuery.ftl");
    }
  }

  /**.
   * Handle radius command from a star name
   */
  private static class SubmitRname implements TemplateViewRoute {
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String k = qm.value("rK2");
      String name = qm.value("rName");
      String command = "radius " + k + " \"" + name + "\"";
      String output = StartREPL.getStartCM().action(command, new PrintWriter(System.out));
      Map<String, String> variables = ImmutableMap.of("title",
              "Stars: Query the database", "result", output);
      return new ModelAndView(variables, "starsQuery.ftl");
    }
  }

  /**.
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
