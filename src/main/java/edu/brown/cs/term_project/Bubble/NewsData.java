package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Database.Database;
import edu.brown.cs.term_project.Graph.ChartCluster;
import edu.brown.cs.term_project.Graph.Cluster;
import edu.brown.cs.term_project.TextSimilarity.IWord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class NewsData extends Database {
  private Connection conn;

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
    conn = super.getConn();
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

  /**
   * Inserts an article and the entities of that article given by frequency map.
   *
   * @param article            the article to insert
   * @param entityFrequencyMap the frequency map for that article
   * @throws SQLException if there was a database error
   */
  public void insertArticleAndEntities(Article article, HashMap<Entity, Integer> entityFrequencyMap) throws SQLException {
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
  private void insertEntities(int articleId, HashMap<Entity, Integer> entityFrequencyMap) throws SQLException {
    for (Entity entity : entityFrequencyMap.keySet()) {
      if (!(entity.getClassType().length() == 1)) {
        insertEntity(articleId, entity, entityFrequencyMap.get(entity));
      }
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
    // first insert into entity
    PreparedStatement prep = conn.prepareStatement("INSERT OR IGNORE INTO entity (class, entity, "
        + "count) VALUES (?, ?, 0);");
    prep.setString(1, entity.getClassType());
    prep.setString(2, entity.getWord());
    prep.execute();
    prep.close();

    PreparedStatement prep2 = conn.prepareStatement("UPDATE entity SET count = count + 1 WHERE "
        + "entity = ? AND class = ?;");
    prep2.setString(1, entity.getWord());
    prep2.setString(2, entity.getClassType());
    prep2.execute();
    prep2.close();

    PreparedStatement prep3 = conn.prepareStatement("INSERT INTO article_entity (article_id, "
        + "entity_class, entity_entity, count) VALUES (?, ?, ?, ?);");
    prep3.setInt(1, articleId);
    prep3.setString(2, entity.getClassType());
    prep3.setString(3, entity.getWord());
    prep3.setInt(4, numOccurrences);
    prep3.execute();
    prep3.close();
  }

  public List<Article> getArticlesFromCluster(int clusterId) throws SQLException {
    // build sql statement
    String statement = "SELECT title, url, date_published, source, id FROM articles";
    PreparedStatement prep = conn.prepareStatement(statement);
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
        String statement = "SELECT id, source, title, url, date_published, text FROM articles";
        PreparedStatement prep = conn.prepareStatement(statement);
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
        return createArticleVerticies(articles, articleText);
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

  // Ben/John

  public Set<ArticleVertex> getArticleVertices(Integer hours) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT id, source, title, url, date_published, "
        + "text "
        + "FROM articles "
        + "WHERE date_pulled >= DATETIME('now', '-24 hours') AND date_pulled < "
        + "DATETIME('now');"
    );
    //prep.setInt(1, hours);
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
    return createArticleVerticies(articles, articleText);
  }

  public Set<ArticleVertex> createArticleVerticies(
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

  public void insertClusters(Set<Cluster<ArticleVertex, Similarity>> clusters) throws SQLException {
    Calendar rightNow = Calendar.getInstance();
    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
    Boolean finalCluster = (hour == 23);
    for (Cluster<ArticleVertex, Similarity> c : clusters) {
      insertCluster(c, hour, finalCluster);
      int clusterId = getClusterId(c.getHeadNode().getId());
      for (ArticleVertex a : c.getNodes()) {
        updateArticle(clusterId, a.getId(), finalCluster);
      }
    }
  }

  public void insertCluster(Cluster<ArticleVertex, Similarity> c, int hour, boolean finalCluster) throws SQLException {
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

  public int getClusterId(int head) throws SQLException {
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

  public void updateArticle(int clusterId, int articleId, boolean finalCluster) throws SQLException {
    PreparedStatement prep;
    if (finalCluster) {
      prep = conn.prepareStatement("UPDATE articles\n"
          + "SET final_cluster_id = ?\n"
          + "WHERE id = ?;");
    } else {
      prep = conn.prepareStatement("UPDATE articles\n"
          + "SET temp_cluster_id = ?\n"
          + "WHERE id = ?;");
    }
    prep.setInt(1, clusterId);
    prep.setInt(2, articleId);
    prep.execute();
    prep.close();
  }

  /**
   * Gets the clusters for a given day. This will be passed to the front end.
   *
   * @param date the date to search for (java.util.Date)
   * @return a set of cluster objects
   * @throws SQLException only thrown if the database is malformed
   */
  public List<ChartCluster> getClusters(String date) throws SQLException {
    String query = "SELECT id, title, size FROM clusters WHERE day = ?";
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

  public static void main(String[] args) throws Exception {
    Article testArticle = new Article("BuzzFeed", new String[]{"Kayla Suazo"},
        "23 Top-Rated Cleaning Products That Are Popular For A Reason",
        "So good, they have *a ton* of 4- and 5-star reviews.",
        "https://www.buzzfeed.com/kaylasuazo/top-rated-cleaning-products-"
            + "that-are-popular-for-a-reason",
        "2020-04-27T17:22:24.963019Z",
        "all you have to do be let it sit for 15 minute and wipe -- minimal work on you "
            + "part . check out BuzzFeed 's full write-up on this Feed-N-Wax Wood Polish to learn "
            + "more!and ! it have 4,700 + positive review on amazon.promising review : `` OMG ! "
            + "this be the most amazing product ! we inherit some antique furniture from the '30s"
            + " that have be in storage forever ... it be dry and dirty and not much to look at ."
            + " I use this product on it and the oak wood literally come alive show the beautiful"
            + " grain and texture of the wood . I have since use it on my oak kitchen cabinet and"
            + " they look AMAZING ! I will never use anything else other than this product on my "
            + "wood surface ! no greasy feel -- and a fantastic smell ! '' -- Tiffany SadowskiGet"
            + " it from Amazon for $ 8.48 + -lrb- available in eight size -rrb- ."
    );
    NewsData db = new NewsData("data/backloaded.db");
    db.insertArticle(testArticle);
  }

}
