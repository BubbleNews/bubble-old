package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.ICluster;

import java.util.Set;

public class Cluster implements ICluster<Article> {
  private String id;
  private Article headline;
  private Integer size;
  private double avgConnections;
  private Set<Article> articles;
  private double avgRadius;
  private double std;

  public Cluster(Article headline, Set<Article> articles) {
    this.headline = headline;
    this.articles = articles;
    this.size = articles.size();
    setAvgConnections();
    setAvgRadius();
    setStd();
  }

  //TODO: implement these three set functions

  public void setAvgConnections() {
    this.avgConnections = 0;
  }

  public void setAvgRadius() {
    this.avgRadius = 0;
  }

  public void setStd() {
    this.std = 0;
  }

  @Override
  public double getAvgConnections() {
    return avgConnections;
  }

  @Override
  public double getAvgRadius() {
    return avgRadius;
  }

  @Override
  public Integer getId() {
    return null;
  }

  public Set<Article> getNodes() {
    return articles;
  }

  @Override
  public double getStd() {
    return std;
  }

  @Override
  public void addNode(Article node) {

  }

  @Override
  public void adjustHead() {

  }

  @Override
  public Integer getSize() {
    return size;
  }

  @Override
  public Article getHeadNode() {
    return headline;
  }


}
