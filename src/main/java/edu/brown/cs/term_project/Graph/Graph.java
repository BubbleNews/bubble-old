package edu.brown.cs.term_project.Graph;

import java.util.*;

public abstract class Graph<C extends ICluster<T>, T extends INode, S extends IEdge> {
  Set<T> nodes;
  List<S> edges;
  double threshold;

  public Graph() {
    this.nodes = new HashSet<T>();
    this.edges = new ArrayList<S>();
  }


  public Graph(Set<T> nodes, List<S> edges) {
    this.nodes = nodes;
    this.edges = edges;
    setThreshold();
  }

  // TODO: implement createClusters and add them to database
  public abstract void createClusters();

  // TODO: implement setThreshold
  public abstract void setThreshold();

}
