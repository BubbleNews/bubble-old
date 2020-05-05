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

public class NewsClusterer {
  private NewsData db;

  public NewsClusterer(NewsData db) {
    this.db = db;
  }

  public List<ChartCluster> clusterArticles(ClusterParameters params) throws SQLException {
    Set<ArticleVertex> pulledArticles = db.getDataRead().getArticleVertices(params.getNumArticles());
    List<Similarity> edges = getEdges(pulledArticles, params);
    // sort edges
    edges.sort(Comparator.comparingDouble(Similarity::getDistance));
    // make graph from vertices and cluster
    Graph<ArticleVertex, Similarity> graph = new Graph<>(pulledArticles, edges);
    // set threshold according to cluster params if params are for reclustering
    if (!params.getDoInsert()) {
      graph.setThreshold(params.getPercentageEdgesToConsider());
    }
    graph.runClusters(params.getClusterMethod());
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

  public List<Similarity> getEdges(Set<ArticleVertex> pulledArticles,
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
    return edges;
  }

  private ChartCluster clusterToChartCluster(Cluster<ArticleVertex, Similarity> complexCluster) {
    List<Article> simpleArticles = new ArrayList<>();
    for (ArticleVertex complexArticle: complexCluster.getArticles()) {
      simpleArticles.add(complexArticle.getArticle());
    }
    return new ChartCluster(complexCluster.getId(),
        complexCluster.getHeadNode().getArticle().getTitle(),
        complexCluster.getSize(), simpleArticles);
  }

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    ClusterParameters params = new ClusterParameters();
    NewsClusterer clusterer = new NewsClusterer(new NewsData("data/mock_data.db"));
    clusterer.clusterArticles(params);
  }
}
