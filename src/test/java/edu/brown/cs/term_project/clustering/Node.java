package edu.brown.cs.term_project.clustering;

import edu.brown.cs.term_project.graph.INode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node implements INode<Edge> {
  private int id;
  private List<Edge> edges = new ArrayList<>();

  public Node(int id) {
    this.id = id;
  }

  @Override
  public List<Edge> getEdges() {
    return edges;
  }

  @Override
  public Edge getEdge(INode<Edge> dst) {
    for (Edge e: edges) {
      if (e.getSource().equals(dst) || e.getDest().equals(dst)) {
        return e;
      }
    }
    return null;
  }

  public void setEdges(List<Edge> newEdges) {
    for (Edge edge: newEdges) {
      if (edge.getSource().equals(this) || edge.getDest().equals(this)) {
        this.edges.add(edge);
      }
    }

  }


  @Override
  public double getDistance(INode<Edge> dst) throws RuntimeException {
    if (dst.equals(this)) {
      return 0;
    } else {
      for (Edge e: edges) {
        if (e.getSource().equals(dst) || e.getDest().equals(dst)) {
          return e.getDistance();
        }
      }
      throw new RuntimeException("Edge not found.");
    }
  }

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return id == node.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
