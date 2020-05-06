package edu.brown.cs.term_project.graph;

/**
 * Interface for edges in a graph.
 * @param <T> the type of node in the graph
 */
public interface IEdge<T> {

  /**
   * Gets the source node of the edge.
   * @return the source node
   */
  T getSource();

  /**
   * Gets the destination node of the edge.
   * @return the destination node
   */
  T getDest();

  /**
   * Gets the distance/weight of the edge.
   * @return the distance or weight of the edge
   */
  double getDistance();
}
