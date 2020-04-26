package edu.brown.cs.term_project.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class OptimalClusterCount<T extends INode<S>, S extends IEdge<T>> {


  public static <T extends INode<S>, S extends IEdge<T>> int getOptimalCount(
      Set<T> nodes, Integer count, List<T> clusterNodes, Integer maxClusters) {
    double maxDiff = Integer.MIN_VALUE; // value to maximize to find optimal number of clusters
    int numClusters = 0;
    double prevRadius2 = ClusterMethods.meanRadiusSet(nodes) * nodes.size();
    double prevRadius1 = ClusterMethods.meanRadiusSet(nodes) * nodes.size();
    for (T d: clusterNodes) {
      System.out.print(d.getId() + " ");
    }
    System.out.println();
    System.out.println("Count: " + 1 + " - " + prevRadius1);
    final double minDenominator = 0.5;
    for (int i = 2; i <= maxClusters; i++) {
      List<Cluster<T, S>> tempClusters = approxFindClusters(nodes, count, clusterNodes, i); //
      // approximate clustering with i clusters
      double newRadius = ClusterMethods.totalRadius(tempClusters);
      double diff = (prevRadius2 - prevRadius1) / Math.max(prevRadius1 - newRadius, minDenominator);
      //experiment - diff formula
      System.out.println("Diff: " + (i - 1) + " - " + diff);
      if (diff > maxDiff) { // if diff is larger than maxDiff, update
        maxDiff = diff;
        numClusters = i - 1;
      }
      prevRadius2 = prevRadius1;
      prevRadius1 = newRadius;
    }
    return numClusters;
  }

  /**
   * Method to find n nodes that should not be in the same cluster.
   * @param nodes the nodes to cluster
   * @param edges the edges to cluster
   * @param n - number of nodes to find
   * @param <T> - type of node for cluster
   * @param <S> - type of edge for cluster
   * @return - List of T nodes that shouldn't be in the same cluster no matter what
   */
  public static <T extends INode<S>, S extends IEdge<T>> List<T> findClusterNodes(
      Set<T> nodes, List<S> edges, Integer n) {
    int tempCount = 1;
    List<T> clusterNodes = new ArrayList<>();
    while (tempCount < n) { //
      if (clusterNodes.size() == 0) { //if no articles found yet, add the two with the farthest edge
        edges.sort(new EdgeComparator<>());
        Collections.reverse(edges);
        S edge = edges.get(0);
        clusterNodes.add(edge.getSource());
        clusterNodes.add(edge.getDest());
      } else {
        // find node that has the largest minimum distance to any clusterNodes
        double max = Integer.MIN_VALUE;
        T currMax = null;
        for (T node1: nodes) {
          double tempMin = Integer.MAX_VALUE;
          for (T node2: clusterNodes) {
            tempMin = Math.min(tempMin, node1.getDistance(node2));
          }
          if (tempMin > max) {
            max = tempMin;
            currMax = node1;
          }
        }
        clusterNodes.add(currMax);
      }
      tempCount++;
    }
    return clusterNodes;
  }

  /**
   * Method to find an approximate best clustering with n clusters and one of  clusterNodes in
   * each cluster. The method is a faster and less accurate version of findClusters, since it
   * doesn't add nodes to clusters in.
   * sequential order from smallest to largest distance
   * @param clusterNodes - nodes to start each cluster with
   * @param n - number of clusters
   * @param nodes - the nodes to cluster
   * @param count - the number of clusters to find
   * @param <T> - the type of node
   * @param <S> - the type of edge
   * @return - List of approximate clustering with n clusters
   */
  public static <T extends INode<S>, S extends IEdge<T>> List<Cluster<T, S>> approxFindClusters(
      Set<T> nodes, Integer count, List<T> clusterNodes, Integer n) {
    List<Cluster<T, S>> tempClusters = new ArrayList<>();
    List<T> tempNodes = new ArrayList<>(nodes);
    for (int i = 0; i < n; i++) { //create n clusters each with one of the clusterNodes in it, add
      // to arraylist
      T curr1 = clusterNodes.get(i);
      Set<T> tempSet = new HashSet<>();
      tempSet.add(curr1);
      tempClusters.add(new Cluster<>(count, curr1, tempSet));
      count++;
      tempNodes.remove(curr1);
    }
    Map<Integer, Double> minDistance = new HashMap<>();
    for (T n1: tempNodes) { //initialize array for nodes with the distance to each clusterNode
      double minDist = Integer.MAX_VALUE;
      for (int i = 0; i < n; i++) {
        double dist = tempClusters.get(i).meanRadiusNode(n1);
        if (dist < minDist) {
          minDist = dist;
        }
      }
      minDistance.put(n1.getId(), minDist);
    }
    tempNodes.sort(new NodeComparator<>(minDistance)); // sorts  by distance to initial clusters
    final double maxSizeBenefit = 0.8;
    final double iterSizeBenefit = 0.02;
    // an approximation, since it doesn't take into consideration when the clusters add nodes
    for (T n1: tempNodes) { // for node, finds closest cluster (including nodes already clustered)
      double min = Integer.MAX_VALUE;
      Cluster<T, S> minCluster = null;
      for (Cluster<T, S> c: tempClusters) {
        // slight advantage to clusters with more nodes as meanRadius will likely be larger than min
        double dist = c.meanRadiusNode(n1)
            * Math.max(maxSizeBenefit, 1 - iterSizeBenefit * (c.getSize() - 1));
        //experiment
        if (dist < min) {
          min = dist;
          minCluster = c;
        }
      }
      minCluster.addNode(n1);
    }
    return tempClusters;
  }
}
