package edu.brown.cs.term_project.TextSimilarity;

import java.util.HashMap;

public interface IText<W extends IWord> {

  /**
   *
   * @param textType and integer telling which type of text (ie Entity  vs Vocab)
   * @return as hashmap that maps from word to frequency in text.
   */
  HashMap<W, Double> getFreq(Integer textType);
}
