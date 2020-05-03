package edu.brown.cs.term_project.Graph;

public class ClusterParameters {
  // calculating edge weight
  private double textWeight;
  private double entityWeight;
  private double titleWeight;
  // clustering
  private double percentageEdgesToConsider;
  // which clustering method to use
  private int clusterMethod;

  public ClusterParameters(double textWeight, double entityWeight, double titleWeight, double percentageEdgesToConsider, int clusterMethod) {
    this.textWeight = textWeight;
    this.entityWeight = entityWeight;
    this.titleWeight = titleWeight;
    this.percentageEdgesToConsider = percentageEdgesToConsider;
    this.clusterMethod = clusterMethod;
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
