package edu.brown.cs.term_project.Bubble;

public class ArticleJSON {

  private String[] authors;
  private String title;
  private String description;
  private String url;
  private String timePublished;
  private String content;

  public ArticleJSON(String[] authors, String title, String description, String url,
                     String timePublished, String content) {
    this.authors = authors;
    this.title = title;
    this.description = description;
    this.url = url;
    this.timePublished = timePublished;
    this.content = content;
  }

  public String[] getAuthors() {
    return authors;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
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
