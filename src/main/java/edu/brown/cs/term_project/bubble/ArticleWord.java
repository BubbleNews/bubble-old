package edu.brown.cs.term_project.bubble;

import edu.brown.cs.term_project.similarity.IWord;

import java.util.Objects;

/**
 * A class wrapping simple word strings.
 */
public class ArticleWord implements IWord {
  private String word;

  /**
   * Constructor for an ArticleWord.
   * @param word the string word
   */
  public ArticleWord(String word) {
    this.word = word;
  }

  @Override
  public String getWord() {
    return word;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArticleWord)) {
      return false;
    }
    ArticleWord that = (ArticleWord) o;
    return word.equals(that.word);
  }

  @Override
  public int hashCode() {
    return Objects.hash(word);
  }

  @Override
  public String toString() {
    return word;
  }
}
