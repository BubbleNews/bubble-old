package edu.brown.cs.term_project.similarity;

/**
 * This interface represents a word that can be passed into our TextCorpus.
 */
public interface IWord {

  /**
   * This gets the word as a string.
   * @return the word
   */
  String getWord();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();
}
