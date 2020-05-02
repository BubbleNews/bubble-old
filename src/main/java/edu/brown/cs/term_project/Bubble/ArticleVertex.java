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
  private final Map<Entity, Double> entities;
  private final Map<ArticleWord, Double> words;
  private final Map<ArticleWord, Double> title;
  private Map<IWord, Double> entitiesImportance;
  private Map<IWord, Double> wordsImportance;
  private Map<IWord, Double> titleImportance;

  public ArticleVertex(Article article, String text, Map<Entity, Double> entities) {
    this.similarities = new ArrayList<>();
    this.article = article;
    this.entities = entities;
    this.words = setWords(text);
    this.title = setWords(article.getTitle());
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

  private Map<ArticleWord, Double> setWords(String text) {
    HashMap<ArticleWord, Double> wordMap = new HashMap<>();
    String[] splitWords = TextProcessing.lemmizeText(text);
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



  public void setImportance(TextCorpus<Entity, ArticleVertex> entityCorpus, TextCorpus<ArticleWord,
      ArticleVertex> wordCorpus, TextCorpus<ArticleWord, ArticleVertex> titleCorpus) {
    this.entitiesImportance = entityCorpus.getImportanceMap(getFreq(0));
    this.wordsImportance = wordCorpus.getImportanceMap(getFreq(1));
    this.titleImportance = titleCorpus.getImportanceMap((getFreq(2)));
  }

  public Map<IWord, Double> getImportance(Integer textType) {
    if (textType == 0) {
      return new HashMap<>(entitiesImportance);
    } else if (textType == 1) {
      return new HashMap<>(wordsImportance);
    } else if (textType == 2) {
      return new HashMap<>(titleImportance);
    } else {
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

}
