package edu.brown.cs.cchrabas_jgraves1.Dijkstra;

/**
 * Class representing a generic edge.
 */
public class GenericEdge implements IEdge<GenericVertex> {
  GenericVertex start;
  GenericVertex end;
  double weight;

  /**
   * Constructor for a generic edge
   * @param s - the start vertex of the edge
   * @param e - the end vertex of the edge
   * @param weight - the length of the edge
   */
  public GenericEdge(GenericVertex s, GenericVertex e, double weight) {
    this.start = s;
    this.end = e;
    this.weight = weight;
  }


  @Override
  public double getWeight() {
    return weight;
  }

  @Override
  public GenericVertex getEnd() {
    return end;
  }

  @Override
  public GenericVertex getStart() {
    return start;
  }

  @Override
  public void setStart(GenericVertex start) {
    this.start = start;
  }
}
