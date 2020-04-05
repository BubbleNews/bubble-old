package edu.brown.cs.term_project.TextSimilarity;

public interface IConnection<T extends IText> {

  public T getSource();

  public T getDest();

  public double getDistance();
}
