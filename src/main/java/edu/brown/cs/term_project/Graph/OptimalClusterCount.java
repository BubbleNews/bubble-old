package edu.brown.cs.term_project.Graph;

import java.util.*;

public class OptimalClusterCount<T extends INode<S>, S extends IEdge<T>> {


  public static <T extends INode<S>, S extends IEdge<T>> int getOptimalCount(Set<T> nodes, Integer count, List<T> clusterNodes, Integer maxClusters) {
    double maxDiff = Integer.MIN_VALUE; // value to maximize to find optimal number of clusters
    int numClusters = 0;
    double prevRadius2 = ClusterMethods.meanRadiusSet(nodes) * nodes.size();
    double prevRadius1 = ClusterMethods.meanRadiusSet(nodes) * nodes.size();
    for (T d: clusterNodes) {
      System.out.print(d.getId() + " ");
    }
    System.out.println();
    System.out.println("Count: " + 1 + " - " + prevRadius1);
    for (int i = 2; i <= maxClusters; i++) {
      List<Cluster> tempClusters = approxFindClusters(nodes, count, clusterNodes, i); // approximate clustering with i clusters
      double newRadius = ClusterMethods.totalRadius(tempClusters);
      double diff = (prevRadius2 - prevRadius1) / Math.max(prevRadius1 - newRadius, 0.5);
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
   * Method to find n nodes that should not be in the same cluster
   * @param n - number of nodes to find
   * @return - List of T nodes that shouldn't be in the same cluster no matter what
   */
  public static <T extends INode<S>, S extends IEdge<T>> List<T> findClusterNodes(Set<T> nodes, List<S> edges, Integer n) {
    int tempCount = 1;
    List<T> clusterNodes = new ArrayList<>();
    while (tempCount < n) { //
      if (clusterNodes.size() == 0) { //if no articles found yet, add the two with the farthest edge
        edges.sort(new EdgeComparator());
        Collections.reverse(edges);
        S edge = edges.get(0);
        clusterNodes.add(edge.getSource());
        clusterNodes.add(edge.getDest());
      } else { // if clusterNodes isn't empty, find node that has the largest minimum distance to any clusterNodes
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
   * Method to find an approximate best clustering with n clusters and one of clusterNodes in each cluster
   * The method is a faster and less accurate version of findClusters, since it doesn't add nodes to clusters in
   * sequential order from smallest to largest distance
   * @param clusterNodes - nodes to start each cluster with
   * @param n - number of clusters
   * @return - List of approximate clustering with n clusters
   */
  public static <T extends INode<S>, S extends IEdge<T>> List<Cluster> approxFindClusters(Set<T> nodes, Integer count, List<T> clusterNodes, Integer n) {
    List<Cluster> tempClusters = new ArrayList<>();
    List<T> tempNodes = new ArrayList<>(nodes);
    for (int i=0; i < n; i++) { //create n clusters each with one of the clusterNodes in it, add to arraylist
      T curr1 = clusterNodes.get(i);
      Set<T> tempSet = new HashSet<T>();
      tempSet.add(curr1);
      tempClusters.add(new Cluster(count, curr1, tempSet));
      count++;
      tempNodes.remove(curr1);
    }
    Map<Integer, Double> minDistance = new HashMap<>();
    for (T n1: tempNodes) { //initialize an array for each node with the distance to each of the clusterNodes
      double minDist = Integer.MAX_VALUE;
      for (int i = 0; i < n; i++) {
        double dist = tempClusters.get(i).meanRadiusNode(n1);
        if (dist < minDist) {
          minDist = dist;
        }
      }
      minDistance.put(n1.getId(), minDist);
    }
    tempNodes.sort(new NodeComparator(minDistance)); // sorts articles by distance to initial clusters
    // this is an approximation, since it doesn't take into consideration when the clusters add nodes
    for (T n1: tempNodes) { // for node, finds closest cluster (including nodes already clustered from this loop)
      double min = Integer.MAX_VALUE;
      Cluster minCluster = null;
      for (Cluster c: tempClusters) {
        // gives slight advantage to clusters with more nodes since meanRadius will likely be larger than min
        double dist = c.meanRadiusNode(n1) * Math.max(.8, 1.02-.02*c.getSize()); //experiment
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
