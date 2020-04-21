package edu.brown.cs.term_project.Bubble;


import java.util.Comparator;
import java.util.Map;

/**
 * Class comparing Articles based on the distance value in a hash map
 */
public class ArticleComparator implements Comparator<Article> {
  private Map<Integer, Double> distances;

  public ArticleComparator(Map<Integer, Double> distances) {
    this.distances = distances;
  }

  @Override
  public int compare(Article o1, Article o2) {
    return Double.compare(distances.get(o1.getId()), distances.get(o2.getId()));
  }
}
