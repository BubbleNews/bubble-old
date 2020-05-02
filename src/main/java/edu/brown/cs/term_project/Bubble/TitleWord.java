package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.TextSimilarity.IWord;

import java.util.Objects;

public class TitleWord implements IWord {
  private String word;

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
