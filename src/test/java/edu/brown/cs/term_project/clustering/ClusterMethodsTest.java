package edu.brown.cs.term_project.clustering;

import edu.brown.cs.term_project.graph.Graph;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ClusterMethodsTest {
  private Graph<Node, Edge> graph;
  private List<Edge> edges = new ArrayList<>();
  Set<Node> nodes1 = new HashSet<>();
  Set<Node> nodes2 = new HashSet<>();
  Set<Node> nodes = new HashSet<>();
  private Node node1;
  private Node node2;
  private Node node3;
  private Node node4;
  private Node node5;
  private Node node6;


  @Before
  public void setUp1() {
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
    nodes1.add(node1);
    nodes1.add(node2);
    nodes1.add(node3);
    nodes2.add(node4);
    nodes2.add(node5);
    nodes2.add(node6);
    nodes.addAll(nodes1);
    nodes.addAll(nodes2);
    graph = new Graph<>(nodes, edges);
    graph.runClusters(1);
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
  public void testMeanRadiusClusters() {
    setUp1();
    assertEquals(ClusterMethods.meanRadiusSet(nodes), 200110.9867, .01);
    tearDown();
  }



}
