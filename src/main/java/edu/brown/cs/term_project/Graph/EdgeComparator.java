package edu.brown.cs.term_project.Graph;



import java.util.Comparator;

/**
 * Comparator of similarities based on their edge weight.
 * @param <T> - type of node used
 * @param <S> - type of edge used
 */
public class EdgeComparator<T extends INode<S>, S extends IEdge<T>> implements Comparator<S> {

  @Override
  public int compare(S o1, S o2) {
    return Double.compare(o1.getDistance(), o2.getDistance());
  }
}
