package edu.brown.cs.term_project.DemoGraph;

import edu.brown.cs.term_project.Graph.ICluster;

import java.util.Objects;
import java.util.Set;

public class DemoCluster implements ICluster<DemoNode> {
  private Integer id;
  private DemoNode head;
  private Integer size;
  private double avgConnections;
  private Set<DemoNode> nodes;
  private double avgRadius;
  private double std;

  public DemoCluster(Integer id, DemoNode head, Set<DemoNode> nodes) {
    this.id = id;
    this.head = head;
    this.nodes = nodes;
    this.size = nodes.size();
    setAvgConnections();
    setAvgRadius();
    setStd();
  }

  @Override
  public Integer getId() {
    return id;
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
  public void addNode(DemoNode node) {
    this.nodes.add(node);
    this.size = nodes.size();
    setAvgConnections();
    setAvgRadius();
    setStd();
  }

  @Override
  public void adjustHead() {
  }

  @Override
  public DemoNode getHeadNode() {
    return head;
  }

  // TODO: fill in these functions
  public void setAvgRadius() {
    double sum = 0;
    int count = 0;
    for (DemoNode n1: nodes) {
      for (DemoNode n2: nodes) {
        if (!n1.equals(n2)) {
          sum += n1.getEdge(n2).getDistance();
          count++;
        }
      }
    }
    if (count > 0) {
      this.avgRadius = sum/count;
    } else {
      this.avgRadius = 0;
    }

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
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
