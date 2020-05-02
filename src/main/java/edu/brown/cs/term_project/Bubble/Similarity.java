package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.IEdge;
import edu.brown.cs.term_project.TextSimilarity.IWord;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;

import java.util.Map;


public class Similarity implements IEdge<ArticleVertex> {
  private final ArticleVertex src;
  private final ArticleVertex dst;
  private double distance;
  private Map<IWord, Double> wordSim;
  private Map<IWord, Double> entitySim;
  private Map<IWord, Double> titleSim;


  public Similarity(ArticleVertex src, ArticleVertex dst, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<Entity, ArticleVertex> entityCorpus,
                    TextCorpus<ArticleWord, ArticleVertex> titleCorpus, double textWeight,
                    double entityWeight, double titleWeight) {
    this.src = src;
    this.dst = dst;
    this.setDistance(entityCorpus, wordCorpus, titleCorpus, textWeight, entityWeight, titleWeight);
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

  private void setDistance(TextCorpus<Entity, ArticleVertex> entityCorpus, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<ArticleWord, ArticleVertex> titleCorpus,
                           double textWeight, double entityWeight, double titleWeight) {
    // Should be between 0 and 1, the higher it is, the more highly weighted entities are
    double totalWeight = textWeight + entityWeight + titleWeight;
    final double divideZeroShift = .00000001;
    this.distance = 1 / ((entityWeight / totalWeight
        * entityCorpus.getSimilarity(this.src, this.dst))
        + (textWeight / totalWeight  * wordCorpus.getSimilarity(this.src, this.dst))
        + (titleWeight / totalWeight * titleCorpus.getSimilarity(this.src, this.dst))
        + divideZeroShift);
  }

  public void setImportance(TextCorpus<Entity, ArticleVertex> entityCorpus, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<ArticleWord, ArticleVertex> titleCorpus) {
    this.entitySim = entityCorpus.getSimilarityHash(src, dst);
    this.wordSim = wordCorpus.getSimilarityHash(src, dst);
    this.titleSim = titleCorpus.getSimilarityHash(src, dst);
  }

  public Map<IWord, Double> getWordSim() {
    return wordSim;
  }

  public Map<IWord, Double> getEntitySim() {
    return entitySim;
  }

  public Map<IWord, Double> getTitleSim() {
    return titleSim;
  }
}
