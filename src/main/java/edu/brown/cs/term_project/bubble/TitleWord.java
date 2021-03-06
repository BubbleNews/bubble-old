package edu.brown.cs.term_project.bubble;

import edu.brown.cs.term_project.similarity.IWord;

import java.util.Objects;

/**
 * Class for words in the title of an article.
 */
public class TitleWord implements IWord {
  private String word;

  /**
   * Constructor for a titleword.
   * @param word the string word
   */
  public TitleWord(String word) {
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
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TitleWord that = (TitleWord) o;
    return Objects.equals(word, that.word);
  }

  @Override
  public int hashCode() {
    return Objects.hash(word);
  }
}
