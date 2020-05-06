package edu.brown.cs.term_project.clustering;

import edu.brown.cs.term_project.graph.IEdge;

public class Edge implements IEdge<Node> {
  private Node src;
  private Node dest;
  private double distance;

  public Edge(Node src, Node dest, double distance) {
    this.src = src;
    this.dest = dest;
    this.distance = distance;
  }

  @Override
  public Node getSource() {
    return src;
  }

  @Override
  public Node getDest() {
    return dest;
  }

  @Override
  public double getDistance() {
    return distance;
  }
}
