package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.INode;

import java.util.ArrayList;
import java.util.List;

public class ArticleVertex implements INode<Similarity> {
  private List<Similarity> similarities;
  private Article article;

  public ArticleVertex(Article article) {
    this.similarities = new ArrayList<>();
    this.article = article;
  }

  @Override
  public List<Similarity> getEdges() {
    return similarities;
  }

  /**
   * Gets the distance to another node by finding the relevant edge, and getting the distance
   * @param dst - destination node
   * @return - distance to dst
   */
  @Override
  public double getDistance(INode<Similarity> dst) {
    if (dst.equals(this)) {
      return 0;
    } else {
      for (Similarity e: similarities) {
        if (e.getSource().equals(dst) || e.getDest().equals(dst)) {
          return e.getDistance();
        }
      }
      return -1;
    }
  }

  @Override
  public Integer getId() {
    return article.getId();
  }

  public Article getArticle() {
    return article;
  }

  /**
   * Sets edges of Article, by parsing through list of Similarities and finding the ones that include itself
   * @param newSimilarities - similarities to pull edges from
   */
  public void setEdges(List<Similarity> newSimilarities) {
    for (Similarity similarity: newSimilarities) {
      if (similarity.getSource().equals(this) || similarity.getDest().equals(this)) {
        this.similarities.add(similarity);
      }
    }
  }

  public void addEdges(Similarity similarity) {
    similarities.add(similarity);
  }

  /**
   * Method to find the edge connecting this Article to another.
   * @param dst - Article to find Similarity to
   * @return - Similarity connecting to dst
   */
  public Similarity getEdge(INode<Similarity> dst) {
    for (Similarity e: similarities) {
      if (e.getSource().equals(dst) || e.getDest().equals(dst)) {
        return e;
      }
    }
    return null;
  }
}
