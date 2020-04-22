package edu.brown.cs.term_project.TextSimilarity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * This class represents a textcorpus and allows us to calculate similarity between articles.
 * @param <W> A type that implements IWord
 * @param <T> An article type that implements IText
 */
public class TextCorpus<W extends IWord, T extends IText<W>> {
  private HashMap<W, Integer> wordFreq;
  private int docNum;

  /**
   * This instantiates the TextCorpus.
   * @param wordFreq hashmap representing number of documents each word in our vocab is in
   * @param docNum number of documents used to create our vocab
   */
  public TextCorpus(HashMap<W, Integer> wordFreq, Integer docNum) {
    this.wordFreq = wordFreq;
    this.docNum = docNum;
  }

  /**
   * Returns the cosine similarity of two documents. It determines this based on the based in
   * value for the type of word we are comparing.
   * @param src Article 1
   * @param dst Article 2
   * @param textType Integer representing the type of word we are comparing from the articles
   * @return the cosine similarity of the two articles based on the textType
   */
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

  /**
   * Gets the importance of a word in a document based on its normalized term frequency and its
   * inverse document frequency.
   * @param wordMap word frequency hashmap for a article
   * @param word the word to get importance for
   * @return the importance of the word in the document
   */
  private Double getImportance(Map<W, Integer> wordMap, W word) {
    Double normalizedTermFrequency =  wordMap.get(word) / (double) wordMap.size();
    Double inverseDocumentFrequency = docNum / (double) wordFreq.get(word);
    return normalizedTermFrequency * inverseDocumentFrequency;
  }

  /**
   * Sums up the importances of all words in an article.
   * @param wordMap a hashmap of all the word frequencies in an article.
   * @return the sum of all the importances of the words in the article.
   */
  private Double getMagImportance(Map<W, Integer> wordMap) {
    double sum = 0;
    for (W w: wordMap.keySet()) {
      sum += this.getImportance(wordMap, w);
    }
    return sum;
  }

}
