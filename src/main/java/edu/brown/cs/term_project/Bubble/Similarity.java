package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.IEdge;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;


public class Similarity implements IEdge<ArticleVertex> {
  private ArticleVertex src;
  private ArticleVertex dst;
  private double distance;

  public Similarity(ArticleVertex src, ArticleVertex dst, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<Entity, ArticleVertex> entityCorpus) {
    this.src = src;
    this.dst = dst;
    this.setDistance(wordCorpus, entityCorpus);
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
      ArticleVertex> entityCorpus) {
    // Should be between 0 and 1, the higher it is, the more highly weighted entities are
    final double entityWeight = .5;
    final double divideZeroShift = .00001;
    this.distance = 1 / (((1 - entityWeight) * wordCorpus.getSimilarity(this.src, this.dst, 0))
        + (entityWeight * entityCorpus.getSimilarity(this.src, this.dst, 1)) + divideZeroShift);
  }

}
