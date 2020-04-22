package edu.brown.cs.term_project.Graph;

import java.util.*;

public abstract class Graph<C extends ICluster<T, S>, T extends INode<S>, S extends IEdge<T>> {
  private Set<T> nodes;
  private List<S> edges;
  private double threshold;


  // TODO: implement createClusters and add them to database
  public abstract void createClusters();

  // TODO: implement setThreshold
  public abstract void setThreshold();

}
