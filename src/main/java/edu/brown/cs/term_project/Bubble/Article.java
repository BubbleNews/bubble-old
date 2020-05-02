package edu.brown.cs.term_project.Bubble;

public class Article {

  private int id;
  private String sourceName;
  private String[] authors;
  private String title;
  private String description;
  private String url;
  private String timePublished;
  private String content;

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

  public Article(int id, String sourceName, String title, String url, String timePublished) {
    this.id = id;
    this.sourceName = sourceName;
    this.title = title;
    this.url = url;
    this.timePublished = timePublished;
  }

  public String getSourceName() {
    return sourceName;
  }

  public String getTitle() {
    return title;
  }

  public int getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  public String getTimePublished() {
    return timePublished;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "Article{" +
        "id=" + id +
        ", title='" + title + '\'' +
        '}';
  }
}
