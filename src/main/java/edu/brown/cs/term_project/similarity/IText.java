package edu.brown.cs.term_project.similarity;

import java.util.Map;

public interface IText {

  /**
   * Gets the IWord frequency map for the Text.
   * @param textType and integer telling which type of text (ie Entity vs Vocab)
   * @return as hashmap that maps from word to frequency in text.
   */
  Map<IWord, Double> getFreq(Integer textType);

  /**
   * Gets the importance of each word map for the text.
   * @param textType the text type (entity, word)
   * @return the importance map
   */
  Map<IWord, Double> getImportance(Integer textType);
}
