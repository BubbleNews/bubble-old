package edu.brown.cs.term_project.clustering;

import edu.brown.cs.term_project.graph.Graph;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Clustering2Test {
  private Graph<Node, Edge> graph;
  private List<Edge> edges = new ArrayList<>();
  Set<Node> nodes = new HashSet<>();
  private Node node1;
  private Node node2;
  private Node node3;
  private Node node4;
  private Node node5;
  private Node node6;
  private Set<Cluster<Node, Edge>> clusters;
  private double threshold;

  @Before
  public void setUp2() {
    node1 = new Node(1);
    node2 = new Node(2);
    node3 = new Node(3);
    node4 = new Node(4);
    node5 = new Node(5);
    node6 = new Node(6);
    edges.add(new Edge(node1, node2, 0.1));
    edges.add(new Edge(node1, node3, 32));
    edges.add(new Edge(node1, node4, 3.5));
    edges.add(new Edge(node1, node5, 8));
    edges.add(new Edge(node1, node6, 50));
    edges.add(new Edge(node2, node3, 1000000));
    edges.add(new Edge(node2, node4, 0.7));
    edges.add(new Edge(node2, node5, 300));
    edges.add(new Edge(node2, node6, 100));
    edges.add(new Edge(node3, node4, 170));
    edges.add(new Edge(node3, node5, 0.2));
    edges.add(new Edge(node3, node6, 1000000));
    edges.add(new Edge(node4, node5, 0.3));
    edges.add(new Edge(node4, node6, 1000000));
    edges.add(new Edge(node5, node6, 1000));
    node1.setEdges(edges);
    node2.setEdges(edges);
    node3.setEdges(edges);
    node4.setEdges(edges);
    node5.setEdges(edges);
    node6.setEdges(edges);
    nodes.add(node1);
    nodes.add(node2);
    nodes.add(node3);
    nodes.add(node4);
    nodes.add(node5);
    nodes.add(node6);
    graph = new Graph<>(nodes, edges);
    graph.runClusters(2);
    clusters = graph.getClusters();
    threshold = getRadiusThreshold(clusters);
  }


  public double getRadiusThreshold(Set<Cluster<Node, Edge>> clusters) {
    Set<Node> allNodes = new HashSet<>();
    for (Cluster<Node, Edge> c: clusters) {
      allNodes.addAll(c.getNodes());
    }
    return ClusterMethods.setRadiusThreshold(allNodes);
  }

  public boolean checkClusters(Set<Cluster<Node, Edge>> clusters) {
    for (Cluster<Node, Edge> c: clusters) {
      if (c.getAvgRadius() > threshold) {
        return false;
      }
    }
    return true;
  }



  @After
  public void tearDown() {
    graph = null;
    edges = null;
    nodes = null;
    node1 = null;
    node2 = null;
    node3 = null;
    node4 = null;
    node5 = null;
    node6 = null;

  }


  @Test
  public void testCluster2() {
    setUp2();
    for (Cluster<Node, Edge> c: clusters) {
      assertTrue(checkClusters(clusters));
    }
    tearDown();
  }


}