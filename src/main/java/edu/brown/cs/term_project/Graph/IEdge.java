package edu.brown.cs.term_project.Graph;

public interface IEdge<T> {


  T getSource();

  T getDest();

  double getDistance();
}
