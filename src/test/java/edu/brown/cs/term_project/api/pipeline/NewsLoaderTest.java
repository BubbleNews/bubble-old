package edu.brown.cs.term_project.api.pipeline;

import edu.brown.cs.term_project.bubble.Article;
import edu.brown.cs.term_project.database.NewsData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NewsLoaderTest {
  private static NewsData db;

  @Before
  public void setUp() throws SQLException, ClassNotFoundException {
    db = new NewsData("data/loader_test.db");
  }

  @After
  public void tearDown() throws SQLException {
    db.getDataWrite().deleteAllData();
    db = null;
  }

  @Test
  public void testProcessJSONArticles() throws SQLException {
    List<Article> jsonArticles = new ArrayList<>();
    Article a = new Article("cnn", new String[]{"author"}, "article1",
        "description", "url", "time", "content trump article coronavirus");
    Article b = new Article("fox", new String[]{"author"}, "article2",
        "description", "url", "time", "coronavirus coronavirus pandemic");
    jsonArticles.add(a);
    jsonArticles.add(b);
    NewsLoader loader = new NewsLoader(db, "n/a");
    loader.processJSONArticles(jsonArticles);
    // tests
    assertEquals(2, db.getDataRead().getMaxVocabCount());
    Set<String> sourcesInDb = db.getDataRead().getSources();
    Set<String> sources = new HashSet<>();
    sources.add("cnn");
    sources.add("fox");
    assertTrue(sources.containsAll(sourcesInDb));
  }

  @Test
  public void testAddParameters() {
    NewsLoader loader = new NewsLoader(db, "n/a");
    String baseUrl = "baseUrl";
    HashMap<String, String> params = new HashMap<>();
    // no params
    assertEquals(baseUrl, loader.addParameters(baseUrl, params));
    params.put("id", "1");
    params.put("name", "leroy");
    // params
    assertEquals(baseUrl + "?name=leroy&id=1",
        loader.addParameters(baseUrl, params));
  }
}