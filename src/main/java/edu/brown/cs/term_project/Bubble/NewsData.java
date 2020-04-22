package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Database.Database;
import edu.brown.cs.term_project.Graph.Cluster;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class NewsData extends Database {
  private Connection conn = null;

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

  // TODO: Implement Database functions



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
  private void insertEntities(int articleKey, HashMap<Entity, Double> entityFrequencyMap) {}


  public void updateWordFreq(Map<String, Integer> words) {
  }

  // Ben/John

  public Set<Article> getArticles(Integer hours, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<Entity, ArticleVertex> entityCorpus) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT id, title, url, date_published, text\n"
        + "FROM articles\n"
        + "WHERE date_pulled >= date('now', '-? hours') AND date_pulled < date('now');");

    prep.setInt(1, hours);
    ResultSet rs = prep.executeQuery();

    Set<Article> articles = new HashSet<>();
    while (rs.next()) {
      //Article article = new Article(rs.getInt(1), );
      //ways.add(new Way(loc, endLoc, rs.getString(3)));
    }
    rs.close();
    prep.close();
    return null;
  }

  public HashMap<ArticleWord, Double> getVocabFreq() throws SQLException {
    return null;
  }

  public HashMap<Entity, Double> getEntityFreq() throws SQLException {
    return null;
  }

  public HashMap<Entity, Double> getArticleEntityFreq(Integer articleId) throws SQLException {
    return null;
  }

  public Integer getMaxVocabCount() throws SQLException {
    return null;
  }

  public static void insertClusters(Set<Cluster> clusters) throws SQLException {
  }

  // Ian
  public Set<Cluster> getClusters(Date date) {
    return null;
  }

}
