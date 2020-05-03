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
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class NewsClusterer {
  public static void clusterArticles(NewsData db) throws SQLException {
    final double textWeight = 1;
    final double entityWeight = 1;
    final double titleWeight = 1;
    Set<ArticleVertex> pulledArticles = db.getArticleVertices(0);
    Map<ArticleWord, Double> vocabMap = db.getVocabFreq();
    Map<Entity, Double> entityMap = db.getEntityFreq();
    int maxCount = db.getMaxVocabCount();
    TextCorpus<Entity, ArticleVertex> entityCorpus =
        new TextCorpus<>(entityMap, maxCount, 0);
    TextCorpus<ArticleWord, ArticleVertex> wordCorpus =
        new TextCorpus<>(vocabMap, maxCount, 1);
    TextCorpus<ArticleWord, ArticleVertex> titleCorpus =
        new TextCorpus<>(vocabMap, maxCount, 2);
    ArrayList<Similarity> edges = new ArrayList<>();
    System.out.println("Article Size: " + pulledArticles.size());
    for (ArticleVertex a1: pulledArticles) {
      for (ArticleVertex a2: pulledArticles) {
        if (a1.getId() < a2.getId()) {

          Similarity tempEdge = new Similarity(a1, a2, wordCorpus, entityCorpus, titleCorpus,
              textWeight,
              entityWeight, titleWeight);
          a1.addEdge(tempEdge);
          a2.addEdge(tempEdge);
          edges.add(tempEdge);
        }
      }
    }

    edges.sort(Comparator.comparingDouble(Similarity::getDistance));
    int size = edges.size();
    for (int i = 0; i < size; i++) {
      if (i < pulledArticles.size() || i > (size - 25)) {
        Similarity tempEdge = edges.get(i);
        System.out.println(tempEdge.getSource().getArticle().getTitle() + " - "
            + tempEdge.getDest().getArticle().getTitle() + " : " + edges.get(i).getDistance());
      }
    }

    Graph<ArticleVertex, Similarity> graph = new Graph<>(pulledArticles, edges);
    graph.runClusters(1);
    db.insertClusters(graph.getClusters());
  }

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    clusterArticles(new NewsData("data/backloaded.db"));
  }
}
