package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Database.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
  public Set<Article> getArticles(Integer hours) {
    return null;
  }

  public HashMap<Vocab, Double> getVocabFreq() {
    return null;
  }

  public HashMap<Entity, Double> getEntityFreq() {
    return null;
  }

  public HashMap<Entity, Double> getArticleEntityFreq() {
    return null;
  }

  public Integer getMaxVocabCount() {
    return null;
  }

  public void insertCluster(Set<Cluster> clusters) {
  }

  // Ian
  public Set<Cluster> getClusters() {
    return null;
  }

}
