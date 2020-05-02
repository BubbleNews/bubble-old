package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.INode;
import edu.brown.cs.term_project.TextSimilarity.IText;
import edu.brown.cs.term_project.TextSimilarity.IWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleVertex implements INode<Similarity>, IText {
  private List<Similarity> similarities;
  private Article article;
  private Map<Entity, Double> entities;
  private Map<ArticleWord, Double> words;
  private Map<TitleWord, Double> title;
  private Map<Entity, Double> entitiesImportance;
  private Map<ArticleWord, Double> wordsImportance;
  private Map<TitleWord, Double> titleImportance;

  public ArticleVertex(Article article, String text, Map<Entity, Double> entities) {
    this.similarities = new ArrayList<>();
    this.article = article;
    this.entities = entities;
    setWords(text);
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

  private void setWords(String text) {
    this.words = new HashMap<>();
    String[] splitWords = text.split(" ");
    for (String word: splitWords) {
      ArticleWord articleWord = new ArticleWord(word);
      if (words.containsKey(articleWord)) {
        words.replace(articleWord, words.get(articleWord) + 1);
      } else {
        words.put(articleWord, 1.0);
      }
    }
  }

  @Override
  public Map<IWord, Double> getFreq(Integer textType) {
    if (textType == 0) {
      return new HashMap<>(entities);
    } else if (textType == 1) {
      return new HashMap<>(words);
    } else if (textType == 2) {
      return new HashMap<>(title);
    } else {
      return null;
    }
  }

//  public void setImportance(Integer textType) {
//    if (textType == 0) {
//      return new HashMap<>(entities);
//    } else if (textType == 1) {
//      return new HashMap<>(words);
//    } else if (textType == 2) {
//      return new HashMap<>(title);
//    } else {
//      return null;
//    }
//  }

  public Map<Entity, Double> getEntitiesImportance() {
    return entitiesImportance;
  }

  public void setEntitiesImportance(Map<Entity, Double> entitiesImportance) {
    this.entitiesImportance = entitiesImportance;
  }

  public Map<ArticleWord, Double> getWordsImportance() {
    return wordsImportance;
  }

  public void setWordsImportance(Map<ArticleWord, Double> wordsImportance) {
    this.wordsImportance = wordsImportance;
  }

  public Map<TitleWord, Double> getTitleImportance() {
    return titleImportance;
  }

  public void setTitleImportance(Map<TitleWord, Double> titleImportance) {
    this.titleImportance = titleImportance;
  }
}
