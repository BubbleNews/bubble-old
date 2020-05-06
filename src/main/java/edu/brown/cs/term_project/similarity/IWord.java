package edu.brown.cs.term_project.similarity;

/**
 * Interface for word classes.
 */
public interface IWord {

  String getWord();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();
}
