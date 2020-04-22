package edu.brown.cs.term_project.Graph;

import java.util.Collection;
import java.util.Set;

public final class ClusterMethods {

  /**
   * Method to calculate the "total Radius" of a group of clusters. This is not actually the total radius, it is
   * the mean radius multiplied by number of nodes (true total radius would be mean radius times edge count).
   * It isn't true total Radius, since that would advantage more clusters
   * (smaller cluster sizes decrease intra-cluster edge counts).
   * @param currClusters - Collection of clusters to find total radius of
   * @return - double representing sum of average radius times node count of each cluster
   */
  public static double totalRadius(Collection<Cluster> currClusters) {
    double sum = 0;
    for (Cluster c: currClusters) {
      sum += c.getAvgRadius() * c.getSize();
    }
    return sum;
  }

  /**
   * Finds mean radius (edge weight) of all connections of Articles in a Collection.
   * @param nodes1 - collection of nodes to find mean radius of
   * @param <T>
   * @return
   */
  public static <T extends INode> double meanRadiusSet(Collection<T> nodes1) {
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
   * Sets the radius threshold for clusters based on both the normal mean of graph and specifics of this graph.
   * @param articles articles to set radius threshold for
   * @param <T>
   */
  public static <T extends INode> double setRadiusThreshold(Set<T> articles) {
    double graphMeanRadius = ClusterMethods.meanRadiusSet(articles);
    System.out.println("mean Radius: " + graphMeanRadius);
    double normalGraphMeanRadius = .5; //experiment
    double percentCurrent = .5; //experiment with this value -- how much to use current graph in threshold
    double percentFullGraph = 1.2; //experiment -- how many times tighter clusters should be than normal graph
    return (percentCurrent * graphMeanRadius + (1 - percentCurrent) * normalGraphMeanRadius)/ percentFullGraph;
  }
}
