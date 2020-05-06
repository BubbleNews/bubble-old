package edu.brown.cs.term_project.similarity;

import java.util.Map;

/**
 * This interface reprents a text that can be passed into the TextCorpus.
 */
public interface IText {

  /**
   * Gets the IWord frequency map for the Text.
   * @param textType and integer telling which type of text (ie Entity vs Vocab)
   * @return as hashmap that maps from word to frequency in text.
   */
  Map<IWord, Double> getFreq(Integer textType);

  /**
   * Gets a map of each word in the text's importance.
   * @param textType number representing the text type
   * @return a map of each word in the text's importance
   */
  Map<IWord, Double> getImportance(Integer textType);
}
