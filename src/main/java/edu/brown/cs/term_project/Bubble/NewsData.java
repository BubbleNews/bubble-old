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
    PreparedStatement prep = conn.prepareStatement("Select entity.entity, entity.class, "
        + "article_entity.count "
        + "FROM entity "
        + "JOIN article_entity ON article_entity.entity = entity.id "
        + "WHERE article_entity.article = ?;");
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

  public static void insertClusters(Set<Cluster> clusters) throws SQLException {
  }

  // Ian
  public Set<Cluster> getClusters(Date date) {
    return null;
  }

}
