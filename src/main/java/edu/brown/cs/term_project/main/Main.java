package edu.brown.cs.term_project.main;

import edu.brown.cs.term_project.database.NewsData;
import edu.brown.cs.term_project.api.handlers.ChartHandler;
import edu.brown.cs.term_project.api.handlers.ClusterDetailHandler;
import edu.brown.cs.term_project.api.handlers.ClusterHandler;
import edu.brown.cs.term_project.api.handlers.DiagramHandler;
import edu.brown.cs.term_project.api.handlers.EdgeHandler;
import edu.brown.cs.term_project.api.handlers.HomeHandler;
import edu.brown.cs.term_project.api.handlers.ReclusterHandler;
import edu.brown.cs.term_project.api.handlers.UpdateHandler;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;
import freemarker.template.Configuration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static spark.Spark.*;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {
  private static final int DEFAULT_PORT = 4567;

  private static NewsData DATABASE;

  /**
   * The initial method called when execution begins.
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    DATABASE = new NewsData("data/mock_data.db");
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

      runSparkServer((int) options.valueOf("port"));
    } catch (OptionException e) {
      System.out.println("Not a correct flag");
    }
  }

  /**
   * Method to get a port assigned by Heroku.
   * @return the port
   */
  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return DEFAULT_PORT; //return default port if heroku-port isn't set (i.e. on localhost)
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
   * Start gui page.
   * @param port - port to go to
   */
  private void runSparkServer(int port) {
    port(getHerokuAssignedPort());
    externalStaticFileLocation("src/main/resources/static");
    // Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    path("/bubble", () -> {
      // home page endpoint
      get("/home", new HomeHandler(DATABASE), freeMarker);
      get("/diagram", new DiagramHandler(DATABASE), freeMarker);
      // api endpoints
      path("/api", () -> {
        // TODO: add authentication for api calls with before()
        get("/update", (Request request, Response response) -> {
          return UpdateHandler.handle(request, response, DATABASE);
        });
        get("/chart", (Request request, Response response) -> {
          return ChartHandler.handle(request, response, DATABASE);
        });
        get("/cluster", (Request request, Response response) -> {
          return ClusterHandler.handle(request, response, DATABASE);
        });
        get("/details", (Request request, Response response) -> {
          return ClusterDetailHandler.handle(request, response, DATABASE);
        });
        post("/recluster", (Request request, Response response) -> {
          return ReclusterHandler.handle(request, response, DATABASE);
        });
        get("/edge", (Request request, Response response) -> {
          return EdgeHandler.handle(request, response, DATABASE);
        });
      });
    });
  }
}
