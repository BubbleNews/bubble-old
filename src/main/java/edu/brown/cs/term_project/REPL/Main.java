package edu.brown.cs.term_project.REPL;

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
  String articleText = "Ottawa (CNN) A gunman killed at least 16 people, including one Mountie, during a weekend shooting rampage in small-town Nova Scotia, the Royal Canadian Mounted Police said.\n" +
      "\n" +
      "The updated death toll does not include the suspected gunman.\n" +
      "\n" +
      "\"Our important investigational work is continuing. Out of respect for the families who have yet to be notified, we cannot provide any additional information at this time,\" said RCMP National Headquarters spokeswoman Catherine Fortin in a statement to CNN.\n" +
      "\n" +
      "At a media conference Sunday evening, police described chaos with multiple 911 calls coming in late Saturday at a property in Portapique, Nova Scotia.\n" +
      "\n" +
      "\"When police arrived at the scene the members located several casualties inside and outside of the home,\" RCMP Chief Superintendent Chris Leather told reporters.\n" +
      "\n" +
      "Police said they are not certain how many people died or were injured.\n" +
      "\n" +
      "Leather said a search for the suspect lasted overnight and led them to several crime scenes miles apart. The suspect was eventually spotted late Sunday morning at a truck stop.\n" +
      "\n" +
      "\"The search for the suspect ended this morning when the suspect was located and I can confirm that he is deceased,\" said Leather.\n" +
      "\n" +
      "Nova Scotia RCMP identified the suspected gunman as Gabriel Wortman, 51. He eluded police for several hours overnight Sunday as fires were reported in many locations in the area.\n" +
      "\n" +
      "The manhunt was complicated by the fact that the suspect is believed to have been wearing as least part of what looked to be an RCMP uniform, and may have been driving a vehicle made to look like a police car, Leather said.\n" +
      "\n" +
      "\"Of course that's an important element in the investigation, the fact that this individual had a uniform and a police car at his disposal certainly speaks to it not being a random act,\" Leather said.\n" +
      "\n" +
      "But Leather said it was too early in the investigation to tell what the gunman's motivation was and that some victims \"appeared not to have a relationship with the assailant shooter.\"\n" +
      "\n" +
      "A law enforcement told CNN earlier that authorities were searching half a dozen crime scenes, beginning in Portapique and stretching to Enfield, where the suspect was apprehended.\n" +
      "\n" +
      "As they chased the gunman, police told residents to stay inside and lock their doors, warning that he was considered \"armed and dangerous.\"\n" +
      "\n" +
      "\"She goes, 'Oh my God, lock the doors, he's here! And I peek out of the window and I saw some RCMP vehicles and there was four or five uniforms with guns,\" Nurani said.\n" +
      "\n" +
      "Witness Glen Hines was driving past and told CTV he heard gunfire. \"All I could hear was gunshots and my wife, I thought I was going to call 911, because she was going into panic, it scared her so bad,\" Hines said.\n" +
      "\n" +
      "Stephen McNeil, the premier of Nova Scotia, called the shooting spree \"one of the most senseless acts of violence in our province's history.\"\n" +
      "\n" +
      "At a media conference on Covid-19, he told reporters: \"I never imagined when I went to bed last night that I would wake up to the horrific news that an active shooter was on the loose in Nova Scotia.\"\n" +
      "\n" +
      "National Police Union President Brian Sauvé said members' \"hearts are heavy with grief and sadness today as we have lost one of our own.\" He praised the officers who helped end the attack.\n" +
      "\n" +
      "\"A second dedicated Member was injured in the line of duty today. We will do everything we can to support them and wish them a full recovery.\"\n" +
      "\n" +
      "In a statement, Canadian Minister of Public Safety Bill Blair said it was a \"truly heartbreaking day for all of Canada.\"\n" +
      "\n" +
      "\"Tragedies like today are horrific and should never happen. I want to share my deepest condolences with all those suffering right now, particularly the families and friends of the many innocent people that were killed,\" Blair said. \"As families mourn and search for answers, our hearts are broken alongside theirs. We grieve with them.\"\n" +
      "finished parsing\n" +
      "Just because we’re apart, doesn’t mean we can’t do things together";

    ExtractEntities.getEntities(articleText);
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
