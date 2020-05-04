package edu.brown.cs.term_project.Graph;

public class ClusterParameters {
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


  public ClusterParameters(String date, boolean doInsert, double textWeight, double entityWeight, double titleWeight,
                           int clusterMethod, double percentageEdgesToConsider) {
    this.date = date;
    this.doInsert = doInsert;
    this.textWeight = textWeight;
    this.entityWeight = entityWeight;
    this.titleWeight = titleWeight;
    this.clusterMethod = clusterMethod;
    this.percentageEdgesToConsider = percentageEdgesToConsider;
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
}
