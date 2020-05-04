package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.IEdge;
import edu.brown.cs.term_project.TextSimilarity.IWord;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;

import java.util.Map;


public class Similarity implements IEdge<ArticleVertex> {
  private final ArticleVertex src;
  private final ArticleVertex dest;
  private double distance;
  private Map<IWord, Double> wordSim;
  private Map<IWord, Double> entitySim;
  private Map<IWord, Double> titleSim;
  private double wordDistance;
  private double entityDistance;
  private double titleDistance;


  public Similarity(ArticleVertex src, ArticleVertex dest, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<Entity, ArticleVertex> entityCorpus,
                    TextCorpus<ArticleWord, ArticleVertex> titleCorpus, double textWeight,
                    double entityWeight, double titleWeight) {
    this.src = src;
    this.dest = dest;
    this.setDistance(entityCorpus, wordCorpus, titleCorpus, textWeight, entityWeight, titleWeight);
  }


  @Override
  public ArticleVertex getDest() {
    return dest;
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

    this.wordDistance = textWeight / totalWeight  * wordCorpus.getSimilarity(this.src, this.dest);
    this.titleDistance = titleWeight / totalWeight * titleCorpus.getSimilarity(this.src, this.dest);
    this.entityDistance = entityWeight / totalWeight * entityCorpus.getSimilarity(this.src, this.dest);

    this.distance = 1 / (wordDistance + titleDistance + entityDistance + divideZeroShift);
  }

  public void setImportance(TextCorpus<Entity, ArticleVertex> entityCorpus, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<ArticleWord, ArticleVertex> titleCorpus) {
    this.entitySim = entityCorpus.getSimilarityHash(src, dest);
    this.wordSim = wordCorpus.getSimilarityHash(src, dest);
    this.titleSim = titleCorpus.getSimilarityHash(src, dest);
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


  public SimilarityJSON toSimilarityJSON() {
    SimilarityJSON sim = new SimilarityJSON(src.getId(), dest.getId(), this.distance,
        this.titleDistance, this.wordDistance, this.entityDistance);
    return sim;
  }
}


