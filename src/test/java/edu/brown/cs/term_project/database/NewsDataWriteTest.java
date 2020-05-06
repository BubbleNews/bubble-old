package edu.brown.cs.term_project.database;

import edu.brown.cs.term_project.api.response.ChartCluster;
import edu.brown.cs.term_project.bubble.Article;
import edu.brown.cs.term_project.bubble.ArticleVertex;
import edu.brown.cs.term_project.bubble.Entity;
import edu.brown.cs.term_project.bubble.Similarity;
import edu.brown.cs.term_project.clustering.Cluster;
import edu.brown.cs.term_project.similarity.IWord;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class NewsDataWriteTest {

  private static final double DELTA = 0.00005;
  private static NewsData db;

  @BeforeClass
  public static void setUpOnce() throws SQLException, ClassNotFoundException {
    db = new NewsData("data/news_data_read_test.db");
  }

  @AfterClass
  public static void tearDownOnce() throws SQLException {
    db.getDataWrite().deleteAllData();
    db = null;
  }

  @After
  public void tearDown() throws SQLException {
    db.getDataWrite().deleteAllData();
  }

  @Test
  public void testInsertArticlesAndEntities() throws SQLException {
    Article a = new Article("cnn", new String[]{"author"}, "title",
        "description", "url", "time", "content");
    HashMap<Entity, Integer> entityMap = new HashMap<>();
    Entity trump = new Entity("Trump", "0");
    Entity biden = new Entity("Biden", "0");
    entityMap.put(trump, 3);
    entityMap.put(biden, 1);
    db.getDataWrite().insertArticleAndEntities(a, entityMap);
    Article fromDb = db.getDataRead().getArticleByTitle("title");
    assertEquals(a.getSourceName(), fromDb.getSourceName());
  }

  @Test
  public void testUpdateVocabCount() throws SQLException {
    HashMap<String, Integer> occurrenceMap = new HashMap<>();
    occurrenceMap.put("dog", 3);
    occurrenceMap.put("cat", 1);
    occurrenceMap.put("fish", 8);
    db.getDataWrite().updateVocabCounts(occurrenceMap);
    assertEquals(8, db.getDataRead().getMaxVocabCount());
  }

  @Test
  public void testInsertClusters() throws SQLException {
    Set<Cluster<ArticleVertex, Similarity>> clusterSet = new HashSet<>();
    Article a = new Article("cnn", new String[]{"author"}, "title",
        "description", "url", "time", "content");
    ArticleVertex av = new ArticleVertex(a, "text", new HashMap<IWord, Double>());
    Set<ArticleVertex> avSet = new HashSet<>();
    avSet.add(av);
    Cluster<ArticleVertex, Similarity> c =
        new Cluster<ArticleVertex, Similarity>(1, av, avSet);
    clusterSet.add(c);
    db.getDataWrite().insertClusters(clusterSet);
    List<ChartCluster> fromDb = db.getDataRead().getNewestClusters();
    assertEquals("title", fromDb.get(0).getHeadline());
  }
}
