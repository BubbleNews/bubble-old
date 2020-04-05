package edu.brown.cs.term_project.Graph;

import java.util.Set;

public class Graph<C extends ICluster<T>, T extends INode, S extends IEdge> {
  private Set<T> nodes;
  private Set<S> edges;
  private double threshold;


  public Graph(Set<T> nodes, Set<S> edges) {
    this.nodes = nodes;
    this.edges = edges;
    setThreshold();
  }

  // TODO: implement createClusters and add them to database
  public void createClusters() {
  }

  // TODO: implement setThreshold
  public void setThreshold() {
    double threshold = 0;
    this.threshold = threshold;
  }
}
