package edu.brown.cs.term_project.Graph;

import java.util.List;
import java.util.Set;

public interface INode<T extends IEdge> {

  public List<T> getEdges();

  public T getEdge(INode<T> dst);
}
