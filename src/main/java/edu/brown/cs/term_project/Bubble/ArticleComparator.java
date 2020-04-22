package edu.brown.cs.term_project.Bubble;


import java.util.Comparator;
import java.util.Map;

/**
 * Class comparing Articles based on the distance value in a hash map
 */
public class ArticleComparator implements Comparator<ArticleVertex> {
  private Map<Integer, Double> distances;

  public ArticleComparator(Map<Integer, Double> distances) {
    this.distances = distances;
  }

  @Override
  public int compare(ArticleVertex o1, ArticleVertex o2) {
    return Double.compare(distances.get(o1.getId()), distances.get(o2.getId()));
  }
}
