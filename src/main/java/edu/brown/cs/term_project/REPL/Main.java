package edu.brown.cs.term_project.REPL;

import edu.brown.cs.term_project.Bubble.ArticleGraph;
import edu.brown.cs.term_project.handlers.ClusterHandler;
import edu.brown.cs.term_project.handlers.ChartHandler;
import edu.brown.cs.term_project.handlers.HomeHandler;
import edu.brown.cs.term_project.handlers.UpdateHandler;
import edu.brown.cs.term_project.nlp.ExtractEntities;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.template.freemarker.FreeMarkerEngine;
import freemarker.template.Configuration;

import java.io.File;
import java.io.IOException;

import static spark.Spark.*;

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

    } catch (OptionException e) {
      System.out.println("Not a correct flag");
    }

    System.out.println("REPL");
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
    port(port);
    externalStaticFileLocation("src/main/resources/static");
    // Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    path("/bubble", () -> {
      // home page endpoint
      get("/home", new HomeHandler(), freeMarker);
      // api endpoints
      path("/api", () -> {
        // TODO: add authentication for api calls with before()
        get("/update", UpdateHandler::handle);
        get("/chart", ChartHandler::handle);
        get("/cluster", ClusterHandler::handle);
      });
    });
  }
}
