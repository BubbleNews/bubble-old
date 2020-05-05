package edu.brown.cs.term_project.graph;

import edu.brown.cs.term_project.clustering.Cluster;
import edu.brown.cs.term_project.clustering.ClusterMethods;
import edu.brown.cs.term_project.clustering.Clustering1;
import edu.brown.cs.term_project.clustering.Clustering2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph<T extends INode<S>, S extends IEdge<T>> {
  private Set<T> nodes;
  private List<S> edges;
  private double threshold;
  private Set<Cluster<T, S>> clusters;

  /**
   * constructor which creates a graph from articles and their edges.
   * @param nodes - Set of Articles for graph
   * @param edges - list of edges of graph
   */
  public Graph(Set<T> nodes, List<S> edges) {
    super();
    this.nodes = nodes;
    this.edges = edges;
    this.clusters = new HashSet<>();
    System.out.println("nodes: " + nodes.size());
    System.out.println("edges: " + edges.size());
    // set default threshold so twice as many edges as nodes
    this.threshold = Math.min(1.0 * nodes.size() / edges.size(), 1);
    System.out.println("thresh: " + threshold);
    ClusterMethods.setRadiusThreshold(nodes);
  }

  public void runClusters(Integer method) {
    if (method == 1) {
      System.out.println("Clustering Method 1");
      Clustering1<T, S> c1 = new Clustering1<>(new HashSet<>(nodes), new ArrayList<>(edges),
          threshold);
      clusters = c1.createClusters();
      System.out.println("NUMBER: " + clusters.size());
    } else if (method == 2) {
      Clustering2<T, S> c2 = new Clustering2<>(new HashSet<>(nodes), new ArrayList<>(edges),
          threshold);
      clusters = c2.createClusters();
    }
  }

  /**
   * Sets the threshold (precent) for which edges to keep from the original graph.
   *
   * @param threshold the new threshold
   */
  public void setThreshold(double threshold) {
    this.threshold = threshold;
  }

  public Set<Cluster<T, S>> getClusters() {
    return clusters;
  }
}
