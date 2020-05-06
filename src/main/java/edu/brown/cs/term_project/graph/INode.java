package edu.brown.cs.term_project.graph;

import java.util.List;

/**
 * Interface for nodes in a graph.
 * @param <T> the type of the node
 */
public interface INode<T> {

  /**
   * Gets the list of edges from the node.
   * @return the list of edges from the node
   */
  List<T> getEdges();

  /**
   * Gets the edge from the node to a specific other node.
   * @param dst the destination node for the desired edge
   * @return the edge to dst
   */
  T getEdge(INode<T> dst);

  /**
   * Gets the distance from the node to another node.
   * @param dst the destination node
   * @return the distance to dst along an edge
   */
  double getDistance(INode<T> dst);

  /**
   * Gets a unique id of the node.
   * @return the unique id
   */
  Integer getId();
}
