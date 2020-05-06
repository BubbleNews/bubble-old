package edu.brown.cs.term_project.database;

import edu.brown.cs.term_project.bubble.Article;
import edu.brown.cs.term_project.bubble.ArticleVertex;
import edu.brown.cs.term_project.bubble.Entity;
import edu.brown.cs.term_project.bubble.Similarity;
import edu.brown.cs.term_project.clustering.Cluster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class containing all methods that write to the News Database.
 */
public class NewsDataWrite {
  private Connection conn;

  /**
   * Constructor.
   * @param conn a database connection
   */
  public NewsDataWrite(Connection conn) {
    this.conn = conn;
  }

  /**
   * Inserts an article and the entities of that article given by frequency map.
   *
   * @param article            the article to insert
   * @param entityFrequencyMap the frequency map for that article
   * @throws SQLException if there was a database error
   */
  public void insertArticleAndEntities(
      Article article, HashMap<Entity, Integer> entityFrequencyMap) throws SQLException {
    int articleId = insertArticle(article);
    insertEntities(articleId, entityFrequencyMap);
  }

  /**
   * Inserts an article into the database.
   *
   * @param article the article to be inserted
   * @return the primary key of the article in the database once inserted
   */
  private int insertArticle(Article article) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
        "INSERT into articles (source, title, url, date_published, date_pulled,"
            + " text) SELECT ?, ?, ?, ?, DATETIME('now'), ?"
            + "WHERE NOT EXISTS (SELECT 1 FROM articles WHERE title = ?);"
    );
    prep.setString(1, article.getSourceName());
    prep.setString(2, article.getTitle().replace("\'", ""));
    prep.setString(3, article.getUrl());

    String datePublished = article.getTimePublished();
    datePublished = datePublished
        .replace('T', ' ')
        .substring(0, Math.min(19, datePublished.length()));
    prep.setString(4, datePublished);
    prep.setString(5, article.getContent());
    prep.setString(6, article.getTitle().replace("\'", ""));
    prep.execute();
    prep.close();
    // get id of article
    prep = conn.prepareStatement("SELECT last_insert_rowid()\n"
        + "FROM articles\n"
        + "LIMIT 1;");
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
   *
   * @param articleId          the id in the database of the article to which entities belong
   * @param entityFrequencyMap a map of number of times each entity was found in
   *                           the article whose id is articleId
   */
  private void insertEntities(
      int articleId, HashMap<Entity, Integer> entityFrequencyMap) throws SQLException {
    // loop through all entities and insert
    for (Entity entity : entityFrequencyMap.keySet()) {
        insertEntity(articleId, entity, entityFrequencyMap.get(entity));
    }
  }

  /**
   * Inserts an entity into both the entity and article_entity tables, incrementing
   * the overall number of occurrences in entity by 1 and inserting the per article
   * frequency in article_entity.
   *
   * @param articleId      the id of the article in which the entity was found
   * @param entity         the entity to be inserted
   * @param numOccurrences the number of occurrences of that entity in the article of articleId
   * @throws SQLException if there is an error inserting/updating into the database
   */
  private void insertEntity(int articleId, Entity entity, int numOccurrences) throws SQLException {
    // insert literal entity word into the entity table if it hasn't been seen before
    PreparedStatement prep = conn.prepareStatement("INSERT OR IGNORE INTO entity (class, entity, "
        + "count) VALUES (?, ?, 0);");
    prep.setString(1, entity.getClassType());
    prep.setString(2, entity.getWord());
    prep.execute();
    prep.close();

    // add 1 to document frequency (always guaranteed that this article hasn't been seen yet)
    PreparedStatement prep2 = conn.prepareStatement("UPDATE entity SET count = count + 1 WHERE "
        + "entity = ? AND class = ?;");
    prep2.setString(1, entity.getWord());
    prep2.setString(2, entity.getClassType());
    prep2.execute();
    prep2.close();

    // adds pairing between article and count of entity
    PreparedStatement prep3 = conn.prepareStatement("INSERT INTO article_entity (article_id, "
        + "entity_class, entity_entity, count) VALUES (?, ?, ?, ?);");
    prep3.setInt(1, articleId);
    prep3.setString(2, entity.getClassType());
    prep3.setString(3, entity.getWord());
    prep3.setInt(4, numOccurrences);
    prep3.execute();
    prep3.close();
  }

  /**
   * Updates the vocab count in the database by incrementing the count column
   * of the vocab table by the number of articles that a word appeared in
   * in the batch of articles being added.
   *
   * @param vocabOccurrenceMap a map of word to number of occurrences
   * @throws SQLException if an SQL exception occurred
   */
  public void updateVocabCounts(Map<String, Integer> vocabOccurrenceMap) throws SQLException {
    for (String word : vocabOccurrenceMap.keySet()) {
      // if word is not in vocab table, insert then update, else ignore then update
      PreparedStatement prep = conn.prepareStatement(
          "    INSERT OR IGNORE INTO vocab (word, count)"
              + "    VALUES (?, 0);"
      );
      prep.setString(1, word);
      prep.execute();
      prep.close();

      PreparedStatement prep2 = conn.prepareStatement(
          "    UPDATE vocab SET count = count + ? WHERE word = ?;");
      prep2.setInt(1, vocabOccurrenceMap.get(word));
      prep2.setString(2, word);
      prep2.execute();
      prep2.close();
    }
  }

  public void updateArticle(int clusterId, int articleId, boolean finalCluster) throws SQLException {
    PreparedStatement prep;
    prep = conn.prepareStatement("INSERT INTO article_cluster (cluster_id, article_id)\n"
        + "VALUES (?, ?);");
    prep.setInt(1, clusterId);
    prep.setInt(2, articleId);
    prep.execute();
    prep.close();
  }

  public void insertClusters(Set<Cluster<ArticleVertex, Similarity>> clusters) throws SQLException {
    Calendar rightNow = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
    boolean finalCluster = (hour == 23);
    for (Cluster<ArticleVertex, Similarity> c : clusters) {
      int clusterId = insertCluster(c, hour, finalCluster);
      for (ArticleVertex a : c.getNodes()) {
        updateArticle(clusterId, a.getId(), finalCluster);
      }
    }
  }



  public int insertCluster(Cluster<ArticleVertex, Similarity> c, int hour, boolean finalCluster) throws SQLException {
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
    PreparedStatement prep2 = conn.prepareStatement("SELECT last_insert_rowid()\n"
        + "FROM clusters\n"
        + "LIMIT 1;");
    ResultSet rs2 = prep2.executeQuery();
    int lastInsertedId = -1;
    while (rs2.next()) {
      lastInsertedId = rs2.getInt(1);
    }
    if (lastInsertedId == -1) {
      throw new RuntimeException("Somehow article was not inserted?");
    }
    return lastInsertedId;
  }
}
