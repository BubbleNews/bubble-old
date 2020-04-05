package edu.brown.cs.term_project.Dijkstra;

import java.util.List;
import java.util.Objects;

/**
 * Class that represents a generic vertex of a graph.
 */
public class GenericVertex implements IVertex<GenericEdge> {
  String name;
  double weight = 0;
  GenericEdge parent = null;
  List<GenericEdge> edges;

  /**
   * Constructor for generic vertex.
   * @param name - the name of the vertex
   */
  public GenericVertex(String name) {
    this.name = name;
  }

  /**
   * Gets the name of the vertex.
   * @return the name of the vertex
   */
  public String getName() {
    return name;
  }

  @Override
  public void sameDst() {
    this.setParent(new GenericEdge(null, this, 0));
  }

  /**
   * Sets the edges that lead away from the vertex.
   * @param edges - the edges that lead away from the vertex
   */
  public void setEdges(List<GenericEdge> edges) {
    this.edges = edges;
  }

  /**
   * Gets the edges that lead away from the vertex.
   * @return the edges that lead away from the vertex
   */

  @Override
  public List<GenericEdge> explore() {
    return edges;
  }

  @Override
  public double getWeight() {
    return weight;
  }

  @Override
  public void setWeight(double weight) {
    this.weight = weight;
  }

  @Override
  public GenericEdge getParent() {
    return parent;
  }

  @Override
  public void setParent(GenericEdge parent) {
    this.parent = parent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GenericVertex that = (GenericVertex) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
