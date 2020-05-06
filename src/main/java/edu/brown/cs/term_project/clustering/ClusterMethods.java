package edu.brown.cs.term_project.clustering;

import edu.brown.cs.term_project.graph.IEdge;
import edu.brown.cs.term_project.graph.INode;

import java.util.Collection;
import java.util.Set;

public final class ClusterMethods {

  /**
   * Method to calculate the "total Radius" of a group of clusters. This is not actually the
   * total radius, it is the mean radius multiplied by number of nodes (true total radius would
   * be mean radius times edge count). It isn't true total Radius, since that would advantage
   * more clusters
   * (smaller cluster sizes decrease intra-cluster edge counts).
   * @param currClusters - Collection of clusters to find total radius of
   * @param <T> - type of node used
   * @param <S> - type of edge used
   * @return - double representing sum of average radius times node count of each cluster
   */
  public static <T extends INode<S>, S extends IEdge<T>> double totalRadius(Collection<Cluster<T,
      S>> currClusters) {
    double sum = 0;
    for (Cluster<T, S> c: currClusters) {
      sum += c.getAvgRadius() * c.getSize();
    }
    return sum;
  }

  /**
   * Finds mean radius (edge weight) of all connections of Articles in a Collection.
   * @param nodes1 - collection of nodes to find mean radius of
   * @param <T> - type of Node used
   * @param <S> - type of edge used
   * @return mean radius of set
   */
  public static <T extends INode<S>, S extends IEdge<T>> double
          meanRadiusSet(Collection<T> nodes1) {
    double sum = 0;
    int count = 0;
    for (T n1: nodes1) {
      for (T n2: nodes1) {
        if (!n1.equals(n2)) {
          sum += n1.getDistance(n2);
          count++;
        }
      }
    }
    if (count == 0) {
      return 0;
    } else {
      return sum / count;
    }
  }


  /**
   * Sets the radius threshold for clusters based on both the normal mean of graph and specifics
   * of this graph.
   * @param articles articles to set radius threshold for
   * @param <T> - type of node used
   * @param <S> - type of edge used
   * @return radius threshold of graph
   */
  public static <T extends INode<S>, S extends IEdge<T>> double
            setRadiusThreshold(Set<T> articles) {
    double graphMeanRadius = ClusterMethods.meanRadiusSet(articles);
    System.out.println("mean Radius: " + graphMeanRadius);
    final double normalGraphMeanRadius = 11; //experiment
    final double percentCurrent = 1; //experiment - how much to use current graph in threshold
    final double percentFullGraph = 1000; //experiment -- how many times tighter clusters should be
    // than normal graph
    return (percentCurrent * graphMeanRadius + (1 - percentCurrent) * normalGraphMeanRadius)
        / percentFullGraph;
  }
}
