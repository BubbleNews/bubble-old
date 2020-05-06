package edu.brown.cs.term_project.api.pipeline;

import edu.brown.cs.term_project.bubble.Article;
import edu.brown.cs.term_project.bubble.ArticleVertex;
import edu.brown.cs.term_project.bubble.ArticleWord;
import edu.brown.cs.term_project.bubble.Entity;
import edu.brown.cs.term_project.database.NewsData;
import edu.brown.cs.term_project.bubble.Similarity;
import edu.brown.cs.term_project.api.response.ChartCluster;
import edu.brown.cs.term_project.clustering.Cluster;
import edu.brown.cs.term_project.clustering.ClusterParameters;
import edu.brown.cs.term_project.graph.Graph;
import edu.brown.cs.term_project.similarity.TextCorpus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that directs all clustering of articles in the application.
 */
public class NewsClusterer {
  private static final int DEFAULT_CLUSTER_METHOD = 1;
  private static final int NUM_EDGES_TO_PRINT = 25;
  private NewsData db;

  /**
   * Constructor for a clusterer.
   * @param db the database storing the articles
   */
  public NewsClusterer(NewsData db) {
    this.db = db;
  }

  /**
   * Clusters articles according to the inputted parameters.
   * @param params an object storing the parameters for clustering
   * @return a list of chart cluster objects of the clusters
   * @throws SQLException should never be thrown
   */
  public List<ChartCluster> clusterArticles(ClusterParameters params) throws SQLException {
    final int offset = 4;
    Set<ArticleVertex> pulledArticles = db.getDataRead().getArticleVertices(
        params.getDate(), offset, params.getHours(), params.isToday(),
        params.getNumArticles());
    List<Similarity> edges = getEdges(pulledArticles, params);
    // sort edges
    edges.sort(Comparator.comparingDouble(Similarity::getDistance));
    // make graph from vertices and cluster
    Graph<ArticleVertex, Similarity> graph = new Graph<>(pulledArticles, edges);

    graph.runClusters(DEFAULT_CLUSTER_METHOD);
    if (params.getDoInsert()) {
      db.getDataWrite().insertClusters(graph.getClusters());
    }
    // create list of chart clusters
    List<ChartCluster> chartClusters = new ArrayList<>();
    for (Cluster<ArticleVertex, Similarity> cluster: graph.getClusters()) {
      chartClusters.add(clusterToChartCluster(cluster));
    }
    return chartClusters;
  }

  /**
   * Gets the list of edges between every article and every other article in
   * a set of article vertices.
   * @param pulledArticles the set of article vertices
   * @param params clustering parameters
   * @return the list of edges between every article and every other article
   * @throws SQLException if error accessing database (should not be thrown)
   */
  private List<Similarity> getEdges(Set<ArticleVertex> pulledArticles,
                                   ClusterParameters params) throws SQLException {
    Map<ArticleWord, Double> vocabMap = db.getDataRead().getVocabFreq();
    Map<Entity, Double> entityMap = db.getDataRead().getEntityFreq();
    int maxCount = db.getDataRead().getMaxVocabCount();
    TextCorpus<Entity, ArticleVertex> entityCorpus =
        new TextCorpus<>(entityMap, maxCount, 0);
    TextCorpus<ArticleWord, ArticleVertex> wordCorpus =
        new TextCorpus<>(vocabMap, maxCount, 1);
    TextCorpus<ArticleWord, ArticleVertex> titleCorpus =
        new TextCorpus<>(vocabMap, maxCount, 2);
    ArrayList<Similarity> edges = new ArrayList<>();
    System.out.println("Article Size: " + pulledArticles.size());
    for (ArticleVertex a1 : pulledArticles) {
      for (ArticleVertex a2 : pulledArticles) {
        if (a1.getId() < a2.getId()) {
          Similarity tempEdge = new Similarity(a1, a2, wordCorpus, entityCorpus, titleCorpus,
              params.getTextWeight(),
              params.getEntityWeight(), params.getTitleWeight());
          a1.addEdge(tempEdge);
          a2.addEdge(tempEdge);
          edges.add(tempEdge);
        }
      }
    }
    // sort edges
    edges.sort(Comparator.comparingDouble(Similarity::getDistance));
    int size = edges.size();
    for (int i = 0; i < size; i++) {
      if (i < pulledArticles.size() || i > (size - NUM_EDGES_TO_PRINT)) {
        Similarity tempEdge = edges.get(i);
        System.out.println(tempEdge.getSource().getArticle().getTitle() + " - "
            + tempEdge.getDest().getArticle().getTitle() + " : " + edges.get(i).getDistance());
      }
    }

    return edges;
  }

  /**
   * Turns a cluster into a simpler representation (ChartCluster) to be sent
   * to the client.
   * @param complexCluster a cluster
   * @return a simplified representation of that cluster
   */
  private ChartCluster clusterToChartCluster(Cluster<ArticleVertex, Similarity> complexCluster) {
    List<Article> simpleArticles = new ArrayList<>();
    for (ArticleVertex complexArticle: complexCluster.getArticles()) {
      simpleArticles.add(complexArticle.getArticle());
    }
    return new ChartCluster(complexCluster.getId(),
        complexCluster.getHeadNode().getArticle().getTitle(),
        complexCluster.getSize(), complexCluster.getAvgRadius(), simpleArticles);
  }

  /**
   * Main method of NewsClusterer used to carry out manual clustering.
   * @param args user input
   * @throws SQLException if thrown
   * @throws ClassNotFoundException if thrown
   */
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    ClusterParameters params = new ClusterParameters(24, true);
    NewsClusterer clusterer = new NewsClusterer(new NewsData("data/final_data.db"));
    clusterer.clusterArticles(params);
  }
}
