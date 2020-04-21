package edu.brown.cs.term_project.DemoGraph;

import java.util.Comparator;
import java.util.Map;

/**
 * Class comparing
 */
public class NodeComparator implements Comparator<DemoNode> {
  private Map<Integer, Double> distances;

  public NodeComparator(Map<Integer, Double> distances) {
    this.distances = distances;
  }
  @Override
  public int compare(DemoNode o1, DemoNode o2) {
    return Double.compare(distances.get(o1.getId()), distances.get(o2.getId()));
  }
}
