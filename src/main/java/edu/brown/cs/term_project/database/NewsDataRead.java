package edu.brown.cs.term_project.database;

import edu.brown.cs.term_project.api.response.ChartCluster;
import edu.brown.cs.term_project.bubble.Article;
import edu.brown.cs.term_project.bubble.ArticleVertex;
import edu.brown.cs.term_project.bubble.ArticleWord;
import edu.brown.cs.term_project.bubble.Entity;
import edu.brown.cs.term_project.similarity.IWord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NewsDataRead {
  private Connection conn;

  /**
   * Constructor.
   * @param conn a database connection
   */
  public NewsDataRead(Connection conn) {
    this.conn = conn;
  }

  /**
   * Gets the set of sources in the database.
   *
   * @return the set of sources
   * @throws SQLException if error occurs
   */
  public Set<String> getSources() throws SQLException {
    Set<String> sources = new HashSet<String>();
    PreparedStatement prep = conn.prepareStatement("SELECT DISTINCT source from articles");
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      sources.add(rs.getString(1));
    }
    return sources;
  }

  public List<Article> getArticlesFromCluster(int clusterId) throws SQLException {
    // build sql statement
    String statement = "SELECT title, url, date_published, source, id FROM articles a\n"
        + "JOIN article_cluster c ON c.article_id = a.id\n"
        + "WHERE c.cluster_id = ? ORDER BY date_published desc";
    PreparedStatement prep = conn.prepareStatement(statement);
    prep.setInt(1, clusterId);
    ResultSet rs = prep.executeQuery();
    List<Article> articles = new ArrayList<>();
    while (rs.next()) {
      String title = rs.getString(1);
      String url = rs.getString(2);
      String datePublished = rs.getString(3);
      String source = rs.getString(4);
      int id = rs.getInt(5);
      Article a = new Article(id, source, title, url, datePublished);
      articles.add(a);
    }
    return articles;
  }

  public Set<ArticleVertex> getArticleVerticesFromCluster(int clusterId) throws SQLException {
    // build sql statement
    String statement = "SELECT id, source, title, url, date_published, text FROM articles a\n"
        + "JOIN article_cluster c ON c.article_id = a.id\n"
        + "WHERE c.cluster_id = ?";
    PreparedStatement prep = conn.prepareStatement(statement);
    prep.setInt(1, clusterId);
    return getArticleVerticesHelper(prep);
  }

  public Set<ArticleVertex> getArticleVerticesFromArticleIds(String serializedIdSet) throws SQLException {
    // build sql statement
    String statement = "SELECT id, source, title, url, date_published, text FROM articles "
        + "WHERE id IN " + serializedIdSet;
    PreparedStatement prep = conn.prepareStatement(statement);
    return getArticleVerticesHelper(prep);
  }

  public Set<ArticleVertex> getArticleVertices(int maxNumArticles) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT id, source, title, url, date_published, "
        + "text FROM articles "
        + "WHERE date_pulled >= DATETIME('now', '-24 hours') AND date_pulled < DATETIME('now') "
        + "ORDER BY date_published "
        + "LIMIT (?);"
    );
    prep.setInt(1, maxNumArticles);
    return getArticleVerticesHelper(prep);
  }

  public Set<ArticleVertex> getArticlePair(int id1, int id2) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT id, source, title, url, date_published, "
        + "text "
        + "FROM articles WHERE id = ? OR id = ?"
    );
    prep.setInt(1, id1);
    prep.setInt(2, id2);
    return getArticleVerticesHelper(prep);
  }

  private Set<ArticleVertex> getArticleVerticesHelper(PreparedStatement prep) throws SQLException {
    try (ResultSet rs = prep.executeQuery()) {
      Set<Article> articles = new HashSet<>();
      Map<Integer, String> articleText = new HashMap<>();
      while (rs.next()) {
        articles.add(new Article(rs.getInt(1), rs.getString(2),
            rs.getString(3), rs.getString(4), rs.getString(5)));
        articleText.put(rs.getInt(1), rs.getString(6));
      }
      return createArticleVertices(articles, articleText);
    }
  }

  private Set<ArticleVertex> createArticleVertices(
      Set<Article> articles, Map<Integer, String> articleText) throws SQLException {
    Set<ArticleVertex> articleVertices = new HashSet<>();
    for (Article article : articles) {
      articleVertices.add(new ArticleVertex(article, articleText.get(article.getId()),
          this.getArticleEntityFreq(article.getId())));
    }
    return articleVertices;
  }

  public Map<ArticleWord, Double> getVocabFreq() throws SQLException {
    PreparedStatement prep = conn.prepareStatement("Select word, count FROM vocab;");
    ResultSet rs = prep.executeQuery();
    Map<ArticleWord, Double> words = new HashMap<>();
    while (rs.next()) {
      words.put(new ArticleWord(rs.getString(1)), rs.getDouble(2));
    }
    return words;
  }

  public Map<Entity, Double> getEntityFreq() throws SQLException {
    PreparedStatement prep = conn.prepareStatement("Select entity, class, count FROM entity;");
    ResultSet rs = prep.executeQuery();
    Map<Entity, Double> words = new HashMap<>();
    while (rs.next()) {
      words.put(new Entity(rs.getString(1), rs.getString(2)),
          rs.getDouble(3));
    }
    return words;
  }

  public Map<IWord, Double> getArticleEntityFreq(Integer articleId) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT entity_class, entity_entity, count\n"
        + "FROM article_entity\n"
        + "WHERE article_id = ?;");
    prep.setInt(1, articleId);
    ResultSet rs = prep.executeQuery();
    Map<IWord, Double> articleEntityFreq = new HashMap<>();
    while (rs.next()) {
      articleEntityFreq.put(new Entity(rs.getString(2), rs.getString(1)),
          rs.getDouble(3));
    }
    return articleEntityFreq;
  }

  public Integer getMaxVocabCount() throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT MAX(count) FROM vocab;");
    ResultSet rs = prep.executeQuery();
    rs.next();
    return rs.getInt(1);
  }

  // TODO: Don't test.
  /**
   * Gets the clusters for a given day. This will be passed to the front end.
   *
   * @param date the date to search for (java.util.Date)
   * @return a set of cluster objects
   * @throws SQLException only thrown if the database is malformed
   */
  public List<ChartCluster> getClusters(String date) throws SQLException {
    String query =
        "SELECT id, title, size FROM clusters WHERE day = ? ORDER BY size desc";
    //        String query = "SELECT id, title, size FROM clusters";
    PreparedStatement prep = conn.prepareStatement(query);
    prep.setString(1, date);
    ResultSet rs = prep.executeQuery();
    List<ChartCluster> clusters = new ArrayList<>();
    while (rs.next()) {
      int clusterId = rs.getInt(1);
      String headline = rs.getString(2);
      int size = rs.getInt(3);
      List<Article> articles = new ArrayList<>();
//      articles = getArticlesFromCluster(clusterId);
      ChartCluster cluster = new ChartCluster(clusterId, headline, size, articles);
      clusters.add(cluster);
    }
    return clusters;
  }

  // TODO: Don't test
  /**
   * Gets the clusters for a given day. This will be passed to the front end.
   *
   * @param clusterId cluster to find
   * @return double
   * @throws SQLException only thrown if the database is malformed
   */
  public double getClusterMeanRadiusPercentile(Integer clusterId) throws SQLException {
    final double zeroAdj = 0.001;
    String query = "SELECT MAX(avg_radius), MIN(avg_radius) FROM clusters;";
    PreparedStatement prep = conn.prepareStatement(query);
    ResultSet rs = prep.executeQuery();
    double max = 0, min = 0;
    if (rs.next()) {
      max = rs.getDouble(1);
      min = rs.getDouble(2);
    }
    String query2 = "SELECT avg_radius FROM clusters WHERE id = ?;";
    PreparedStatement prep2 = conn.prepareStatement(query2);
    prep2.setInt(1, clusterId);
    ResultSet rs2 = prep2.executeQuery();
    double radius = 0;
    if (rs2.next()) {
      radius = rs2.getDouble(1);
    }
    return (radius - min) / (max - min + zeroAdj);
  }


  public static void main(String[] args) throws SQLException, ClassNotFoundException {
//    NewsData db = new NewsData("data/bubble.db");
//    Set<String> ids = new HashSet<>();
//    ids.add("1");
//    ids.add("2");
//    ids.add("3");
//    Set<ArticleVertex> articles = db.getDataRead().getArticleVerticesFromArticleIds(ids);
//    System.out.println(articles);
  }
}
