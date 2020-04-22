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

  // Kshitij
  public void insertArticleEntity(Article article, Entity entity) {
  }

  public void updateWordFreq(Map<String, Integer> words) {
  }

  // Ben/John
  public Set<Article> getAruticles(Integer hours) {
    return null;
  }

  public HashMap<Vocab, Double> getVocabFreq() {
    return null;
  }

  public HashMap<Entity, Double> getEntityFreq() {
    return null;
  }

  public HashMap<Entity, Double> getArticleEntityFreq(Integer articleId) {
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
