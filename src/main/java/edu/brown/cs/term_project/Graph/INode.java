package edu.brown.cs.term_project.Graph;

import java.util.Set;

public interface INode<T extends IEdge> {

  public String getID();

  public Set<T> getEdges();

  public T getEdge(INode<T> dst);
}
