package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.TextSimilarity.IWord;

public class ArticleWord implements IWord {
  private String word;

  public ArticleWord(String word) {
    this.word = word;
  }
  @Override
  public String getWord() {
    return word;
  }
}
