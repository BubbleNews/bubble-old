package edu.brown.cs.term_project.Graph;

import java.util.Comparator;

public class DemoComparator implements Comparator<DemoEdge> {

  @Override
  public int compare(DemoEdge o1, DemoEdge o2) {
    return Double.compare(o1.getDistance(), o2.getDistance());
  }
}
