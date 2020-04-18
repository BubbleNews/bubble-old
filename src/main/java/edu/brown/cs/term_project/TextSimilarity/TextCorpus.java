package edu.brown.cs.term_project.TextSimilarity;

import java.util.HashMap;
import java.util.Set;

public class TextCorpus<W extends IWord, T extends IText<W>> {
  private HashMap<W, Double> wordFreq;
  private Set<T> nodes;

  public TextCorpus(HashMap<W, Double> wordFreq, Set<T> nodes) {
    this.wordFreq = wordFreq;
    this.nodes = nodes;
  }

  public Double getSimilarity(T src, T dst, Integer textType) {
    return null;
  }
}
