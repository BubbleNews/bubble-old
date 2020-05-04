package edu.brown.cs.term_project.api.response;

/**
 * Class for serializing a similarity edge;
 */
public class SimilarityJSON {
  int articleId1;
  int articleId2;
  double totalDistance;
  double titleDistance;
  double wordDistance;
  double entityDistance;

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
