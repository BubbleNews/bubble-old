package edu.brown.cs.term_project.TextSimilarity;

import java.util.HashMap;
import java.util.Set;

public class TextCorpus<W extends IWord, T extends IText<W>> {
  private HashMap<W, Integer> wordFreq;
  private Set<T> nodes;
  private int docNum;

  public TextCorpus(HashMap<W, Integer> wordFreq, Set<T> nodes, Integer docNum) {
    this.wordFreq = wordFreq;
    this.nodes = nodes;
    this.docNum = docNum;
  }

  public Double getSimilarity(T src, T dst, Integer textType) {
    return null;
  }
}
