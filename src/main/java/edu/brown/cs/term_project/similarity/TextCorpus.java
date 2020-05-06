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
    Double srcTotal = this.getWordMapSize(srcMap);
    Double dstTotal = this.getWordMapSize(dstMap);
    Set<IWord> sharedWords = new HashSet<>(srcMap.keySet());
    sharedWords.retainAll(dstMap.keySet());
    double dotProduct = 0;
    // since this is a dot product we only need to loop through shared words
    for (IWord w: sharedWords) {
      dotProduct += this.getImportance(srcMap, w, srcTotal)
          * this.getImportance(dstMap, w, dstTotal);
    }
    // divide by magnitude
    return dotProduct / (Math.sqrt(this.getMagImportance(srcMap, srcTotal))
        + Math.sqrt(this.getMagImportance(dstMap, dstTotal)));
  }

  /**
   * Gets the total size of an article based on summing all its word's frequencies.
   * @param words map of all the words in an article and their frequencies
   * @return total number of words in an article
   */
  private Double getWordMapSize(Map<IWord, Double> words) {
    Double total = 0.0;
    for (Double freq: words.values()) {
      total += freq;
    }
    return total;
  }

  /**
   * Gets the importance of a word in a document based on its normalized term frequency and its
   * inverse document frequency.
   * @param wordMap word frequency hashmap for a article
   * @param word the word to get importance for
   * @return the importance of the word in the document
   */
  private Double getImportance(Map<? extends IWord, Double> wordMap, IWord word,
                               Double totalWords) {
    Double normalizedTermFrequency =  wordMap.get(word) / totalWords;
    Double inverseDocumentFrequency = 1 + Math.log10(docNum / wordFreq.getOrDefault(word,
        1.0));
    return normalizedTermFrequency * inverseDocumentFrequency;
  }

  /**
   * Sums up the importances of all words in an article.
   * @param wordMap a hashmap of all the word frequencies in an article.
   * @return the sum of all the importances of the words in the article.
   */
  private Double getMagImportance(Map<? extends IWord, Double> wordMap, Double totalWords) {
    double sum = 0;
    for (IWord w: wordMap.keySet()) {
      sum += this.getImportance(wordMap, w, totalWords);
    }
    return sum;
  }


  /**
   * Retrieves a map containing every word and its importance value for an article.
   * @param map a map of words to their frequency in the article
   * @return map of words to their importance in the article
   */
  public Map<IWord, Double> getImportanceMap(Map<IWord, Double> map) {
    Map<IWord, Double> importanceMap = new HashMap<>();
    for (IWord w: map.keySet()) {
      importanceMap.put(w, getImportance(map, w, this.getWordMapSize(map)));
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
    // calculate the amount we divide by for cosine similarity
    double totImportance = (Math.sqrt(this.getMagImportance(srcMap, this.getWordMapSize(srcMap)))
        + Math.sqrt(this.getMagImportance(dstMap, this.getWordMapSize(dstMap))));
    Map<IWord, Double> edgeImportance = new HashMap<>();
    // do dot product for each word and divide by magnitude
    for (IWord w: sharedWords) {
      double toAdd = (srcImportance.get(w) * dstImportance.get(w)) / totImportance;
      edgeImportance.put(w, toAdd);
    }
    return edgeImportance;
  }

}
