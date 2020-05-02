package edu.brown.cs.term_project.TextSimilarity;

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

  /**
   * This instantiates the TextCorpus.
   * @param wordFreq hashmap representing number of documents each word in our vocab is in
   * @param docNum number of documents used to create our vocab
   */
  public TextCorpus(Map<W, Double> wordFreq, Integer docNum) {
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
    Map<IWord, Double> srcMap = src.getFreq(textType);
    Map<IWord, Double> dstMap = dst.getFreq(textType);
    Set<IWord> sharedWords = srcMap.keySet();
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
    Double inverseDocumentFrequency = docNum / wordFreq.get(word);
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
   * Returns the cosine similarity of two documents. It determines this based on the based in
   * value for the type of word we are comparing.
   * @param src Article 1
   * @param dst Article 2
   * @param textType Integer representing the type of word we are comparing from the articles
   * @return the cosine similarity of the two articles based on the textType
   */
  public Map<IWord, Double> getSimilarityHast(T src, T dst, Integer textType) {
    Map<IWord, Double> srcMap = src.getFreq(textType);
    Map<IWord, Double> dstMap = dst.getFreq(textType);
    Set<IWord> sharedWords = srcMap.keySet();
    sharedWords.retainAll(dstMap.keySet());
    double
    for (IWord w: sharedWords) {
      dotProduct += this.getImportance(srcMap, w) * this.getImportance(dstMap, w);
    }
    return dotProduct / (Math.sqrt(this.getMagImportance(srcMap)
        + Math.sqrt(this.getMagImportance(dstMap))));
  }

}
