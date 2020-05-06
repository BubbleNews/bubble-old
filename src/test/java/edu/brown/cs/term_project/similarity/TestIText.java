package edu.brown.cs.term_project.similarity;

import java.util.Map;

public class TestIText implements IText {

  private final Map<IWord, Double> freq;
  private final Map<IWord, Double> importanceMap;

  public TestIText(Map<IWord, Double> freq, Map<IWord, Double> importanceMap) {
    this.freq = freq;
    this.importanceMap = importanceMap;
  }
  @Override
  public Map<IWord, Double> getFreq(Integer textType) {
    if (textType == 0) {
      return freq;
    }
    return null;
  }

  @Override
  public Map<IWord, Double> getImportance(Integer textType) {
    if (textType == 0) {
      return this.importanceMap;
    }
    return null;
  }
}
