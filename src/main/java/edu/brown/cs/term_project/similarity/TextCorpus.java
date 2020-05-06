package edu.brown.cs.term_project.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a textcorpus and allows us to calculate similarity between articles.
 * @param <W> A type that implements IWord
 * @param <T> An article type that implements IText
 */
public class TextCorpus<W extends IWord, T extends IText> {
  private Map<W, Double> wordFreq;
  private int docNum;
  private int corpusType;

  /**
   * This instantiates the TextCorpus.
   * @param wordFreq hashmap representing number of documents each word in our vocab is in
   * @param docNum number of documents used to create our vocab
   * @param corpusType int representing corpus type
   */
  public TextCorpus(Map<W, Double> wordFreq, Integer docNum, int corpusType) {
    this.wordFreq = wordFreq;
    this.docNum = docNum;
    this.corpusType = corpusType;
  }

  /**
   * Returns the cosine similarity of two documents. It determines this based on the based in
   * value for the type of word we are comparing.
   * @param src Article 1
   * @param dst Article 2
   * @return the cosine similarity of the two articles based on the textType
   */
  public Double getSimilarity(T src, T dst) {
    Map<IWord, Double> srcMap = src.getFreq(corpusType);
    Map<IWord, Double> dstMap = dst.getFreq(corpusType);
    Set<IWord> sharedWords = new HashSet<>(srcMap.keySet());
    sharedWords.retainAll(dstMap.keySet());
    double dotProduct = 0;
    for (IWord w: sharedWords) {
      dotProduct += this.getImportance(srcMap, w) * this.getImportance(dstMap, w);
    }
    return dotProduct / (Math.sqrt(this.getMagImportance(srcMap)
        + Math.sqrt(this.getMagImportance(dstMap))));
  }

  /**
   * Gets the importance of a word in a document based on its normalized term frequency and its
   * inverse document frequency.
   * @param wordMap word frequency hashmap for a article
   * @param word the word to get importance for
   * @return the importance of the word in the document
   */
  private Double getImportance(Map<? extends IWord, Double> wordMap, IWord word) {
    Double normalizedTermFrequency =  wordMap.get(word) / (double) wordMap.size();
    Double inverseDocumentFrequency = docNum / wordFreq.getOrDefault(word, 1.0);
    return normalizedTermFrequency * inverseDocumentFrequency;
  }

  /**
   * Sums up the importances of all words in an article.
   * @param wordMap a hashmap of all the word frequencies in an article.
   * @return the sum of all the importances of the words in the article.
   */
  private Double getMagImportance(Map<? extends IWord, Double> wordMap) {
    double sum = 0;
    for (IWord w: wordMap.keySet()) {
      sum += this.getImportance(wordMap, w);
    }
    return sum;
  }

  /**
   * Gets the importance map for words in a Text.
   * @param map a word frequency map
   * @return the importance map
   */
  public Map<IWord, Double> getImportanceMap(Map<IWord, Double> map) {
    Map<IWord, Double> importanceMap = new HashMap<>();
    for (IWord w: map.keySet()) {
      importanceMap.put(w, getImportance(map, w));
    }
    return importanceMap;
  }

  /**
   * Returns the cosine similarity of two documents. It determines this based on the based in
   * value for the type of word we are comparing.
   * @param src Article 1
   * @param dst Article 2
   * @return the cosine similarity of the two articles based on the textType
   */
  public Map<IWord, Double> getSimilarityHash(T src, T dst) {
    Map<IWord, Double> srcMap = src.getFreq(corpusType);
    Map<IWord, Double> dstMap = dst.getFreq(corpusType);
    Map<IWord, Double> srcImportance = src.getImportance(corpusType);
    Map<IWord, Double> dstImportance = dst.getImportance(corpusType);
    Set<IWord> sharedWords = new HashSet<>(srcImportance.keySet());
    sharedWords.retainAll(dstImportance.keySet());
    double totImportance = (Math.sqrt(this.getMagImportance(srcMap)
        + Math.sqrt(this.getMagImportance(dstMap))));
    Map<IWord, Double> edgeImportance = new HashMap<>();
    for (IWord w: sharedWords) {
      double toAdd = (srcImportance.get(w) * dstImportance.get(w)) / totImportance;
      edgeImportance.put(w, toAdd);
    }
    return edgeImportance;
  }

}
