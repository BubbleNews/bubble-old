package edu.brown.cs.term_project.REPL;

import edu.brown.cs.term_project.Bubble.NewsData;
import edu.brown.cs.term_project.handlers.ClusterHandler;
import edu.brown.cs.term_project.handlers.ChartHandler;
import edu.brown.cs.term_project.handlers.HomeHandler;
import edu.brown.cs.term_project.handlers.UpdateHandler;
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
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    DATABASE = new NewsData("data/backloaded big Cluster 1.db");
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

//    String article = "On a cold morning in February 2018, a group of 30 microbiologists, zoologists and public-health experts from around the world met at the headquarters of the World Health Organization in Geneva. The group was established by the W.H.O. in 2015 to create a priority list of dangerous viruses — specifically, those for which no vaccines or drugs were already in development. The consensus, at least among those in the room, was that as populations and global travel continued to grow and development increasingly pushed into wild areas, it was almost inevitable that once-containable local outbreaks, like SARS or Ebola, could become global disasters.\n" +
//        "“The meeting was in a big room, with all the tables arranged around the edge, facing each other,” one of the group’s members, Peter Daszak, recalled recently. “It was a very formal process. Each person was asked to present the case for including a particular disease on the list of top threats. And everything you say is being taken down, and checked factually, and recorded.”\n" +
//        "Daszak, who directs the pandemic-prevention group EcoHealth Alliance and is also chairman of the Forum on Microbial Threats at the National Academy of Sciences, Engineering and Medicine, had been given the task of presenting on SARS, a lethal coronavirus that killed roughly 800 people after it emerged in 2002. (SARS stands for Severe Acute Respiratory Syndrome and is officially known as SARS-CoV-1.) “We’d done a lot of research on coronaviruses, so we knew they were a clear and present danger,” he told me. “High mortality, no drugs or vaccines in the pipeline, with new variants that could still be emerging.”\n" +
//        "The discussion, he said, was intense. “Everyone else in the room knows the facts already — they’ve read all the research,” Daszak said. But for each pathogen, the speaker had to convince the room that it presented a significant threat — “that this disease really could take off, and that we should concentrate on it rather than on Lassa fever or something else. So, you argue the case, and then people vote. And sometimes it gets quite heated. I remember that monkey pox was an issue, because there are outbreaks, but there’s really nothing we can do about them. It was a really rigorous, really excellent debate — and then afterward, we went and had fondue.”\n" +
//        "The final list — which did contain SARS and MERS, along with seven other respiratory, hemorrhagic or otherwise-lethal viruses — also included something the W.H.O. dubbed “Disease X”: a stand-in for all the unknown pathogens, or devastating variations on existing pathogens, that had yet to emerge. Daszak describes Covid-19, the disease caused by the virus SARS-CoV-2, as exactly the kind of threat that Disease X was meant to represent: a novel, highly infectious coronavirus, with a high mortality rate, and no existing treatment or prevention. “The problem isn’t that prevention was impossible,” Daszak told me. “It was very possible. But we didn’t do it. Governments thought it was too expensive. Pharmaceutical companies operate for profit.” And the W.H.O., for the most part, had neither the funding nor the power to enforce the large-scale global collaboration necessary to combat it.\n" +
//        "As Covid-19 has spread around the world, overwhelming hospitals and even mortuaries, there has been widespread consternation over how we could have been caught so flat-footed by a virus. Given all the shining advances of high-tech medicine — computer-controlled surgery, unprecedented immunotherapies, artificial-intelligence programs for assessing heart-disease risk — this failure feels utterly baffling. How could the entire world remain so powerless? More important, what could be different next time?\n" +
//        "\n" +
//        "According to some infectious-disease experts, the scientific tools already exist to create a kind of viral-defense department — one that would allow us to pursue a broad range of vital global projects, from developing vaccines and drugs that work against a wide range of pathogens to monitoring disease hot spots and identifying potential high-risk viruses, both known and unknown. What’s lacking is resources. “We really did miss the wake-up call,” Daszak says. “The alarm went off with SARS, and we hit the snooze button. And then we hit it again with Ebola, with MERS, with Zika. Now that we’re awake, we should think about where to go from here.”\n" +
//        "In late March, Vincent Racaniello, host of the podcast “This Week in Virology” and a professor at Columbia University, conducted an interview with the pediatric infectious-disease expert Mark Denison. Denison, who teaches at Vanderbilt University Medical Center, led a team that developed one of the most promising current treatments for Covid-19: the drug remdesivir, currently being tested by the pharmaceutical company Gilead Sciences.\n" +
//        "On the show, Denison noted that because it is almost impossible to predict which virus might cause the next pandemic, researchers had long argued that it was essential to design panviral drugs and vaccines that would be effective against a wide range of strains: all types of influenza, for instance, or a substantial group of coronaviruses rather than just one. When his lab was first applying for a grant to study remdesivir, Denison recalled, that was already the goal. “We don’t want to work with a compound unless it inhibits every coronavirus we test,” Denison said. “Because we’re worried about MERS, we’re worried about SARS-1, but they’re not really our problem. The future is the problem.”\n" +
//        "Panviral drugs — ones that work broadly within or across virus families — are harder to make than broad-spectrum antibiotics, largely because viruses work by hijacking the machinery of our cells, harnessing their key functions in order to replicate. A drug that blocks one of those functions (e.g., the production of a particular protein) is often also disrupting something that our own cells need to survive. Researchers have begun to find ways around that problem, in part by refining which process a drug targets. But they’ve also begun to test existing drugs against a wider array of viruses. It was in just such a follow-up screen that Gilead discovered that remdesivir, originally developed to treat hepatitis C and later tried against Ebola, might be effective against coronaviruses. (Favipiravir, an influenza drug developed in Japan, is another broad-spectrum candidate.) The reason drugs sometimes work in extremely different diseases — in, say, Ebola and coronaviruses and flu — is that they block some common mechanism. Remdesivir and favipiravir, for instance, each mimics a key building block in a virus’s RNA, which, when inserted, keeps the virus from replicating. “It’s definitely possible to make a drug that would work across a good range of coronaviruses,” Racaniello says. “We honestly should have had one long ago, since SARS in 2003. It would have taken care of this outbreak in China before it got out. And the only reason we didn’t is because there wasn’t enough financial backing.";
////
//    TextProcessing.getEntityFrequencies(article);
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
        get("/update", (Request request, Response response) -> {
          return UpdateHandler.handle(request, response, DATABASE);
        });
        get("/chart", (Request request, Response response) -> {
          return ChartHandler.handle(request, response, DATABASE);
        });
        get("/cluster", (Request request, Response response) -> {
          return ClusterHandler.handle(request, response, DATABASE);
        });
      });
    });
  }
}
