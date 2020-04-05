package edu.brown.cs.term_project.TextSimilarity;

import java.util.HashMap;
import java.util.Set;

public class TextCorpus<W extends IWord, T extends IText, C extends IConnection> {
  private HashMap<W, Double> wordFreq;
  private Set<T> nodes;

  public TextCorpus(HashMap<W, Double> wordFreq, Set<T> nodes) {
    this.wordFreq = wordFreq;
    this.nodes = nodes;
  }

  //TODO: Implement findConnections and getSimilarity
//  public Set<C> findConnections() {
//
//  }
//
//  public double getSimilarity(T src, T dst) {
//
//  }
}
