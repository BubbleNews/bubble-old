package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.IEdge;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;


public class Similarity implements IEdge<ArticleVertex> {
  private final ArticleVertex src;
  private final ArticleVertex dst;
  private double distance;

  public Similarity(ArticleVertex src, ArticleVertex dst, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<Entity, ArticleVertex> entityCorpus,
                    double textWeight, double entityWeight, double titleWeight) {
    this.src = src;
    this.dst = dst;
    this.setDistance(wordCorpus, entityCorpus, textWeight, entityWeight, titleWeight);
  }


  @Override
  public ArticleVertex getDest() {
    return dst;
  }

  @Override
  public ArticleVertex getSource() {
    return src;
  }

  @Override
  public double getDistance() {
    return distance;
  }

  private void setDistance(TextCorpus<ArticleWord, ArticleVertex> wordCorpus, TextCorpus<Entity,
      ArticleVertex> entityCorpus, double textWeight, double entityWeight, double titleWeight) {
    // Should be between 0 and 1, the higher it is, the more highly weighted entities are
    double totalWeight = textWeight + entityWeight + titleWeight;
    final double divideZeroShift = .00000001;
    this.distance = 1 / ((entityWeight / totalWeight
        * entityCorpus.getSimilarity(this.src, this.dst, 0))
        + (textWeight / totalWeight  * wordCorpus.getSimilarity(this.src, this.dst, 1))
        + (titleWeight / totalWeight * wordCorpus.getSimilarity(this.src, this.dst, 2))
        + divideZeroShift);
  }

}
