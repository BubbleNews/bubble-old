package edu.brown.cs.term_project.Graph;

public interface IEdge<T extends INode> {

  public String getID();

  public T getSource();

  public T getDest();

  public double getDistance();
}
