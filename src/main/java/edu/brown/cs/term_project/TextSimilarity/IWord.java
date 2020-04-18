package edu.brown.cs.term_project.TextSimilarity;

public interface IWord {

  String getWord();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();
}
