package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Database.Database;
import edu.brown.cs.term_project.Graph.Cluster;
import org.joda.time.DateTime;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.sql.Date;
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

  /**
   * Inserts an article and the entities of that article given by frequency map.
   * @param article the article to insert
   * @param entityFrequencyMap the frequency map for that article
   * @throws SQLException if there was a database error
   */
  public void insertArticleAndEntities(ArticleJSON article, HashMap<Entity, Integer> entityFrequencyMap) throws SQLException {
    int articleId = insertArticle(article);
    insertEntities(articleId, entityFrequencyMap);
  }

  /**
   * Inserts an article into the database.
   * @param article the article to be inserted
   * @return the primary key of the article in the database once inserted
   */
  private int insertArticle(ArticleJSON article) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
        "INSERT into articles (title, url, date_published, date_pulled,"
            + " text) VALUES (?, ?, ?, ?, ?);"
    );
    prep.setString(1, article.getTitle());
    prep.setString(2, article.getUrl());
//    prep.setDate(3, article.getTimePublished());
//    prep.setDate(4, new Date());
    prep.setString(5, article.getContent());
    prep.execute();
    prep.close();
    // get id of article
    prep = conn.prepareStatement("SELECT last_insert_rowid();");
    ResultSet rs = prep.executeQuery();
    int lastInsertedId = -1;
    while (rs.next()) {
      lastInsertedId = rs.getInt(1);
    }
    if (lastInsertedId == -1) {
      throw new RuntimeException("Somehow article was not inserted?");
    }
    return lastInsertedId;
  }

  /**
   * Inserts every entity in the frequency map with the given article id.
   * @param articleId the id in the database of the article to which entities belong
   * @param entityFrequencyMap a map of number of times each entity was found in
   *                           the article whose id is articleId
   */
  private void insertEntities(int articleId, HashMap<Entity, Integer> entityFrequencyMap) throws SQLException {
    for (Entity entity: entityFrequencyMap.keySet()) {
      insertEntity(articleId, entity, entityFrequencyMap.get(entity));
    }
  }

  /**
   * Inserts an entity into both the entity and article_entity tables, incrementing
   * the overall number of occurrences in entity by 1 and inserting the per article
   * frequency in article_entity.
   * @param articleId the id of the article in which the entity was found
   * @param entity the entity to be inserted
   * @param numOccurrences the number of occurrences of that entity in the article of articleId
   * @throws SQLException if there is an error inserting/updating into the database
   */
  private void insertEntity(int articleId, Entity entity, int numOccurrences) throws SQLException {
    // first insert into entity
    PreparedStatement prep = conn.prepareStatement(
        "BEGIN TRANSACTION;"
            + "    INSERT OR IGNORE INTO entity (class, entity, count)"
            + "    VALUES ((?), (?), 0);"
            + "    UPDATE entity SET count = count + 1 WHERE entity = (?) AND class = (?);"
            + "    INSERT INTO article_entity (article_id, entity_class, entity_entity, count)"
            + "    VALUES ((?), (?), (?), (?));"
            + "COMMIT;"
    );
    prep.setString(1, entity.getClassType());
    prep.setString(2, entity.getWord());
    prep.setString(3, entity.getWord());
    prep.setString(4, entity.getClassType());
    prep.setInt(5, articleId);
    prep.setString(6, entity.getClassType());
    prep.setString(7, entity.getWord());
    prep.setInt(8, numOccurrences);
    prep.execute();
    prep.close();
  }


  /**
   * Updates the vocab count in the database by incrementing the count column
   * of the vocab table by the number of articles that a word appeared in
   * in the batch of articles being added.
   * @param vocabOccurrenceMap a map of word to number of occurrences
   * @throws SQLException if an SQL exception occurred
   */
  public void updateVocabCounts(Map<String, Integer> vocabOccurrenceMap) throws SQLException {
    for (String word: vocabOccurrenceMap.keySet()) {
      // if word is not in vocab table, insert then update, else ignore then update
      PreparedStatement prep = conn.prepareStatement(
          "BEGIN TRANSACTION;"
              + "    INSERT OR IGNORE INTO vocab (word, count)"
              + "    VALUES ((?), 0);"
              + "    UPDATE vocab SET count = count + 4 WHERE word = (?);"
              + "COMMIT;"
      );
      prep.setString(1, word);
      prep.setInt(2, vocabOccurrenceMap.get(word));
      prep.execute();
      prep.close();
    }
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
