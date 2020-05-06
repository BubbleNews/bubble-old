package edu.brown.cs.term_project.similarity;

/**
 * Interface for word classes.
 */
public interface IWord {
  /**
   * Getter for the word string represented by the IWord.
   * @return the word string
   */
  String getWord();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();
}
