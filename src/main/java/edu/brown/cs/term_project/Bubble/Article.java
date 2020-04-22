package edu.brown.cs.term_project.Bubble;

/**
 * This class represents an article and its content.
 */
public class Article {
  private int id;
  private String title;
  private String date;
  private String author;
  private String url;

  /**
   * Creates an article.
   * @param id unique id in database
   * @param title Title of the article
   * @param date Date the article was published
   * @param author Author of the article
   * @param url URL to the article
   */
  public Article(int id, String title, String date, String author, String url) {
    this.id = id;
    this.title = title;
    this.date = date;
    this.author = author;
    this.url = url;
  }

  /**
   * Creates an article with just an id (for testing the gui)
   * @param id unique id
   */
  public Article(int id) {
    this.id = id;
  }

  /**
   * Gets the id of the article.
   * @return the id of the article
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the title of the article.
   * @return the title of the article
   */
  public String getTitle() {
    return title;
  }

}
