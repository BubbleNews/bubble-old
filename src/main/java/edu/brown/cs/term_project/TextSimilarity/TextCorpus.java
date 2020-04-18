package edu.brown.cs.term_project.TextSimilarity;

import java.util.HashMap;
import java.util.Map;
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
    Map<W, Integer> srcMap = src.getFreq(textType);
    Map<W, Integer> dstMap = dst.getFreq(textType);
    Set<W> sharedWords = srcMap.keySet();
    sharedWords.retainAll(dstMap.keySet());
    double dotProduct = 0;
    for (W w: sharedWords) {
      dotProduct += this.getImportance(srcMap, w) * this.getImportance(dstMap, w);
    }
    return dotProduct / (Math.sqrt(this.getMagImportance(srcMap)
        + Math.sqrt(this.getMagImportance(dstMap))));
  }

  private Double getImportance(Map<W, Integer> wordMap, W word) {
    Double normalizedTermFrequency =  wordMap.get(word) / (double) wordMap.size();
    Double inverseDocumentFrequency = docNum / (double) wordFreq.get(word);
    return normalizedTermFrequency * inverseDocumentFrequency;
  }

  private Double getMagImportance(Map<W, Integer> wordMap) {
    double sum = 0;
    for (W w: wordMap.keySet()) {
      sum += this.getImportance(wordMap, w);
    }
    return sum;
  }

}
