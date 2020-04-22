package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.IEdge;


public class Similarity implements IEdge<Article>, IConnection<Article> {
  private int id;
  private Article src;
  private Article dst;
  private double distance;

  public Similarity(int id, Article src, Article dst, double distance) {
    this.id = id;
    this.src= src;
    this.dst = dst;
    this.distance = distance;
  }

  public Similarity(Article src, Article dst, double distance) {
    this.src= src;
    this.dst = dst;
    this.distance = distance;
  }


  public int getID() {
    return id;
  }

  @Override
  public Article getDest() {
    return dst;
  }

  @Override
  public Article getSource() {
    return src;
  }

  @Override
  public double getDistance() {
    return distance;
  }

}
