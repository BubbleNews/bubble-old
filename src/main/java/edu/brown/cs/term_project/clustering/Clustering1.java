package edu.brown.cs.term_project.clustering;

import edu.brown.cs.term_project.graph.EdgeComparator;
import edu.brown.cs.term_project.graph.IEdge;
import edu.brown.cs.term_project.graph.INode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for clustering method 1.
 * @param <T> the type of node in the graph
 * @param <S> the type of edge in the graph
 */
public class Clustering1<T extends INode<S>, S extends IEdge<T>> {
  private Set<T> nodes;
  private List<S> edges;
  private double threshold;
  private double radiusThreshold;
  private Set<Cluster<T, S>> clusters;
  private int count = 0;
  private final double minMaxMult = 1.3;
  private final double maxMaxMult = 2.6;
  private final double iterMaxMult = 0.1;
  private final double tightCluster = 0.5;

  /**
   * Constructor for clustering1.
   * @param nodes the set of nodes to be clustered
   * @param edges the set of edges
   * @param threshold the distance threshold above which not to consider edges
   */
  public Clustering1(Set<T> nodes, List<S> edges, double threshold) {
    this.nodes = nodes;
    this.edges = edges;
    this.threshold = threshold;
    this.clusters = new HashSet<>();
  }

  /**
   * Method to cluster - It sorts the edges by increasing weight and adds them in sequentially.
   * As it adds the edges in it determines whether the two nodes should be in the same cluster,
   * and if the nodes are already clustered, whether the two clusters should be combined.
   * @return - set of created clusters
   */
  public Set<Cluster<T, S>> createClusters() {
    trimData(); // trims graph by removing edges and nodes based on threshold
    System.out.println("nodes_adj: " + nodes.size());
    System.out.println("edges_adj: " + edges.size());
    //sets new radius threshold based on the updated graph
    radiusThreshold = ClusterMethods.setRadiusThreshold(nodes);
    System.out.println("radius_adj: " + radiusThreshold);
    Map<Integer, Cluster<T, S>> tempClusters = new HashMap<>(); // tracks nodes in clusters
    for (int i = 0; i < edges.size(); i++) {
      S curr = edges.get(i);
      T src = curr.getSource();
      T dst = curr.getDest();
      if (tempClusters.containsKey(src.getId()) && tempClusters.containsKey(dst.getId())) {
        // if both nodes of current edge are clustered, decides whether to combine clusters
        combine(tempClusters.get(src.getId()), tempClusters.get(dst.getId()), tempClusters);
      } else if (tempClusters.containsKey(src.getId())) {
        // if one node is cluster, determine whether to add other node
        add(tempClusters.get(src.getId()), dst, tempClusters);
      } else if (tempClusters.containsKey(dst.getId())) {
        add(tempClusters.get(dst.getId()), src, tempClusters);
      } else {
        // if none are clustered, create a cluster with both nodes
        Set<T> addNodes = new HashSet<T>();
        addNodes.add(src);
        addNodes.add(dst);
        Cluster newCluster = new Cluster(count, src, addNodes);
        if (newCluster.getAvgRadius() < radiusThreshold / 3.0) { //experiment
          tempClusters.put(src.getId(), newCluster);
          tempClusters.put(dst.getId(), newCluster);
          count++;
          System.out.println("Make: " + src.getId() + " : " + dst.getId());
        }
      }
    } // add cluster in tempClusters to clusters
    tempClusters.forEach((k, v) -> {
      if (!clusters.contains(v)) {
        v.adjustHead(); // optimize the head node of the cluster, to make headline most fitting
        clusters.add(v);
        System.out.println("Cluster.java: " + v.getHeadNode().getId() + " size: " + v.getSize());
        StringBuilder toPrint = new StringBuilder("");
        Set<T> clusterNodes = v.getNodes();
        for (T n : clusterNodes) { //prints out nodes in cluster
          toPrint.append(n.getId());
          toPrint.append(" ");
        }
        System.out.println(toPrint.toString());
      }
    });
    return clusters;
  }


  /**
   * Method to trim data for CreateClusters method. Removes all edges beyond threshold, and any
   * nodes without any edges remaining.
   */
  private void trimData() {
    edges = new ArrayList<>(edges); // creates alternative edges to modify
    nodes = new HashSet<>(nodes); // creates alternative nodes to modify
    edges.sort(new EdgeComparator()); // sorts edges by weight
    int size = edges.size();
    // removes edges above threshold
    edges = new ArrayList<>(edges.subList(0, (int) Math.floor(size * threshold)));
    Set<T> newNodes = new HashSet<>();
    for (S e : edges) { // removes nodes with no edges left
      newNodes.add(e.getSource());
      newNodes.add(e.getDest());
    }
    nodes = newNodes;
  }

  /**
   * Checks if node should be added to cluster, if it should adds it, and updates hash map.
   * @param c1             - cluster to see if node should be added to
   * @param node           - node
   * @param expandClusters - map from node id to cluster, must be updated if node is added
   */
  private void add(Cluster c1, T node, Map<Integer, Cluster<T, S>> expandClusters) {
    double oldMean = c1.getAvgRadius();
    double newMean = c1.meanRadiusNode(node);
    double maxMult = Math.max(maxMaxMult - (iterMaxMult * c1.getSize()), minMaxMult); //experiment
    double diff2 = newMean / (tightCluster * radiusThreshold); //experiment
    // checks if mean radius of new cluster is less than threshold, and the either new mean isn't
    // too much more than old mean or if the new mean is small enough to compensate for increasing
    // old mean significantly
    if (newMean < radiusThreshold && (newMean < maxMult * oldMean || diff2 < 1)) { //add to cluster
      c1.addNode(node);
      expandClusters.put(node.getId(), c1);
      System.out.println("added: " + node.getId() + " : "
          + c1.getHeadNode().getId() + " -> " + c1.getSize());
    } else {
      System.out.println("Not added: " + node.getId() + " : "
          + c1.getHeadNode().getId() + " -> " + c1.getSize());

    }
  }

  /**
   * Checks if two clusters should be combined and are not equal, if they should combine them
   * and update hashmap.
   * @param c1                - first cluster to check for combination
   * @param c2                - second cluster to check for combination
   * @param articleToClusters - map from node id to cluster, clusters for nodes from smaller
   *                          cluster must be updated if combined
   */
  private void combine(
      Cluster<T, S> c1, Cluster<T, S> c2, Map<Integer, Cluster<T, S>> articleToClusters) {
    double meanC1 = c1.getAvgRadius();
    double meanC2 = c2.getAvgRadius();
    double newMean = c1.meanRadiusClusters(c2);
    double maxMult1 =
        Math.min(maxMaxMult - (iterMaxMult * c1.getSize() / c2.getSize()), minMaxMult); //experiment
    double maxMult2 =
        Math.min(maxMaxMult - (iterMaxMult * c2.getSize() / c1.getSize()), minMaxMult); //experiment
    double diff2 = newMean / (tightCluster * radiusThreshold); //experiment
    // ensures mean radius of new cluster is less than threshold, and the either new mean isn't too
    // much more than both old means or if the new mean is small enough to compensate for
    // increasing old mean significantly
    if (!c1.equals(c2) && newMean < radiusThreshold
        && ((newMean < maxMult1 * meanC1 && newMean < maxMult2 * meanC2) || diff2 < 1)) {
      System.out.println("merging: " + c1.getSize() + " - " + c2.getSize());
      // finds smaller cluster, so combination can be more efficient
      if (c1.getSize() >= c2.getSize()) {
        c1.addNodes(c2);
        Set<T> clusterNodes = c2.getNodes();
        for (T a : clusterNodes) {
          articleToClusters.replace(a.getId(), c1);
        }
      } else {
        c2.addNodes(c1);
        Set<T> clusterNodes2 = c1.getNodes();
        for (T a : clusterNodes2) {
          articleToClusters.replace(a.getId(), c2);
        }
      }
      System.out.println("combined: " + c1.getHeadNode().getId() + " : "
          + c2.getHeadNode().getId() + " "
          + "-> " + c1.getSize() + "/" + c2.getSize());
    } else {
      System.out.println("Not combined: " + c1.getHeadNode().getId() + " : "
          + c2.getHeadNode().getId() + " "
          + "-> " + c1.getSize() + "/" + c2.getSize());
    }
  }
}
