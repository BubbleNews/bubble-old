package edu.brown.cs.term_project.TextSimilarity;

import java.util.Map;

public interface IText {

  /**
   *
   * @param textType and integer telling which type of text (ie Entity  vs Vocab)
   * @return as hashmap that maps from word to frequency in text.
   */
  Map<? extends IWord, Integer> getFreq(Integer textType);
}
