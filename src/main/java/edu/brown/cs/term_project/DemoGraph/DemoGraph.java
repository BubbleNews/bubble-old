package edu.brown.cs.term_project.DemoGraph;

import edu.brown.cs.term_project.Graph.Graph;
import org.w3c.dom.Node;

import java.util.*;

public class DemoGraph extends Graph<DemoCluster, DemoNode, DemoEdge> {
  private Set<DemoNode> nodes;
  private List<DemoEdge> edges;
  private double threshold;
  private double radiusThreshold;
  private Set<DemoCluster> clusters;
  private Integer count = 0;

  public DemoGraph() {
    super();
    this.nodes = new HashSet<DemoNode>();
    this.edges = new ArrayList<DemoEdge>();
    this.clusters = new HashSet<>();
    createGraph(100);
    System.out.println("nodes: " + nodes.size());
    System.out.println("edges: " + edges.size());
    setThreshold();
    setRadiusThreshold();
    System.out.println("Radius Now: " + radiusThreshold);
  }

  @Override
  public void createClusters() {
    trimData();
    System.out.println("nodes_adj: " + nodes.size());
    System.out.println("edges_adj: " + edges.size());
    for (DemoEdge e: edges) {
      System.out.println(e.getSource().getId() + " -> " + e.getDest().getId() + ": " + e.getDistance());
    }
    setRadiusThreshold();
    System.out.println("radius_adj: " + radiusThreshold);
    Map<Integer, DemoCluster> tempClusters = new HashMap<>();
    for (int i = 0; i < edges.size(); i++) {
      System.out.println(edges.get(i).getSource().getId() + " -> " + edges.get(i).getDest().getId());
      DemoEdge curr = edges.get(i);
      DemoNode src = curr.getSource();
      DemoNode dst = curr.getDest();
      if (tempClusters.containsKey(src.getId()) && tempClusters.containsKey(dst.getId())) {
          combine(tempClusters.get(src.getId()), tempClusters.get(dst.getId()), tempClusters);
      } else if (tempClusters.containsKey(src.getId())) {
        add(tempClusters.get(src.getId()), dst, tempClusters);
      } else if (tempClusters.containsKey(dst.getId())) {
        add(tempClusters.get(dst.getId()), src, tempClusters);
      } else {
        Set<DemoNode> add_nodes = new HashSet<DemoNode>();
        add_nodes.add(src);
        add_nodes.add(dst);
        DemoCluster new_cluster = new DemoCluster(count, src, add_nodes);
        tempClusters.put(src.getId(), new_cluster);
        tempClusters.put(dst.getId(), new_cluster);
        count++;
      }
    }
    tempClusters.forEach((k, v) -> {
      if (!clusters.contains(v)) {
        v.adjustHead();
        clusters.add(v);
        System.out.println("Cluster: " + v.getHeadNode().getId() + " size: " + v.getSize());
        String toPrint = "";
        for(DemoNode n: v.getNodes()) {
          toPrint += n.getId();
          toPrint += " ";
        }
        System.out.println(toPrint);
      }
    });
  }

  public void createClusters2() {
    trimData2();
    System.out.println("nodes_adj: " + nodes.size());
    System.out.println("edges_adj: " + edges.size());
//    for (DemoEdge e: edges) {
//      System.out.println(e.getSource().getId() + " -> " + e.getDest().getId() + ": " + e.getDistance());
//    }
    setRadiusThreshold();
    System.out.println("radius_adj: " + radiusThreshold);
    int maxClusters = (int)Math.ceil(nodes.size()/2) + 1;
    List<DemoNode> clusterNodes = findClusterNodes(maxClusters);
    double maxDiff = Integer.MIN_VALUE;
    int numClusters = 0;
    double prevRadius2 = meanRadiusSet(nodes)*edges.size();
    double prevRadius1 = meanRadiusSet(nodes)*edges.size();;
    for (DemoNode d: clusterNodes) {
      System.out.print(d.getId() + " ");
    }
    System.out.println();
    System.out.println("Count: " + 1 + " - " + prevRadius1);
    for (int i=2; i <= maxClusters; i++) {
      Set<DemoCluster> tempClusters = findClusters(clusterNodes, i);
      double newRadius = totalRadius(tempClusters);
      //System.out.println("Count: " + i + " - " + newRadius);
      double diff = (prevRadius2 - prevRadius1)/Math.max(prevRadius1 - newRadius, 0.5); //experiment
      System.out.println("Diff: " + (i - 1) + " - " + diff);
      if (diff > maxDiff) {
        maxDiff = diff;
        numClusters = i - 1;
      }
      prevRadius2 = prevRadius1;
      prevRadius1 = newRadius;
    }
    Set<DemoCluster> finalClusters = findClusters(clusterNodes, numClusters);
    for (DemoCluster c: finalClusters) {
      c.adjustHead();
      clusters.add(c);
      System.out.println("Cluster: " + c.getHeadNode().getId() + " size: " + c.getSize());
      String toPrint = "";
      for(DemoNode n: c.getNodes()) {
        toPrint += n.getId();
        toPrint += " ";
      }
      System.out.println(toPrint);
    }
  }


  public void trimData() {
    edges.sort(new DemoComparator());
    int size = edges.size();
    edges = new ArrayList<>(edges.subList(0, (int)Math.floor(size * threshold)));
    Set<DemoNode> newNodes = new HashSet<>();
    for (DemoEdge e: edges) {
      newNodes.add(e.getSource());
      newNodes.add(e.getDest());
    }
    nodes = newNodes;
  }

  public void trimData2() {
    edges.sort(new DemoComparator());
    int size = edges.size();
    List<DemoEdge> edges2 = new ArrayList<>(edges.subList(0, (int)Math.floor(size * threshold)));
    Set<DemoNode> newNodes = new HashSet<>();
    for (DemoEdge e: edges2) {
      newNodes.add(e.getSource());
      newNodes.add(e.getDest());
    }
    nodes = newNodes;
    List<DemoEdge> newEdges = new ArrayList<>();
    for (DemoEdge e: edges) {
      if (nodes.contains(e.getSource()) && nodes.contains(e.getDest())) {
        newEdges.add(e);
      }
    }
    edges = newEdges;
  }

  public List<DemoNode> findClusterNodes(Integer n) {
    int count = 0;
    List<DemoNode> clusterNodes = new ArrayList<>();
    while (count < n) {
      if (clusterNodes.size() == 0) {
        edges.sort(new DemoComparator());
        Collections.reverse(edges);
        DemoEdge edge = edges.get(0);
        clusterNodes.add(edge.getSource());
        clusterNodes.add(edge.getDest());
      } else {
        double max = Integer.MIN_VALUE;
        DemoNode currMax = null;
        for (DemoNode node1: nodes) {
          double tempMin = Integer.MAX_VALUE;
          for (DemoNode node2: clusterNodes) {
            tempMin = Math.min(tempMin, node1.getDistance(node2));
          }
          if (tempMin > max) {
            max = tempMin;
            currMax = node1;
          }
        }
        clusterNodes.add(currMax);
      }
      count++;
    }
    return clusterNodes;
  }

  public Set<DemoCluster> findClusters(List<DemoNode> clusterNodes, Integer n) {
    Set<DemoCluster> tempClusters = new HashSet<>();
    Set<DemoNode> tempNodes = new HashSet<>(nodes);
    for (int i=0; i < n; i++) {
      DemoNode curr1 = clusterNodes.get(i);
      Set<DemoNode> tempSet = new HashSet<DemoNode>();
      tempSet.add(curr1);
      tempClusters.add(new DemoCluster(count, curr1, tempSet));
      count++;
      tempNodes.remove(curr1);
    }
    while (tempNodes.size() > 0) {
      double min = Integer.MAX_VALUE;
      DemoNode minNode = null;
      DemoCluster minCluster = null;
      for (DemoNode n1: tempNodes) {
        for (DemoCluster c: tempClusters) {
          double dist = meanRadiusNode(c, n1)*Math.max(.8, 1.02-.02*c.getSize()); //experiment
          if (dist < min) {
            min = dist;
            minNode = n1;
            minCluster = c;
          }
        }
      }
      minCluster.addNode(minNode);
      tempNodes.remove(minNode);
    }
    return tempClusters;
  }

  public double totalRadius(Set<DemoCluster> currClusters) {
    double sum = 0;
    for (DemoCluster c: currClusters) {
      sum += c.getAvgRadius()*(c.getSize()*(c.getSize()-1))/2;
    }
    return sum;
  }

  public double meanRadiusClusters(DemoCluster cluster1, DemoCluster cluster2) {
      double sum = 0;
      int count = 0;
      int size1 = cluster1.getNodes().size();
      int size2 = cluster2.getNodes().size();
      int edgeCount1 = (size1*(size1-1))/2;
      int edgeCount2 = (size2*(size2-1))/2;
      sum += cluster1.getAvgRadius()*edgeCount1;
      count += edgeCount1;
      sum += cluster2.getAvgRadius()*edgeCount2;
      count += edgeCount2;
      for (DemoNode n1: cluster1.getNodes()) {
        for (DemoNode n2: cluster2.getNodes()) {
          if (!n1.equals(n2)) { // there shouldn't be any overlap
            sum += n1.getDistance(n2);
            count++;
          }
        }
      }
      return sum/count;
  }

  public double meanRadiusNode(DemoCluster cluster1, DemoNode n) {
    double sum = 0;
    int count = 0;
    int size1 = cluster1.getNodes().size();
    int edgeCount1 = (size1*(size1-1))/2;
    sum += cluster1.getAvgRadius()*edgeCount1;
    count += edgeCount1;
    for (DemoNode n1: cluster1.getNodes()) {
        if (!n1.equals(n)) { // there shouldn't be any overlap

          sum += n1.getDistance(n);
          count++;
        }
      }
    if (count > 0) {
      return sum/count;
    } else {
      return 0;
    }
  }


  public double meanRadiusSet(Set<DemoNode> nodes1) {
    double sum = 0;
    int count = 0;
    for (DemoNode n1: nodes1) {
      for (DemoNode n2: nodes1) {
        if (!n1.equals(n2)) {
          sum += n1.getDistance(n2);
          count++;
        }
      }
    }
    if (count == 0) {
      return 0;
    } else {
      return sum/count;
    }
  }

  /**
   * Checks if node should be added to cluster, if it should adds it, and updates hash map
   * @param c1
   * @param node
   * @param clusters
   */
  public void add(DemoCluster c1, DemoNode node, Map<Integer, DemoCluster> clusters) {
    double oldMean = c1.getAvgRadius();
    double newMean = meanRadiusNode(c1, node);
    double maxMult = Math.max(2 - (0.05 * c1.getSize()), 1.3); //experiment with this
    double diff2 = newMean/(0.5*radiusThreshold); //experiment
    if (newMean < radiusThreshold && (newMean < maxMult*oldMean || diff2 < 1)) { //add to cluster
      c1.addNode(node);
      clusters.put(node.getId(), c1);
    }
  }

  /**
   * Checks if two clusters should be combined and are not equal, if they should combine them and update hashmap
   * @param c1
   * @param c2
   * @param clusters
   */
  public void combine(DemoCluster c1, DemoCluster c2, Map<Integer, DemoCluster> clusters) {
    double meanC1 = c1.getAvgRadius();
    double meanC2 = c2.getAvgRadius();
    double newMean = meanRadiusClusters(c1, c2);
    double maxMult1 = Math.min(2 - (0.05 * c1.getSize()/c2.getSize()), 1.3); //experiment with this
    double maxMult2 = Math.min(2 - (0.05 * c2.getSize()/c2.getSize()), 1.3); //experiment with this
    double diff2 = newMean/(0.5*radiusThreshold); //experiment
    if (!c1.equals(c2) && newMean < radiusThreshold &&
        ((newMean < maxMult1*meanC1 && newMean < maxMult2*meanC2) || diff2 < 1)) {
      Set<DemoNode> merged = new HashSet<>();
      merged.addAll(c1.getNodes());
      merged.addAll(c2.getNodes());
      DemoCluster newCluster = new DemoCluster(count, c1.getHeadNode(), merged);
      count++;
      for (DemoNode a: merged) {
        clusters.replace(a.getId(), newCluster);
      }
    } // Could have an else that decides a better split of the clusters
  }

  @Override
  public void setThreshold() {
    this.threshold = 0.2;
  }

  public void setRadiusThreshold() {
    double graphMeanRadius = meanRadiusSet(nodes);
    System.out.println("mean Radius: " + graphMeanRadius);
    double normalGraphMeanRadius = 1.6; //update this value
    double percentCurrent = .5; //experiment with this value -- how much to use current graph in threshold
    double percentFullGraph = 1.5; //experiment -- how many times tighter clusters should be than normal graph
    this.radiusThreshold = (percentCurrent*graphMeanRadius + (1-percentCurrent)*normalGraphMeanRadius)/percentFullGraph;
  }

  public void createGraph(int size) {
    for (int i = 0; i < size; i++) {
      DemoNode node = new DemoNode(i);
      this.nodes.add(node);
    }
    for (DemoNode a: nodes) {
      for (DemoNode b: nodes) {
        if (a.getId() < b.getId()) {
          this.edges.add(new DemoEdge(a, b, Math.random())); // + Math.abs(Math.sin(a.getId())) + Math.abs(Math.sin(b.getId()))));
        }
      }
    }
    for (DemoNode a: nodes) {
      a.setEdges(edges);
    }
  }


}






