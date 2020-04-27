package edu.brown.cs.term_project.Graph;

import java.util.Objects;
import java.util.Set;

public class Cluster<T extends INode<E>, E extends IEdge<T>> {
  private int id;
  private T headlineArticleVertex;
  private Integer size;
  private double avgConnections;
  private Set<T> articles;
  private double avgRadius;
  private double std;

  public Cluster(Integer id, T headlineArticleVertex, Set<T> articles) {
    this.id = id;
    this.headlineArticleVertex = headlineArticleVertex;
    this.articles = articles;
    this.size = articles.size();
    setAvgConnections();
    setAvgRadius();
    setStd();
  }
  
  public Integer getId() {
    return id;
  }
  
  public Set<T> getNodes() {
    return articles;
  }
  
  public Integer getSize() {
    return size;
  }
  
  public double getAvgConnections() {
    return avgConnections;
  }
  
  public double getAvgRadius() {
    return avgRadius;
  }
  
  public double getStd() {
    return std;
  }

  /**
   * Method to add node to the cluster.
   * @param node - node to add
   */
  public void addNode(T node) {
    this.articles.add(node);
    this.size++;
    setAvgConnections();
    this.avgRadius = meanRadiusNode(node);
    setStd();
  }

  /**
   * Method to add multiple nodes at once to a cluster.
   * @param cluster
   */
  public void addNodes(Cluster cluster) {
    this.articles.addAll(cluster.getNodes());
    this.size = articles.size();
    setAvgConnections();
    this.avgRadius = meanRadiusClusters(cluster);
    setStd();
  }

  public void adjustHead() {
  }

  public T getHeadNode() {
    return headlineArticleVertex;
  }

  /**
   * Method to calculate the average radius of a cluster.
   */
  public void setAvgRadius() {
    double sum = 0;
    int count = 0;
    for (T n1: articles) {
      for (T n2: articles) {
        if (!n1.equals(n2)) {
          sum += n1.getEdge(n2).getDistance();
          count++;
        }
      }
    }
    if (count > 0) {
      this.avgRadius = sum / count;
    } else {
      this.avgRadius = 0;
    }

  }

  public void setAvgConnections() {
    this.avgConnections = 0;
  }

  public void setStd() {
    this.std = 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cluster that = (Cluster) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  /**
   * Method to calculate the mean radius of the combination of this cluster and another.
   * @param cluster2 - cluster to combine
   * @return - double representing combined mean radius
   */
  public double meanRadiusClusters(Cluster cluster2) {
    double sum = 0;
    int count = 0;
    int size2 = cluster2.getNodes().size();
    int edgeCount1 = (size * (size - 1)) / 2;
    int edgeCount2 = (size2 * (size2 - 1)) / 2;
    sum += avgRadius * edgeCount1; //scaling this avgRadius by number of edges
    count += edgeCount1;
    sum += cluster2.getAvgRadius() * edgeCount2; //scaling cluster2's avgRadius by it's number of
    // edges
    count += edgeCount2;
    for (T n1: articles) {
      Set<T> clusterNodes = cluster2.getNodes();
      for (T n2: clusterNodes) { //adding in radius of edges between the two
        // clusters
        if (!n1.equals(n2)) { // there shouldn't be any overlap
          sum += n1.getDistance(n2);
          count++;
        }
      }
    }
    return sum / count;
  }

  /**
   * Method to calculate teh mean radius of the combination of this cluster and an Article.
   * @param n - article to combine
   * @return - double representing combined mean radius
   */
  public double meanRadiusNode(T n) {
    double sum = 0;
    int count = 0;
    int edgeCount1 = (size * (size - 1)) / 2;
    sum += avgRadius * edgeCount1;
    count += edgeCount1;
    for (T n1: articles) {
      if (!n1.equals(n)) { // there shouldn't be any overlap

        sum += n1.getDistance(n);
        count++;
      }
    }
    if (count > 0) {
      return sum / count;
    } else {
      return 0;
    }
  }
}
