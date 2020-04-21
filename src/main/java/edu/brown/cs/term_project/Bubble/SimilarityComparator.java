package edu.brown.cs.term_project.Bubble;


import java.util.Comparator;

/**
 * Comparator of similarities based on their edge weight
 */
public class SimilarityComparator implements Comparator<Similarity> {

  @Override
  public int compare(Similarity o1, Similarity o2) {
    return Double.compare(o1.getDistance(), o2.getDistance());
  }
}
