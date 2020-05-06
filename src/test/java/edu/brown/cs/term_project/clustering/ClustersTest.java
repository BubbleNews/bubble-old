package edu.brown.cs.term_project.clustering;

import edu.brown.cs.term_project.clustering.Cluster;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import edu.brown.cs.term_project.clustering.Edge;
import edu.brown.cs.term_project.clustering.Node;

import java.util.ArrayList;
import java.util.List;


public class ClustersTest {
  private Cluster cluster;
  private List<Edge> edges = new ArrayList<>();
  private Node node1;
  private Node node2;
  private Node node3;
  private Node node4;
  private Node node5;
  private Node node6;

  @Before
  public void setUp() {
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
    edges.add(new Edge(node4, node4, 0.3));
    edges.add(new Edge(node5, node5, 1000000));
    edges.add(new Edge(node5, node6, 1000));
    node1.setEdges(edges);
    node2.setEdges(edges);
    node3.setEdges(edges);
    node4.setEdges(edges);
    node5.setEdges(edges);
    node6.setEdges(edges);
  }


  @After
  public void tearDown() {

  }

  @Test
  public void test1() {
    setUp();
    tearDown();
  }

  @Test
  public void test2() {
    setUp();
    tearDown();
  }


}