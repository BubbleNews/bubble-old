package edu.brown.cs.term_project.Graph;



import java.util.Comparator;

/**
 * Comparator of similarities based on their edge weight.
 * @param <S>
 */
public class EdgeComparator<S extends IEdge> implements Comparator<S> {

  @Override
  public int compare(S o1, S o2) {
    return Double.compare(o1.getDistance(), o2.getDistance());
  }
}
