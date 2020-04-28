package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.TextSimilarity.IWord;

import java.util.Objects;

public class ArticleWord implements IWord {
  private String word;

  public ArticleWord(String word) {
    this.word = word;
  }
  @Override
  public String getWord() {
    return word;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArticleWord that = (ArticleWord) o;
    return Objects.equals(word, that.word);
  }

  @Override
  public int hashCode() {
    return Objects.hash(word);
  }
}
