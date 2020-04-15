package edu.brown.cs.term_project.Graph;

import edu.brown.cs.term_project.Bubble.Article;

import java.util.Objects;
import java.util.Set;

public class DemoCluster implements ICluster<DemoNode> {
  private DemoNode head;
  private Integer size;
  private double avgConnections;
  private Set<DemoNode> nodes;
  private double avgRadius;
  private double std;

  public DemoCluster(DemoNode head, Set<DemoNode> nodes) {
    this.head = head;
    this.nodes = nodes;
    this.size = nodes.size();
    setAvgConnections();
    setAvgRadius();
    setStd();
  }

  @Override
  public Set<DemoNode> getNodes() {
    return nodes;
  }

  @Override
  public Integer getSize() {
    return size;
  }

  @Override
  public double getAvgConnections() {
    return avgConnections;
  }

  @Override
  public double getAvgRadius() {
    return avgRadius;
  }

  @Override
  public double getStd() {
    return std;
  }

  @Override
  public DemoNode getHeadNode() {
    return head;
  }

  // TODO: fill in these functions
  public void setAvgRadius() {
    this.avgRadius = 0;
  }

  public void setAvgConnections() {
    this.avgConnections = 0;
  }

  public void setStd() {
    this.std = 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DemoCluster that = (DemoCluster) o;
    return Objects.equals(head, that.head);
  }

  @Override
  public int hashCode() {
    return Objects.hash(head);
  }
}
