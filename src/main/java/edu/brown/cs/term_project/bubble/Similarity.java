package edu.brown.cs.term_project.bubble;

import edu.brown.cs.term_project.api.response.SimilarityJSON;
import edu.brown.cs.term_project.graph.IEdge;
import edu.brown.cs.term_project.similarity.IWord;
import edu.brown.cs.term_project.similarity.TextCorpus;

import java.util.Map;

/**
 * Class that represents the edge between two articles in a graph by the
 * similarity between the two articles.
 */
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

  /**
   * Constructor.
   * @param src the source article vertex
   * @param dest the destination article vertex
   */
  public Similarity(ArticleVertex src, ArticleVertex dest) {
    this.src = src;
    this.dest = dest;
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

  /**
   * Sets the total distance of the edge.
   * @param wordCorpus a text corpus for words
   * @param entityCorpus a text corpus for entities
   * @param titleCorpus a text corpus for title
   * @param textWeight weight of text distance in total distance calculation
   * @param entityWeight weight of entity distance in total distance calculation
   * @param titleWeight weight of title distance in total distance calculation
   */
  public void setDistance(TextCorpus<Entity, ArticleVertex> entityCorpus, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<ArticleWord, ArticleVertex> titleCorpus,
                           double textWeight, double entityWeight, double titleWeight) {
    // Should be between 0 and 1, the higher it is, the more highly weighted entities are
    double totalWeight = textWeight + entityWeight + titleWeight;
    final double divideZeroShift = .00000001;

    this.wordDistance =
        textWeight / totalWeight  * wordCorpus.getSimilarity(this.src, this.dest);
    this.titleDistance =
        titleWeight / totalWeight * titleCorpus.getSimilarity(this.src, this.dest);
    this.entityDistance =
        entityWeight / totalWeight * entityCorpus.getSimilarity(this.src, this.dest);

    this.distance = 1 / (wordDistance + titleDistance + entityDistance + divideZeroShift);
  }

  /**
   * Sets the importance hash maps in the edge to see how important words and
   * entities are too the edge weight calculation.
   * @param wordCorpus a text corpus for words
   * @param entityCorpus a text corpus for entities
   * @param titleCorpus a text corpus for title
   */
  public void setImportance(TextCorpus<Entity, ArticleVertex> entityCorpus, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<ArticleWord, ArticleVertex> titleCorpus) {
    this.entitySim = entityCorpus.getSimilarityHash(src, dest);
    this.wordSim = wordCorpus.getSimilarityHash(src, dest);
    this.titleSim = titleCorpus.getSimilarityHash(src, dest);
  }

  /**
   * Getter for word similarity map.
   * @return word similarity map
   */
  public Map<IWord, Double> getWordSim() {
    return wordSim;
  }

  /**
   * Getter for entity similarity map.
   * @return entity similarity map
   */
  public Map<IWord, Double> getEntitySim() {
    return entitySim;
  }

  /**
   * Getter for title similarity map.
   * @return title similarity map
   */
  public Map<IWord, Double> getTitleSim() {
    return titleSim;
  }

  /**
   * A method that turns a similarity into a similarity JSON.
   * @return the similarity json representation of the edge
   */
  public SimilarityJSON toSimilarityJSON() {
    SimilarityJSON sim = new SimilarityJSON(src.getId(), dest.getId(), this.distance,
        this.titleDistance, this.wordDistance, this.entityDistance);
    return sim;
  }
}


