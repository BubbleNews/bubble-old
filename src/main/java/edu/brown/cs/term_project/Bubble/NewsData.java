package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Database.Database;
import edu.brown.cs.term_project.Graph.Cluster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.util.Calendar;

public final class NewsData extends Database {
  private static Connection conn = null;

  /**
   * A constructor to setup connection to SQLDatabase. Sets up for querying the sql database and
   * error checks the file.
   *
   * @param filename the filename that the user wants to query from
   * @throws SQLException           if sql error
   * @throws ClassNotFoundException if issue setting up database connection
   */
  public NewsData(String filename) throws SQLException, ClassNotFoundException {
    super(filename);
  }




  public void insertArticleAndEntities(Article article) {
    // CALL The three methods below
  }

  /**
   * Inserts an article into the database.
   * @param article the article to be inserted
   * @return the primary key of the article in the database once inserted
   */
  private int insertArticle(Article article) {
    return 0;
  }

  /**
   *
   * @param articleKey
   * @param entityFrequencyMap
   */
  private void insertEntities(int articleKey, HashMap<Entity, Double> entityFrequencyMap) {

  }


  public void updateWordFreq(Map<String, Integer> words) {

  }

  // Ben/John

  public Set<ArticleVertex> getArticleVertices(Integer hours) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT id, title, date_published, author, "
        + "url, text "
        + "FROM articles "
        + "WHERE date_pulled >= date('now', '-? hours') AND date_pulled < date('now');");
    prep.setInt(1, hours);
    ResultSet rs = prep.executeQuery();
    Set<Article> articles = new HashSet<>();
    Map<Integer, String> articleText = new HashMap<>();
    while (rs.next()) {
      articles.add(new Article(rs.getInt(1), rs.getString(2),
          rs.getString(3), rs.getString(4), rs.getString(5)));
      articleText.put(rs.getInt(1), rs.getString(6));
    }
    rs.close();
    prep.close();
    Set<ArticleVertex> articleVertices = new HashSet<>();
    for (Article article: articles) {
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

  public Map<Entity, Double> getArticleEntityFreq(Integer articleId) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT entity_class, entity_entity, count\n"
        + "FROM article_entity\n"
        + "WHERE article_id = ?;");
    prep.setInt(1, articleId);
    ResultSet rs = prep.executeQuery();
    Map<Entity, Double> articleEntityFreq = new HashMap<>();
    while (rs.next()) {
      articleEntityFreq.put(new Entity(rs.getString(1), rs.getString(2)),
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

  public static void insertClusters(Set<Cluster<ArticleVertex, Similarity>> clusters) throws SQLException {
    Calendar rightNow = Calendar.getInstance();
    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
    Boolean finalCluster = (hour == 23);
    for (Cluster<ArticleVertex, Similarity> c: clusters) {
      insertCluster(c, hour, finalCluster);
      int clusterId = getClusterId(c.getHeadNode().getId());
      for (ArticleVertex a: c.getNodes()) {
        updateArticle(clusterId, a.getId(), finalCluster);
      }
    }

  }

  public static void insertCluster(Cluster<ArticleVertex, Similarity> c, int hour, boolean finalCluster) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("INSERT INTO clusters (head, title, size, day, hour, avg_connections, avg_radius, std, intermediate_cluster)\n"
        + "VALUES (?, ?, ?, DATE('now'), ?, ?, ?, ?, ?);");
    prep.setInt(1, c.getHeadNode().getId());
    prep.setString(2, c.getHeadNode().getArticle().getTitle());
    prep.setInt(3, c.getSize());
    prep.setInt(4, hour);
    prep.setDouble(5, c.getAvgConnections());
    prep.setDouble(6, c.getAvgRadius());
    prep.setDouble(7, c.getStd());
    prep.setBoolean(8, finalCluster);
    prep.execute();
    prep.close();
  }

  public static int getClusterId(int head) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT id\n"
        + "FROM clusters \n"
        + "WHERE head = ?;");
    prep.setInt(1, head);
    ResultSet rs = prep.executeQuery();
    if (rs.next()) {
      return rs.getInt(1);
    }
    throw new NullPointerException();
  }

  public static void updateArticle(int clusterId, int articleId, boolean finalCluster) throws SQLException {
    PreparedStatement prep;
    if (finalCluster) {
      prep = conn.prepareStatement("UPDATE articles\n"
          + "SET final_cluster_id = ?\n"
          + "WHERE id = ?;");
      prep.setInt(1, clusterId);
      prep.setInt(2, articleId);
    } else {
      prep = conn.prepareStatement("UPDATE articles\n"
          + "SET temp_cluster_id = ?\n"
          + "WHERE id = ?;");
      prep.setInt(1, clusterId);
      prep.setInt(2, articleId);
    }
    prep.execute();
    prep.close();
  }

  // Ian
  public Set<Cluster> getClusters(Date date) {
    return null;
  }

}
