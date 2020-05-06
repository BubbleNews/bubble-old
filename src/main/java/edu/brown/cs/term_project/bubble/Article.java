package edu.brown.cs.term_project.bubble;

/**
 * Class that stores all the information about an article but does not include
 * its representation in our graph/clustering scheme.
 */
public class Article {
  // fields
  private int id;
  private String sourceName;
  private String[] authors;
  private String title;
  private String description;
  private String url;
  private String timePublished;
  private String content;

  /**
   * Constructor for an article.
   * @param sourceName the name of the source, ex: "CNN"
   * @param authors the authors of the article
   * @param title the title of the article
   * @param description a description of the article
   * @param url the url of the article
   * @param timePublished the string time published
   * @param content the raw text of the article in some form
   */
  public Article(String sourceName, String[] authors, String title, String description, String url,
                 String timePublished, String content) {
    this.sourceName = sourceName;
    this.authors = authors;
    this.title = title;
    this.description = description;
    this.url = url;
    this.timePublished = timePublished;
    this.content = content;
  }

  /**
   * Another constructor for article that we use to pass information to the client
   * because it contains only what we need and also references the article id in
   * the database.
   * @param id id in articles table in the database
   * @param sourceName the name of the source
   * @param title the title
   * @param url the article url
   * @param timePublished the time published
   */
  public Article(int id, String sourceName, String title, String url, String timePublished) {
    this.id = id;
    this.sourceName = sourceName;
    this.title = title;
    this.url = url;
    this.timePublished = timePublished;
  }

  /**
   * Getter for name of source.
   * @return the source name
   */
  public String getSourceName() {
    return sourceName;
  }

  /**
   * Getter for title.
   * @return the article title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Getter for id in database.
   * @return the id in the articles table
   */
  public int getId() {
    return id;
  }

  /**
   * Getter for url.
   * @return the url to the original article
   */
  public String getUrl() {
    return url;
  }

  /**
   * Getter for time published.
   * @return the time the article was published
   */
  public String getTimePublished() {
    return timePublished;
  }

  /**
   * Getter for content.
   * @return the article content
   */
  public String getContent() {
    return content;
  }

  /**
   * Setter for content.
   * @param content the new content to be stored in content field
   */
  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "Article{"
        + "id=" + id
        + ", title='" + title + '\''
        + '}';
  }
}
