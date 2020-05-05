package edu.brown.cs.term_project.clustering;

import spark.QueryParamsMap;
import spark.Request;

import java.util.Calendar;
import java.util.TimeZone;

import static java.lang.Double.parseDouble;

public class ClusterParameters {
  // default clustering parameters
  private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
  public static final int DEFAULT_NUM_ARTICLES = 2400;
  public static final double DEFAULT_TEXT_WEIGHT = 1;
  public static final double DEFAULT_ENTITY_WEIGHT = 1;
  public static final double DEFAULT_TITLE_WEIGHT = 1;
  public static final int DEFAULT_CLUSTER_METHOD = 1;
  public static final String DEFAULT_YEAR = String.valueOf(calendar.YEAR);
  public static final String DEFAULT_MONTH = String.valueOf(calendar.MONTH);
  public static final String DEFAULT_DAY = String.valueOf(calendar.DAY_OF_MONTH);
  public static final boolean DEFAULT_IS_TODAY = true;
  public static final int DEFAULT_OFFSET = 0;
  public static final int DEFAULT_HOURS = 24;


  // date of clusters
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

  private String year;
  private String month;
  private String day;
  private boolean isToday;
  private int offset;
  private int hours;


  public ClusterParameters(int hours, boolean doInsert) {
    // default clustering parameters
    this.doInsert = doInsert;
    this.textWeight = DEFAULT_TEXT_WEIGHT;
    this.entityWeight = DEFAULT_ENTITY_WEIGHT;
    this.titleWeight = DEFAULT_TITLE_WEIGHT;
    this.clusterMethod = DEFAULT_CLUSTER_METHOD;
    this.numArticles = DEFAULT_NUM_ARTICLES;
    this.year = DEFAULT_YEAR;
    this.month = DEFAULT_MONTH;
    this.day = DEFAULT_DAY;
    this.isToday = DEFAULT_IS_TODAY;
    this.offset = DEFAULT_OFFSET;
    this.hours = hours;
  }

  public ClusterParameters(String year, String month, String day,
                           boolean isToday, int offset, int hours, boolean doInsert) {
    // default clustering parameters
    this.doInsert = doInsert;
    this.textWeight = DEFAULT_TEXT_WEIGHT;
    this.entityWeight = DEFAULT_ENTITY_WEIGHT;
    this.titleWeight = DEFAULT_TITLE_WEIGHT;
    this.clusterMethod = DEFAULT_CLUSTER_METHOD;
    this.numArticles = DEFAULT_NUM_ARTICLES;
    this.year = DEFAULT_YEAR;
    this.month = DEFAULT_MONTH;
    this.day = DEFAULT_DAY;
    this.isToday = DEFAULT_IS_TODAY;
    this.offset = DEFAULT_OFFSET;
    this.hours = DEFAULT_HOURS;
  }

  public ClusterParameters(QueryParamsMap qm) {
    this.doInsert = false;
    this.textWeight = parseDouble(qm.value("textWeight"));
    this.entityWeight = parseDouble(qm.value("entityWeight"));
    this.titleWeight = parseDouble(qm.value("titleWeight"));
    this.clusterMethod = Integer.parseInt(qm.value("clusterMethod"));
    this.percentageEdgesToConsider = parseDouble(qm.value("edgeThreshold"));
    this.numArticles = Integer.parseInt(qm.value("numArticles"));
    this.year = qm.value("year");
    this.month = qm.value("month");
    this.day = qm.value("day");
    this.isToday = Boolean.parseBoolean(qm.value("isToday"));
    this.offset = Integer.parseInt(qm.value("offset"));
    this.hours = DEFAULT_HOURS;
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

  public String getDate() {
    return year + "-" + month + "-" + day;
  }

  public int getHours() {
    return hours;
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

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public boolean isToday() {
    return isToday;
  }

  public void setToday(boolean today) {
    isToday = today;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public void setNumArticles(int numArticles) {
    this.numArticles = numArticles;
  }
}
