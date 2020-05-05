package edu.brown.cs.term_project.graph;


import java.util.Comparator;
import java.util.Map;

/**
 * Class comparing Articles based on the distance value in a hash map.
 * @param <N>
 */
public class NodeComparator<N extends INode> implements Comparator<N> {
  private Map<Integer, Double> distances;

  public NodeComparator(Map<Integer, Double> distances) {
    this.distances = distances;
  }

  @Override
  public int compare(N o1, N o2) {
    return Double.compare(distances.get(o1.getId()), distances.get(o2.getId()));
  }
}
