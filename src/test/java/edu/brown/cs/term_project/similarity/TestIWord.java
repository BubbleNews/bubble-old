package edu.brown.cs.term_project.similarity;

import java.util.Objects;

public class TestIWord implements IWord {

  private final String word;

  public TestIWord(String word) {
    this.word = word;
  }
  @Override
  public String getWord() {
    return this.word;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TestIWord)) {
      return false;
    }
    TestIWord testIWord = (TestIWord) o;
    return Objects.equals(word, testIWord.word);
  }

  @Override
  public int hashCode() {
    return Objects.hash(word);
  }
}
