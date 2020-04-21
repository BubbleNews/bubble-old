package edu.brown.cs.term_project.TextSimilarity;

import java.util.HashMap;

public class TextCorpus<W extends IWord, T extends IText<W>> {
  private HashMap<W, Integer> wordFreq;
  private int docNum;

  public TextCorpus(HashMap<W, Integer> wordFreq, Integer docNum) {
    this.wordFreq = wordFreq;
    this.docNum = docNum;
  }

  public Double getSimilarity(T src, T dst, Integer textType) {
    return null;
  }
}
