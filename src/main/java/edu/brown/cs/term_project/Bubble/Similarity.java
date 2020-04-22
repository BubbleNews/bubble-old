package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.IEdge;


public class Similarity implements IEdge<ArticleVertex> {
  private int id;
  private ArticleVertex src;
  private ArticleVertex dst;
  private double distance;

  public Similarity(int id, ArticleVertex src, ArticleVertex dst, double distance) {
    this.id = id;
    this.src = src;
    this.dst = dst;
    this.distance = distance;
  }

  public int getID() {
    return id;
  }

  @Override
  public ArticleVertex getDest() {
    return dst;
  }

  @Override
  public ArticleVertex getSource() {
    return src;
  }

  @Override
  public double getDistance() {
    return distance;
  }

}
