package edu.brown.cs.term_project.clustering;

public class ClusterParameters {
  // default clustering parameters
  public static final int DEFAULT_NUM_ARTICLES = 2400;
  public static final double DEFAULT_TEXT_WEIGHT = 1;
  public static final double DEFAULT_ENTITY_WEIGHT = 1;
  public static final double DEFAULT_TITLE_WEIGHT = 1;
  public static final int DEFAULT_CLUSTER_METHOD = 1;

  // date of clusters
  private String date;
  // whether or not to insert into database
  private boolean doInsert;
  // calculating edge weight
  private double textWeight;
  private double entityWeight;
  private double titleWeight;
  // which clustering method to use
  private int clusterMethod;
  // clustering
  private double percentageEdgesToConsider;
  // max number of articles to get from the database
  private int numArticles;


  public ClusterParameters() {
    // default clustering parameters
    this.date = null;
    this.doInsert = true;
    this.textWeight = DEFAULT_TEXT_WEIGHT;
    this.entityWeight = DEFAULT_ENTITY_WEIGHT;
    this.titleWeight = DEFAULT_TITLE_WEIGHT;
    this.clusterMethod = DEFAULT_CLUSTER_METHOD;
    this.numArticles = DEFAULT_NUM_ARTICLES;
  }

  public boolean getDoInsert() {
    return doInsert;
  }

  public double getTextWeight() {
    return textWeight;
  }

  public double getEntityWeight() {
    return entityWeight;
  }

  public double getTitleWeight() {
    return titleWeight;
  }

  public double getPercentageEdgesToConsider() {
    return percentageEdgesToConsider;
  }

  public int getClusterMethod() {
    return clusterMethod;
  }

  public int getNumArticles() {
    return numArticles;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setDoInsert(boolean doInsert) {
    this.doInsert = doInsert;
  }

  public void setTextWeight(double textWeight) {
    this.textWeight = textWeight;
  }

  public void setEntityWeight(double entityWeight) {
    this.entityWeight = entityWeight;
  }

  public void setTitleWeight(double titleWeight) {
    this.titleWeight = titleWeight;
  }

  public void setClusterMethod(int clusterMethod) {
    this.clusterMethod = clusterMethod;
  }

  public void setPercentageEdgesToConsider(double percentageEdgesToConsider) {
    this.percentageEdgesToConsider = percentageEdgesToConsider;
  }

  public void setNumArticles(int numArticles) {
    this.numArticles = numArticles;
  }
}
