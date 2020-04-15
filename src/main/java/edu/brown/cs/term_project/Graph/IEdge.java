package edu.brown.cs.term_project.Graph;

public interface IEdge<T extends INode> {


  public T getSource();

  public T getDest();

  public double getDistance();
}
