package edu.brown.cs.term_project.TextSimilarity;

public interface IWord {

  public String getWord();

  @Override
  public boolean equals(Object o);

  @Override
  public int hashCode();
}
