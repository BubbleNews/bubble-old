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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class for reading from the database, all read only query wrappers are
 * contained within this class.
 */
public class NewsDataRead {
  private Connection conn;
  private static final int HOURS_PER_DAY = 24;

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
   * @throws SQLException if error with database
   */
  public Set<String> getSources() throws SQLException {
    String query = "SELECT DISTINCT source from articles";
    Set<String> sources = new HashSet<>();
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          sources.add(rs.getString(1));
        }
        return sources;
      }
    }
  }

  /**
   * Gets articles from a cluster.
   * @param clusterId the id of the cluster to get articles for
   * @return the articles for that cluster
   * @throws SQLException if error with database
   */
  public List<Article> getArticlesFromCluster(int clusterId) throws SQLException {
    String statement = "SELECT title, url, date_published, source, id FROM articles a\n"
        + "JOIN article_cluster c ON c.article_id = a.id\n"
        + "WHERE c.cluster_id = ? ORDER BY date_published desc";
    List<Article> articles = new ArrayList<>();

    try (PreparedStatement prep = conn.prepareStatement(statement)) {
      prep.setInt(1, clusterId);
      try (ResultSet rs = prep.executeQuery()) {
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
    }
  }

  /**
   * Gets article vertices from a list of article ids.
   * @param articleIDs the array of article ids
   * @return the set of article vertices
   * @throws SQLException if error with database
   */
  public Set<ArticleVertex> getArticleVerticesFromArticleIds(
      String[] articleIDs) throws SQLException {
    // array -> (id1,id2,...,idn) for SQL
    String articleIdsSQL = Arrays.toString(articleIDs)
        .replace("[", "(")
        .replace("]", ")");
    String statement = "SELECT id, source, title, url, date_published, text FROM articles "
        + "WHERE id IN " + articleIdsSQL;
    PreparedStatement prep = conn.prepareStatement(statement);
    return getArticleVerticesHelper(prep);
  }

  /**
   * Gets article vertices for a certain date.
   * @param date the date to get articles from
   * @param offset timezone offset
   * @param hoursBack number of hours back to look
   * @param current whether or not to search for most current articles
   * @param maxNumArticles max number of articles to return
   * @return set of article vertices
   * @throws SQLException if error with database
   */
  public Set<ArticleVertex> getArticleVertices(String date, int offset, int hoursBack,
                                               boolean current,
                                               int maxNumArticles) throws SQLException {
    String query = "SELECT id, source, title, url, date_published, "
            + "text FROM articles "
            + "WHERE date_published >= DATETIME(DATETIME(?, ?), ?) AND date_published < DATETIME"
            + "(?,?) "
            + "ORDER BY date_published "
            + "LIMIT (?);";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      String timeOffset = "+" + (offset + HOURS_PER_DAY) + " hours";
      String hourOffset = "-" + hoursBack + " hours";
      String defaultBack = "+0 hours";
      if (current) {
        prep.setString(1, "now");
        prep.setString(2, defaultBack);
        prep.setString(3, hourOffset);
        prep.setString(4, "now");
        prep.setString(5, defaultBack);
      } else {
        prep.setString(1, date);
        prep.setString(2, timeOffset);
        prep.setString(3, hourOffset);
        prep.setString(4, date);
        prep.setString(5, timeOffset);
      }
      prep.setInt(6, maxNumArticles);
      return getArticleVerticesHelper(prep);
    }
  }

  /**
   * Helper method that produces set of article vertices from a prepared statement
   * that will get articles from the database, factored out to reduce duplicated
   * code.
   * @param prep a prepared statement with a query that will get articles from the
   *             database.
   * @return the set of article vertices
   * @throws SQLException if error with database
   */
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

  /**
   * Creates article vertices from a set of articles and their respective texts.
   * @param articles the articles
   * @param articleText map from article id to article test
   * @return set of article vertices (articles in graph structure)
   * @throws SQLException if error with database
   */
  private Set<ArticleVertex> createArticleVertices(
      Set<Article> articles, Map<Integer, String> articleText) throws SQLException {
    Set<ArticleVertex> articleVertices = new HashSet<>();
    for (Article article : articles) {
      articleVertices.add(new ArticleVertex(article, articleText.get(article.getId()),
          this.getArticleEntityFreq(article.getId())));
    }
    return articleVertices;
  }

  /**
   * Gets the vocab frequency map (number of articles a word appears in).
   * @return the vocab frequency map
   * @throws SQLException if error with database
   */
  public Map<ArticleWord, Double> getVocabFreq() throws SQLException {
    String query = "Select word, count FROM vocab;";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      try (ResultSet rs = prep.executeQuery()) {
        Map<ArticleWord, Double> words = new HashMap<>();
        while (rs.next()) {
          words.put(new ArticleWord(rs.getString(1)), rs.getDouble(2));
        }
        return words;
      }
    }
  }

  /**
   * Gets the entity frequency map (number of articles an entity appears in).
   * @return the entity frequency map
   * @throws SQLException if error with database
   */
  public Map<Entity, Double> getEntityFreq() throws SQLException {
    String query = "Select entity, class, count FROM entity;";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      try (ResultSet rs = prep.executeQuery()) {
        Map<Entity, Double> words = new HashMap<>();
        while (rs.next()) {
          words.put(new Entity(rs.getString(1),
              rs.getString(2)), rs.getDouble(3));
        }
        return words;
      }
    }
  }

  /**
   * Gets the entity term frequency map for a single article from the database (number of times an
   * entity was found in the article with id articleId.
   * @param articleId the id of the article
   * @return the entity frequency map for that article
   * @throws SQLException if error with database
   */
  public Map<IWord, Double> getArticleEntityFreq(Integer articleId) throws SQLException {
    String query = "SELECT entity_class, entity_entity, count "
        + "FROM article_entity WHERE article_id = ?;";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setInt(1, articleId);
      try (ResultSet rs = prep.executeQuery()) {
        Map<IWord, Double> articleEntityFreq = new HashMap<>();
        while (rs.next()) {
          articleEntityFreq.put(new Entity(rs.getString(2),
              rs.getString(1)), rs.getDouble(3));
        }
        return articleEntityFreq;
      }
    }
  }

  /**
   * Gets the number of articles the word that appeared in the most articles
   * appeared in.
   * @return the number of articles the most widespread word appeared in.
   * @throws SQLException if error with database
   */
  public int getMaxVocabCount() throws SQLException {
    String query = "SELECT MAX(count) FROM vocab;";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      ResultSet rs = prep.executeQuery();
      rs.next();
      return rs.getInt(1);
    }
  }

  /**
   * Gets the clusters for a given day. This will be passed to the front end.
   * @param date the date to search for (java.util.Date)
   * @param hours the amount to off set the hours
   * @param addDays the amount of days to add to the date passed in
   * @return a set of cluster objects
   * @throws SQLException only thrown if the database is malformed
   */
  public List<ChartCluster> getClusters(String date, int hours, int addDays) throws SQLException {
    String query = "SELECT id, title, size, avg_radius FROM clusters WHERE day = DATE(?, ?) AND "
        + "hour = ?;";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setString(1, date);
      String daysToAdd = "+" + addDays + " days";
      prep.setString(2, daysToAdd);
      prep.setInt(3, hours);
      try (ResultSet rs = prep.executeQuery()) {
        List<ChartCluster> clusters = new ArrayList<>();
        while (rs.next()) {
          int clusterId = rs.getInt(1);
          String headline = rs.getString(2);
          int size = rs.getInt(3);
          double meanRadius = rs.getDouble(4);
          List<Article> articles = new ArrayList<>();
          clusters.add(new ChartCluster(clusterId, headline, size, meanRadius, articles));
        }
        return clusters;
      }
    }
  }

  /**
   * Gets the newest set of clusters.
   * @return the most recent set of clusters
   * @throws SQLException if error with database
   */
  public List<ChartCluster> getNewestClusters() throws SQLException {
    String query = "SELECT day, hour FROM clusters ORDER BY day DESC, hour DESC LIMIT 1;";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      try (ResultSet rs = prep.executeQuery()) {
        List<ChartCluster> clusters = new ArrayList<>();
        if (rs.next()) {
          String day = rs.getString(1);
          int hour = rs.getInt(2);
          clusters = getClusters(day, hour, 0);
        }
        return clusters;
      }
    }
  }

  /**
   * Gets a list of clusters for a day.
   * @param day the day to get clusters for
   * @return the list of clusters for that day
   * @throws SQLException if error with database
   */
  public List<ChartCluster> getClusterByDay(String day) throws SQLException {
    String query = "SELECT hour FROM clusters WHERE day = DATE(?) ORDER BY hour DESC "
        + "LIMIT 1;";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setString(1, day);
      try (ResultSet rs = prep.executeQuery()) {
        List<ChartCluster> clusters = new ArrayList<>();
        if (rs.next()) {
          int hour = rs.getInt(1);
          clusters = getClusters(day, hour, 0);
        }
        return clusters;
      }
    }
  }

  /**
   * Gets an article by title from database.
   * @param title the article title
   * @return the article or null if no article with title title in database
   * @throws SQLException if thrown
   */
  public Article getArticleByTitle(String title) throws SQLException {
    Article toReturn = null;
    String query = "SELECT id, source, title, url, date_published FROM articles "
        + "WHERE title = (?)";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setString(1, title);
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          toReturn = new Article(rs.getInt(1),
              rs.getString(2), rs.getString(3),
              rs.getString(4), rs.getString(5));
        }
      }
    }
    return toReturn;
  }
}
