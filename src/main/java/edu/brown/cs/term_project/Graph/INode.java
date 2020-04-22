package edu.brown.cs.term_project.Graph;

import java.util.List;


public interface INode<T> {

  List<T> getEdges();

  T getEdge(INode<T> dst);

  double getDistance(INode<T> dst);

  Integer getId();
}
