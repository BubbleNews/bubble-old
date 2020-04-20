package edu.brown.cs.term_project.DemoGraph;
import edu.brown.cs.term_project.Graph.INode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DemoNode implements INode<DemoEdge> {
  private int id;
  private List<DemoEdge> edges = new ArrayList<>();

  public DemoNode(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Override
  public List<DemoEdge> getEdges() {
    return edges;
  }

  public void setEdges(List<DemoEdge> edges) {
    for (int i = 0; i < edges.size(); i++) {
      if (edges.get(i).getSource().equals(this) || edges.get(i).getDest().equals(this)) {
        this.edges.add(edges.get(i));
      }
    }
  }

  // TODO: write get similarity

  public DemoEdge getEdge(INode<DemoEdge> dst) {
    for (DemoEdge e: edges) {
      if (e.getSource().equals(dst) || e.getDest().equals(dst)) {
        return e;
      }
    }
    return null;
  }

  public double getDistance(INode<DemoEdge> dst) {
    if (dst.equals(this)) {
      return 0;
    } else {
      for (DemoEdge e: edges) {
        if (e.getSource().equals(dst) || e.getDest().equals(dst)) {
          return e.getDistance();
        }
      }
      return -1;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DemoNode demoNode = (DemoNode) o;
    return id == demoNode.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
