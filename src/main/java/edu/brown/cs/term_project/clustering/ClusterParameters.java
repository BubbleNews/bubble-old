package edu.brown.cs.term_project.clustering;

import spark.QueryParamsMap;
import spark.Request;

import static java.lang.Double.parseDouble;

public class ClusterParameters {
  // default clustering parameters
  public static final int DEFAULT_NUM_ARTICLES = 2400;
  public static final double DEFAULT_TEXT_WEIGHT = 1;
  public static final double DEFAULT_ENTITY_WEIGHT = 1;
  public static final double DEFAULT_TITLE_WEIGHT = 1;

  // date of clusters
  private String date;
  // whether or not to insert into database
  private boolean doInsert;
  // calculating edge weight
  private double textWeight;
  private double entityWeight;
  private double titleWeight;
  // max number of articles to get from the database
  private int numArticles;


  public ClusterParameters() {
    // default clustering parameters
    this.date = null;
    this.doInsert = true;
    this.textWeight = DEFAULT_TEXT_WEIGHT;
    this.entityWeight = DEFAULT_ENTITY_WEIGHT;
    this.titleWeight = DEFAULT_TITLE_WEIGHT;
    this.numArticles = DEFAULT_NUM_ARTICLES;
  }

  public ClusterParameters(QueryParamsMap qm) {
    this.date = qm.value("date");
    this.textWeight = parseDouble(qm.value("textWeight"));
    this.entityWeight = parseDouble(qm.value("entityWeight"));
    this.titleWeight = parseDouble(qm.value("titleWeight"));
    this.numArticles = Integer.parseInt(qm.value("numArticles"));
    this.doInsert = false;
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

  public void setNumArticles(int numArticles) {
    this.numArticles = numArticles;
  }
}
