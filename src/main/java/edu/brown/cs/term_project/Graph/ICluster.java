package edu.brown.cs.term_project.Graph;

import java.util.Set;

public interface ICluster<T extends INode<E>, E extends IEdge<T>> {

  Integer getId();

  Set<T> getNodes();

  Integer getSize();

  T getHeadNode();

  double getAvgConnections();

  double getAvgRadius();

  double getStd();

  void addNode(T node);

  void adjustHead();

}
