package edu.brown.cs.term_project.clustering;

import spark.QueryParamsMap;

import java.util.Calendar;
import java.util.TimeZone;

import static java.lang.Double.parseDouble;

/**
 * Class containing parameters for clustering.
 */
public class ClusterParameters {
  // default clustering parameters
  private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
  public static final int DEFAULT_NUM_ARTICLES = 2400;
  public static final double DEFAULT_TEXT_WEIGHT = 1;
  public static final double DEFAULT_ENTITY_WEIGHT = 1;
  public static final double DEFAULT_TITLE_WEIGHT = 1;
  public static final String DEFAULT_YEAR = String.valueOf(calendar.get(Calendar.YEAR));
  public static final String DEFAULT_MONTH = String.valueOf(calendar.get(Calendar.MONTH));
  public static final String DEFAULT_DAY = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
  public static final boolean DEFAULT_IS_TODAY = true;
  public static final int DEFAULT_OFFSET = 0;
  public static final int DEFAULT_HOURS = 24;

  // date of clusters
  private boolean doInsert;
  // calculating edge weight
  private double textWeight;
  private double entityWeight;
  private double titleWeight;
  // max number of articles to get from the database
  private int numArticles;

  private String year;
  private String month;
  private String day;
  private boolean isToday;
  private int offset;
  private int hours;

  /**
   * Default constructor for clustering parameters.
   * @param hours the hour to get clusters for
   * @param doInsert whether or not to insert into the database
   */
  public ClusterParameters(int hours, boolean doInsert) {
    // default clustering parameters
    this.doInsert = doInsert;
    this.textWeight = DEFAULT_TEXT_WEIGHT;
    this.entityWeight = DEFAULT_ENTITY_WEIGHT;
    this.titleWeight = DEFAULT_TITLE_WEIGHT;
    this.numArticles = DEFAULT_NUM_ARTICLES;
    this.year = DEFAULT_YEAR;
    this.month = DEFAULT_MONTH;
    this.day = DEFAULT_DAY;
    this.isToday = DEFAULT_IS_TODAY;
    this.offset = DEFAULT_OFFSET;
    this.hours = hours;
  }

  /**
   * Another constructor for clustering parameters with more datetime information.
   * @param year the year string
   * @param month the month string
   * @param day the day string
   * @param isToday whether or not getting clusters for today
   * @param offset a timezone offset
   * @param hours the hour to get cluster for
   * @param doInsert whether or not to insert into the database
   */
  public ClusterParameters(String year, String month, String day,
                           boolean isToday, int offset, int hours, boolean doInsert) {
    // default clustering parameters
    this.doInsert = doInsert;
    this.year = year;
    this.month = month;
    this.day = day;
    this.isToday = isToday;
    this.offset = offset;
    this.hours = hours;
    this.textWeight = DEFAULT_TEXT_WEIGHT;
    this.entityWeight = DEFAULT_ENTITY_WEIGHT;
    this.titleWeight = DEFAULT_TITLE_WEIGHT;
    this.numArticles = DEFAULT_NUM_ARTICLES;

  }

  /**
   * Constructor for getting cluster params from an HTTP request params map.
   * @param qm the query params map
   * @param useDate whether or not to use the date
   */
  public ClusterParameters(QueryParamsMap qm, boolean useDate) {
    this.doInsert = false;
    this.textWeight = parseDouble(qm.value("textWeight"));
    this.entityWeight = parseDouble(qm.value("entityWeight"));
    this.titleWeight = parseDouble(qm.value("titleWeight"));
    this.numArticles = Integer.parseInt(qm.value("numArticles"));
    if (useDate) {
      this.year = qm.value("year");
      this.month = qm.value("month");
      this.day = qm.value("day");
      this.isToday = Boolean.parseBoolean(qm.value("isToday"));
      this.offset = Integer.parseInt(qm.value("offset"));
      this.hours = DEFAULT_HOURS;
    } else {
      this.year = DEFAULT_YEAR;
      this.month = DEFAULT_MONTH;
      this.day = DEFAULT_DAY;
      this.isToday = DEFAULT_IS_TODAY;
      this.offset = DEFAULT_OFFSET;
      this.hours = DEFAULT_HOURS;
    }

  }

  /**
   * Gets doInsert field.
   * @return doInsert
   */
  public boolean getDoInsert() {
    return doInsert;
  }

  /**
   * Gets textWeight parameter for edge calculation.
   * @return the text weight
   */
  public double getTextWeight() {
    return textWeight;
  }

  /**
   * Gets textWeight parameter for edge calculation.
   * @return the the entity weight
   */
  public double getEntityWeight() {
    return entityWeight;
  }

  /**
   * Gets title weight parameter for edge calculation.
   * @return the the title weight
   */
  public double getTitleWeight() {
    return titleWeight;
  }

  /**
   * Gets the number of articles to cluster.
   * @return the number of clusters parameter
   */
  public int getNumArticles() {
    return numArticles;
  }

  /**
   * Gets the string concatenation of the date.
   * @return the string formatted date
   */
  public String getDate() {
    return year + "-" + month + "-" + day;
  }

  /**
   * Gets the hour to cluster.
   * @return the hour
   */
  public int getHours() {
    return hours;
  }

  /**
   * Gets the year of the date to cluster.
   * @return the year
   */
  public String getYear() {
    return year;
  }

  /**
   * Sets the year.
   * @param year the year of the date
   */
  public void setYear(String year) {
    this.year = year;
  }

  /**
   * Gets the month to cluster for.
   * @return the month
   */
  public String getMonth() {
    return month;
  }

  /**
   * Sets the month parameter.
   * @param month the month
   */
  public void setMonth(String month) {
    this.month = month;
  }

  /**
   * Gets the day parameter.
   * @return the day
   */
  public String getDay() {
    return day;
  }

  /**
   * Sets the day parameter.
   * @param day the day
   */
  public void setDay(String day) {
    this.day = day;
  }

  /**
   * Gets the isToday parameter.
   * @return whether or not cluster for today.
   */
  public boolean isToday() {
    return isToday;
  }

  /**
   * Sets the today parameter.
   * @param today whether or not to cluster for today
   */
  public void setToday(boolean today) {
    isToday = today;
  }

  /**
   * Gets the offset from utc.
   * @return the time zone offset in hours
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Sets the offset from utc.
   * @param offset the timezone offset in hours
   */
  public void setOffset(int offset) {
    this.offset = offset;
  }

  /**
   * Sets the number of articles to cluster.
   * @param numArticles the max number of articles
   */
  public void setNumArticles(int numArticles) {
    this.numArticles = numArticles;
  }
}
