package edu.brown.cs.term_project.api.response;

/**
 * Class for serializing a similarity edge.
 */
public class SimilarityJSON {
  private int articleId1;
  private int articleId2;
  private double totalDistance;
  private double titleDistance;
  private double wordDistance;
  private double entityDistance;

  /**
   * Constructor for an easily serializable Similarity (Edge) class.
   * @param articleId1 the database id of the first article in the edge
   * @param articleId2 the database id of the second article in the edge
   * @param totalDistance the total calculated distance between the two articles
   * @param titleDistance the distance between the title vectors
   * @param wordDistance the distance between the word vectors
   * @param entityDistance the distance between the entity vectors
   */
  public SimilarityJSON(int articleId1, int articleId2, double totalDistance,
                        double titleDistance, double wordDistance, double entityDistance) {
    this.articleId1 = articleId1;
    this.articleId2 = articleId2;
    this.totalDistance = totalDistance;
    this.titleDistance = titleDistance;
    this.wordDistance = wordDistance;
    this.entityDistance = entityDistance;
  }
}
