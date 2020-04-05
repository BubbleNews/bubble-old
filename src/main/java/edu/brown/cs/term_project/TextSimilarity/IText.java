package edu.brown.cs.term_project.TextSimilarity;

import java.util.HashMap;

public interface IText<W extends IWord> {

  /**
   *
   * @param textType and integer telling which type of text (ie Entity  vs Vocab)
   * @return
   */
  public HashMap<W, Double> getFreq(Integer textType);
}
