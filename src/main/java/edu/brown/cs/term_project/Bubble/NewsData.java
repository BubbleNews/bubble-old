package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Database.Database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

public final class NewsData extends Database {

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
//  public Set<Article> getArticles(Integer hours) {}
//
//  public HashMap<Vocab, Double> getVocabFreq() {}
//
//  public HashMap<Entity, Double> getEntityFreq() {}
//
//  public HashMap<Vocab, Double> getArticleVocabFreq() {}
//
//  public HashMap<Entity, Double> getArticleEntityFreq() {}
//
//  //[updates total vocab/entity]
//  public void insertArticles(Set<Article> articles) {}
//
//  public void insertEdges(Set<Similarity> edges) {}
//
//  public void insertCluster(Set<Cluster> clusters) {}

}
