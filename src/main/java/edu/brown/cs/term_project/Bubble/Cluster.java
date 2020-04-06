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

  private Cluster()
}
