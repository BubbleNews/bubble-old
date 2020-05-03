package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.INode;
import edu.brown.cs.term_project.TextSimilarity.IText;
import edu.brown.cs.term_project.TextSimilarity.IWord;
import edu.brown.cs.term_project.TextSimilarity.TextCorpus;
import edu.brown.cs.term_project.nlp.TextProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleVertex implements INode<Similarity>, IText {
  private final List<Similarity> similarities;
  private final Article article;
  private final Map<IWord, Double> entities;
  private final Map<IWord, Double> words;
  private final Map<IWord, Double> title;
  private Map<IWord, Double> entitiesImportance;
  private Map<IWord, Double> wordsImportance;
  private Map<IWord, Double> titleImportance;

  public ArticleVertex(Article article, String text, Map<IWord, Double> entities) {
    this.similarities = new ArrayList<>();
    this.article = article;
    this.entities = entities;
    this.words = setWords(text, false);
    this.title = setWords(article.getTitle(), true);
  }

  @Override
  public List<Similarity> getEdges() {
    return similarities;
  }

  /**
   * Gets the distance to another node by finding the relevant edge, and getting the distance
   * @param dst - destination node
   * @return - distance to dst
   */
  @Override
  public double getDistance(INode<Similarity> dst) {
    if (dst.equals(this)) {
      return 0;
    } else {
      for (Similarity e: similarities) {
        if (e.getSource().equals(dst) || e.getDest().equals(dst)) {
          return e.getDistance();
        }
      }
      return -1;
    }
  }

  @Override
  public Integer getId() {
    return article.getId();
  }

  public Article getArticle() {
    return article;
  }

  /**
   * Sets edges of Article, by parsing through list of Similarities and finding the ones that include itself
   * @param newSimilarities - similarities to pull edges from
   */
  public void setEdges(List<Similarity> newSimilarities) {
    for (Similarity similarity: newSimilarities) {
      if (similarity.getSource().equals(this) || similarity.getDest().equals(this)) {
        this.similarities.add(similarity);
      }
    }
  }

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

  private Map<IWord, Double> setWords(String text, boolean isTitle) {
    HashMap<IWord, Double> wordMap = new HashMap<>();
    String[] splitWords;
    if (isTitle) {
      splitWords = TextProcessing.lemmizeText(text);
    } else {
      splitWords = text.split("~\\^");
    }

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

  public void setImportance(TextCorpus<Entity, ArticleVertex> entityCorpus, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<ArticleWord, ArticleVertex> titleCorpus) {
    this.entitiesImportance = entityCorpus.getImportanceMap(getFreq(0));
    this.wordsImportance = wordCorpus.getImportanceMap(getFreq(1));
    this.titleImportance = titleCorpus.getImportanceMap((getFreq(2)));
  }

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

  public Map<IWord, Double> getEntitiesImportance() {
    return entitiesImportance;
  }


  public Map<IWord, Double> getWordsImportance() {
    return wordsImportance;
  }


  public Map<IWord, Double> getTitleImportance() {
    return titleImportance;
  }

  @Override
  public String toString() {
    return "ArticleVertex{" +
        "article=" + article +
        ", entitiesImportance=" + entitiesImportance +
        ", wordsImportance=" + wordsImportance +
        ", titleImportance=" + titleImportance +
        '}';
  }
}
