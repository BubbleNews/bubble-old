package edu.brown.cs.term_project.DemoGraph;
import edu.brown.cs.term_project.Graph.INode;

import java.util.List;
import java.util.Objects;

public class DemoNode implements INode<DemoEdge> {
  private int id;
  private List<DemoEdge> edges;

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
    this.edges = edges;
  }

  // TODO: write get similarity

  public DemoEdge getEdge(INode<DemoEdge> dst) {
    return null;
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
