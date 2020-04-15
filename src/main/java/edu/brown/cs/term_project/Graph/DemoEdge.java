package edu.brown.cs.term_project.Graph;

import edu.brown.cs.term_project.Bubble.Article;

public class DemoEdge implements IEdge {
  private DemoNode src;
  private DemoNode dst;
  private double distance;

  public DemoEdge(DemoNode src, DemoNode dst, double distance) {
    this.src= src;
    this.dst = dst;
    this.distance = distance;

  }


  @Override
  public DemoNode getDest() {
    return dst;
  }

  @Override
  public DemoNode getSource() {
    return src;
  }

  @Override
  public double getDistance() {
    return distance;
  }


}
