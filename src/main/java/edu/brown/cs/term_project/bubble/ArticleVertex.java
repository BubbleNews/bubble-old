package edu.brown.cs.term_project.bubble;

import edu.brown.cs.term_project.graph.INode;
import edu.brown.cs.term_project.similarity.IText;
import edu.brown.cs.term_project.similarity.IWord;
import edu.brown.cs.term_project.similarity.TextCorpus;
import edu.brown.cs.term_project.language.TextProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that allows articles and the connections between them to be represented
 * in a graph like structure (thus implements INode).
 */
public class ArticleVertex implements INode<Similarity>, IText {
  private final List<Similarity> similarities;
  private final Article article;
  private final Map<IWord, Double> entities;
  private final Map<IWord, Double> words;
  private final Map<IWord, Double> title;
  private Map<IWord, Double> entitiesImportance;
  private Map<IWord, Double> wordsImportance;
  private Map<IWord, Double> titleImportance;

  /**
   * Constructor for an Article Vertex.
   * @param article the article object the vertex represents
   * @param text the string text of the article
   * @param entities a frequency map of entities for the article
   */
  public ArticleVertex(Article article, String text, Map<IWord, Double> entities) {
    this.similarities = new ArrayList<>();
    this.article = article;
    this.entities = entities;
    this.words = setWords(text, false);
    this.title = setWords(article.getTitle(), true);
    this.entitiesImportance = null;
    this.wordsImportance = null;
    this.titleImportance = null;
  }

  @Override
  public List<Similarity> getEdges() {
    return similarities;
  }

  /**
   * Gets the distance to another node by finding the relevant edge, and getting the distance.
   * @param destination - destination node
   * @return - distance to dst
   */
  @Override
  public double getDistance(INode<Similarity> destination) throws RuntimeException {
    if (destination.equals(this)) {
      return 0;
    } else {
      for (Similarity e: similarities) {
        if (e.getSource().equals(destination) || e.getDest().equals(destination)) {
          return e.getDistance();
        }
      }
      throw new RuntimeException("Edge Not Found");
    }
  }

  @Override
  public Integer getId() {
    return article.getId();
  }

  /**
   * Gets the article of an article vertex.
   * @return the article
   */
  public Article getArticle() {
    return article;
  }

  /**
   * Sets edges of Article, by parsing through list of Similarities and finding the ones that
   * include itself.
   * @param newSimilarities - similarities to pull edges from
   */
  public void setEdges(List<Similarity> newSimilarities) {
    for (Similarity similarity: newSimilarities) {
      if (similarity.getSource().equals(this) || similarity.getDest().equals(this)) {
        this.similarities.add(similarity);
      }
    }
  }

  /**
   * Adds an edge to the article vertex.
   * @param similarity the edge to be added
   */
  public void addEdge(Similarity similarity) {
    similarities.add(similarity);
  }

  /**
   * Method to find the edge connecting this Article to another.
   * @param dst - Article to find Similarity to
   * @return - Similarity connecting to dst
   */
  public Similarity getEdge(INode<Similarity> dst) {
    for (Similarity e: similarities) {
      if (e.getSource().equals(dst) || e.getDest().equals(dst)) {
        return e;
      }
    }
    return null;
  }

  /**
   * Makes a term frequency hashmap for a text.
   * @param text the text
   * @param isTitle whether or not the text is in a title
   * @return the term frequency map
   */
  private Map<IWord, Double> setWords(String text, boolean isTitle) {
    HashMap<IWord, Double> wordMap = new HashMap<>();
    String[] splitWords;
    if (isTitle) {
      splitWords = TextProcessing.lemmatizeText(text);
    } else {
      splitWords = text.split("~\\^");
    }
    // loop through words adding to frequency map
    for (String word: splitWords) {
      ArticleWord articleWord = new ArticleWord(word);
      if (wordMap.containsKey(articleWord)) {
        wordMap.replace(articleWord, wordMap.get(articleWord) + 1);
      } else {
        wordMap.put(articleWord, 1.0);
      }
    }
    return wordMap;
  }

  @Override
  public Map<IWord, Double> getFreq(Integer textType) {
    switch (textType) {
      case 0:
        return new HashMap<>(entities);
      case 1:
        return new HashMap<>(words);
      case 2:
        return new HashMap<>(title);
      default:
        return null;
    }
  }

  /**
   * Sets the importance maps of the Article Vertex by calling the text type's
   * respective corpus objects' getImportanceMap methods.
   * @param entityCorpus a text corpus for entities
   * @param wordCorpus a text corpus for words
   * @param titleCorpus a text corpus for titles
   */
  public void setImportance(TextCorpus<Entity, ArticleVertex> entityCorpus, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<ArticleWord, ArticleVertex> titleCorpus) {
    this.entitiesImportance = entityCorpus.getImportanceMap(getFreq(0));
    this.wordsImportance = wordCorpus.getImportanceMap(getFreq(1));
    this.titleImportance = titleCorpus.getImportanceMap((getFreq(2)));
  }

  /**
   * Gets the importance map of the inputted text type.
   * @param textType an integer corresponding to the text type
   * @return the importance map for that text type
   */
  public Map<IWord, Double> getImportance(Integer textType) {
    switch (textType) {
      case 0:
        return entitiesImportance;
      case 1:
        return wordsImportance;
      case 2:
        return titleImportance;
      default:
        return null;
    }
  }

  @Override
  public String toString() {
    return "ArticleVertex{"
        + "article=" + article
        + ", entitiesImportance=" + entitiesImportance
        + ", wordsImportance=" + wordsImportance
        + ", titleImportance=" + titleImportance
        + '}';
  }
}
