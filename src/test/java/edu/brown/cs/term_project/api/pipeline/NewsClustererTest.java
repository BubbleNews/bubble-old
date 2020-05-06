package edu.brown.cs.term_project.api.pipeline;

import edu.brown.cs.term_project.api.response.ChartCluster;
import edu.brown.cs.term_project.clustering.ClusterParameters;
import edu.brown.cs.term_project.database.NewsData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

public class NewsClustererTest {
  private static NewsData db;
  private static NewsClusterer clusterer;

  @Before
  public void setUp() throws SQLException, ClassNotFoundException {
    db = new NewsData("data/loader_test.db");
    clusterer = new NewsClusterer(db);
  }

  @After
  public void tearDown() throws SQLException {
    db.getDataWrite().deleteAllData();
    db = null;
    clusterer = null;
  }

  @Test
  public void testClusterArticles() throws SQLException {
    ClusterParameters params = new ClusterParameters(0, false);
    List<ChartCluster> clusters = clusterer.clusterArticles(params);
    assertEquals(0, clusters.size());
  }
}
