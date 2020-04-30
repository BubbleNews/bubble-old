package edu.brown.cs.term_project.Bubble;

public class ArticleJSON {

  private String sourceName;
  private String[] authors;
  private String title;
  private String description;
  private String url;
  private String timePublished;
  private String content;

  public ArticleJSON(String sourceName, String[] authors, String title, String description, String url,
                     String timePublished, String content) {
    this.sourceName = sourceName;
    this.authors = authors;
    this.title = title;
    this.description = description;
    this.url = url;
    this.timePublished = timePublished;
    this.content = content;
  }

  public ArticleJSON(String sourceName, String title, String url, String timePublished) {
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
}
