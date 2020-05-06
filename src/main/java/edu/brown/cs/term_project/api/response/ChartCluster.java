package edu.brown.cs.term_project.api.response;

import edu.brown.cs.term_project.bubble.Article;

import java.util.List;

/**
 * This class is only used to pass cluster information from the database to the front end.
 */
public class ChartCluster {
  private int clusterId;
  private String headline;
  private int size;
  private double meanRadius;
  private List<Article> articles;

  /**
   * Constructor for a chart cluster.
   * @param clusterId the id
   * @param headline the headline article title
   * @param size size of cluster
   * @param meanRadius mean radius of cluster
   * @param articles list of articles
   */
  public ChartCluster(int clusterId, String headline, int size,
                      double meanRadius, List<Article> articles) {
    this.clusterId = clusterId;
    this.headline = headline;
    this.size = size;
    this.meanRadius = meanRadius;
    this.articles = articles;
  }

  /**
   * Getter for cluster id.
   * @return cluster id
   */
  public int getClusterId() {
    return clusterId;
  }

  /**
   * Getter for size.
   * @return the cluster size
   */
  public int getSize() {
    return size;
  }

  /**
   * Getter for headline.
   * @return the headline article title
   */
  public String getHeadline() {
    return headline;
  }
}
