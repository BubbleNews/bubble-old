package edu.brown.cs.term_project.DemoGraph;

import edu.brown.cs.term_project.Graph.Graph;

import java.util.*;

public class DemoGraph extends Graph<DemoCluster, DemoNode, DemoEdge> {
  private Set<DemoNode> nodes;
  private List<DemoEdge> edges;
  private double threshold;
  private Set<DemoCluster> clusters;

  public DemoGraph() {
    super();
  }

  @Override
  public void createClusters() {
    setThreshold();
    edges.sort(new DemoComparator());
    Map<Integer, DemoCluster> clusters = new HashMap<>();
    for (int i = 0; i < edges.size()/threshold; i++) {
      DemoEdge curr = edges.get(i);
      DemoNode src = curr.getSource();
      DemoNode dst = curr.getDest();
      if (clusters.containsKey(src.getId()) && clusters.containsKey(dst.getId())) {
          combine(clusters.get(src.getId()), clusters.get(dst.getId()), clusters);
      } else if (clusters.containsKey(src.getId())) {
        add(clusters.get(src.getId()), dst, clusters);
      } else if (clusters.containsKey(dst.getId())) {
        add(clusters.get(dst.getId()), src, clusters);
      } else {
        Set<DemoNode> add_nodes = new HashSet<DemoNode>();
        add_nodes.add(src);
        add_nodes.add(dst);
        DemoCluster new_cluster = new DemoCluster(src, add_nodes);
        clusters.put(src.getId(), new_cluster);
        clusters.put(dst.getId(), new_cluster);
      }
    }

  }

  /**
   * Checks if node should be added to cluster, if it should adds it, and updates hash map
   * @param c1
   * @param node
   * @param clusters
   */
  public void add(DemoCluster c1, DemoNode node, Map<Integer, DemoCluster> clusters) {
  }

  /**
   * Checks if two clusters should be combined and are not equal, if they should combine them and update hashmap
   * @param c1
   * @param c2
   * @param clusters
   */
  public void combine(DemoCluster c1, DemoCluster c2, Map<Integer, DemoCluster> clusters) {
  }

  @Override
  public void setThreshold() {
    this.threshold = 0.2;
  }

  @Override
  public void CreateGraph(int size) {
    for (int i = 0; i < size; i++) {
      this.nodes.add(new DemoNode(i));
    }
    for (DemoNode a: nodes) {
      for (DemoNode b: nodes) {
        if (a.getId() < b.getId()) {
          this.edges.add(new DemoEdge(a, b, Math.random()));
        }
      }
    }
  }


}






