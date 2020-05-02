package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.IEdge;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;

import java.util.Map;


public class Similarity implements IEdge<ArticleVertex> {
  private ArticleVertex src;
  private ArticleVertex dst;
  private double distance;
  private Map<ArticleWord, Double> wordSim;
  private Map<Entity, Double> entitySim;
  private Map<TitleWord, Double> titleSim;

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
    this.distance = 1 / ((entityWeight * entityCorpus.getSimilarity(this.src, this.dst, 0))
        + ((1 - entityWeight) * wordCorpus.getSimilarity(this.src, this.dst, 1)) + divideZeroShift);
  }

  public void setEntitySim(Map<Entity, Double> entitySim) {
    this.entitySim = entitySim;
  }

  public void setTitleSim(Map<TitleWord, Double> titleSim) {
    this.titleSim = titleSim;
  }

  public void setWordSim(Map<ArticleWord, Double> wordSim) {
    this.wordSim = wordSim;
  }

  public Map<ArticleWord, Double> getWordSim() {
    return wordSim;
  }

  public Map<Entity, Double> getEntitySim() {
    return entitySim;
  }

  public Map<TitleWord, Double> getTitleSim() {
    return titleSim;
  }
}
