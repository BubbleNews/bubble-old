package edu.brown.cs.term_project.graph;

public interface IEdge<T> {


  T getSource();

  T getDest();

  double getDistance();
}
