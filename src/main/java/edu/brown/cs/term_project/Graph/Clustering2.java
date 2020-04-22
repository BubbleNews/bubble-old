package edu.brown.cs.term_project.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Clustering2<T extends INode<S>, S extends IEdge<T>> {
  private Set<T> nodes;
  private List<S> edges;
  private double threshold;
  private double radiusThreshold;
  private Set<Cluster> clusters;
  private int count = 0;

  public Clustering2(Set<T> nodes, List<S> edges, double threshold) {
    this.nodes = nodes;
    this.edges = edges;
    this.threshold = threshold;
    this.clusters = new HashSet<>();
  }

  /**
   * Second version of clustering - This one uses some of the principles of K-Means clustering to find the optimal
   * number of clusters. Up to a max integer n, it tries to find n nodes that are all far apart, so that none of them
   * should probably be in the same cluster. It adds each of the remaining nodes to a cluster with the apart node
   * they are closest to. For each number of nodes it calculates the mean radius, and tries to find an "elbow point"
   * by maximizing the change in the slope between intervals of n. Then the clustering based on the optimal n is used
   * with a bit more elaborate method to get more accurate results.
   */
  public Set<Cluster> createClusters() {
    trimData(); // trims graph by removing nodes that have no edges in threshold, and edges that have no nodes left
    System.out.println("nodes_adj: " + nodes.size());
    System.out.println("edges_adj: " + edges.size());
    radiusThreshold = ClusterMethods.setRadiusThreshold(nodes); //sets radius threshold based on trimmed data
    System.out.println("radius_adj: " + radiusThreshold);
    int maxClusters = Math.min((int) Math.ceil(nodes.size()/2) + 1, 50); //experiment - max
    List<T> clusterNodes = OptimalClusterCount.findClusterNodes(nodes, edges, maxClusters);
    int numClusters = OptimalClusterCount.getOptimalCount(nodes, count, clusterNodes, maxClusters);
    // instead of picking just best cluster, could make a queue of top 5 or so, go from there
    List<Cluster> finalClusters = findClusters(clusterNodes, numClusters); // finds optimal clustering with numClusters
    for (Cluster c: finalClusters) {
      c.adjustHead(); // optimzes cluster head
      clusters.add(c); // adds cluster to graphs clusters
      System.out.println("Cluster.java: " + c.getHeadNode().getId() + " size: " + c.getSize());
      String toPrint = "";
      Set<T> clusterNodeSet = c.getNodes();
      for (T n: clusterNodeSet) { // prints clustering
        toPrint += n.getId();
        toPrint += " ";
      }
      System.out.println(toPrint);
    }
    return clusters;
  }


  /**
   * Method to trim data for CreateClusters2 method. Removes all nodes that have no edges below threshold and all
   * edges with either their source or destination deleted.
   */
  public void trimData() {
    edges = new ArrayList<>(edges); // creates alternative edges to modify
    nodes = new HashSet<>(nodes); // creates alternative nodes to modify
    edges.sort(new EdgeComparator());
    int size = edges.size();
    // temporarily removes edges above threshold
    List<S> edges2 = new ArrayList<>(edges.subList(0, (int)Math.floor(size * threshold)));
    Set<T> newNodes = new HashSet<>();
    for (S e: edges2) { // removes nodes that have no edges left after temporary removal
      newNodes.add(e.getSource());
      newNodes.add(e.getDest());
    }
    nodes = newNodes;
    List<S> newEdges = new ArrayList<>();
    for (S e: edges) { // removes edges that have one of their nodes removed
      if (nodes.contains(e.getSource()) && nodes.contains(e.getDest())) {
        newEdges.add(e);
      }
    }
    edges = newEdges;
  }


  /**
   * Method to optimally divide node into n clusters
   * @param clusterNodes - the head nodes to start each cluster with
   * @param n - the number of clusters to make
   * @return
   */
  public List<Cluster> findClusters(List<T> clusterNodes, Integer n) {
    List<Cluster> tempClusters = new ArrayList<>();
    Set<T> tempNodes = new HashSet<>(nodes);
    for (int i=0; i < n; i++) { //create n clusters each with one of the clusterNodes in it, add to arraylist
      T curr1 = clusterNodes.get(i);
      Set<T> tempSet = new HashSet<T>();
      tempSet.add(curr1);
      tempClusters.add(new Cluster(count, curr1, tempSet));
      count++;
      tempNodes.remove(curr1);
    }
    Map<Integer, double[]> nodeDistance = new HashMap<>();
    Map<Integer, Integer> minDistIndex = new HashMap<>();
    for (T n1: tempNodes) { //initialize an array for each node with the distance to each of the clusterNodes
      double[] distances = new double[n];
      double minDist = Integer.MAX_VALUE;
      int minIndex = -1;
      for (int i = 0; i < n; i++) {
        double dist = tempClusters.get(i).meanRadiusNode(n1);
        if (dist < minDist) {
          minDist = dist;
          minIndex = i;
        }
        distances[i] = dist;
        nodeDistance.put(n1.getId(), distances);
      }
      minDistIndex.put(n1.getId(), minIndex);
    }
    while (tempNodes.size() > 0) { // until tempNodes is empty, find tempNode with smallest distance to a cluster
      // add that node to the respective cluster
      double min = Integer.MAX_VALUE;
      T minNode = null;
      int minClusterIdx = -1;
      for (T n1: tempNodes) { // find node closest to a cluster by iterating through minDistIndex map
        // this finds the index of the shortest distance in the nodeDistance map.
        int id = n1.getId();
        int clusterIdx = minDistIndex.get(id);
        double dist = nodeDistance.get(id)[clusterIdx];
        if (dist < min) {
          min = dist;
          minNode = n1;
          minClusterIdx = clusterIdx;
        }
      }
      tempClusters.get(minClusterIdx).addNode(minNode);
      tempNodes.remove(minNode);
      for (T n1: tempNodes) { // update every array in nodeDistance map at the minClusterIdx with new distance
        int id = n1.getId();
        Cluster c = tempClusters.get(minClusterIdx);
        double tempDist = c.meanRadiusNode(n1)*Math.max(.8, 1.02-.02*c.getSize()); //experiment
        double[] distances = nodeDistance.get(id);
        distances[minClusterIdx] = tempDist;
        if (tempDist < distances[minDistIndex.get(id)]) { //if less than min distance, update the minDistIndex hashmap
          minDistIndex.replace(id, minClusterIdx);
        } else if (minDistIndex.get(id) == minClusterIdx) {
          // if old min was current cluster, but no longer is, find new min and update minDistIndex
          double minDist = Integer.MAX_VALUE;
          int minIndex = -1;
          for (int i = 0; i < n; i++) {
            double dist = tempClusters.get(i).meanRadiusNode(n1);
            if (dist < minDist) {
              minDist = dist;
              minIndex = i;
            }
          }
          minDistIndex.replace(id, minIndex);
        }
      }
    }
    return tempClusters;
  }

}
