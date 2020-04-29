package edu.brown.cs.term_project.handlers;

import edu.brown.cs.term_project.Bubble.ArticleVertex;
import edu.brown.cs.term_project.Bubble.ArticleWord;
import edu.brown.cs.term_project.Bubble.Entity;
import edu.brown.cs.term_project.Bubble.NewsData;
import edu.brown.cs.term_project.Bubble.Similarity;
import edu.brown.cs.term_project.Graph.Graph;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class NewsClusterer {
  public static void clusterArticles(NewsData db) throws SQLException {
    Set<ArticleVertex> pulledArticles = db.getArticleVertices(24);
    Map<ArticleWord, Double> vocabMap = db.getVocabFreq();
    Map<Entity, Double> entityMap = db.getEntityFreq();
    int maxCount = db.getMaxVocabCount();
    TextCorpus<ArticleWord, ArticleVertex> wordCorpus =
        new TextCorpus<>(vocabMap, maxCount);
    TextCorpus<Entity, ArticleVertex> entityCorpus =
        new TextCorpus<>(entityMap, maxCount);
    ArrayList<Similarity> edges = new ArrayList<>();
    System.out.println("Article Size: " + pulledArticles.size());
    for (ArticleVertex a1: pulledArticles) {
      for (ArticleVertex a2: pulledArticles) {
        if (a1.getId() < a2.getId()) {

          Similarity tempEdge = new Similarity(a1, a2, wordCorpus, entityCorpus);
          a1.addEdge(tempEdge);
          a2.addEdge(tempEdge);
          edges.add(tempEdge);
          System.out.println(a1.getId() + " - " + a2.getId() + " : " + tempEdge.getDistance());
        }
      }
    }

    Graph<ArticleVertex, Similarity> graph = new Graph(pulledArticles, edges);
    graph.runClusters(1);
    db.insertClusters(graph.getClusters());
  }

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    clusterArticles(new NewsData("data/backloaded.db"));
  }
}
