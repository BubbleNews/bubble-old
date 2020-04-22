package edu.brown.cs.term_project.Bubble;


import edu.brown.cs.term_project.Graph.INode;
import edu.brown.cs.term_project.TextSimilarity.IText;
import edu.brown.cs.term_project.TextSimilarity.IWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Article implements INode<Similarity>, IText {
  private int id;
  private String title;
  private String date;
  private String author;
  private String url;
  private List<Similarity> similarities;
  private HashMap<Entity, Double> entities;
  private HashMap<Vocab, Double> words;

  public Article(int id, String title, String date, String author, String url, String text) {
    this.id = id;
    this.title = title;
    this.date = date;
    this.author = author;
    this.url = url;
    this.similarities = new ArrayList<>();
    setEntities();
    setWords(text);
  }

  public Article(int id) {
    this.id = id;
  }

  @Override
  public HashMap<? extends IWord, Double> getFreq(Integer textType) {
    if (textType == 0) {
      return entities;
    } else if (textType == 1) {
      return words;
    } else {
      return null;
    }
  }

  public int getId() {
    return id;
  }

  @Override
  public List<Similarity> getEdges() {
    return similarities;
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

  public void addEdges(Similarity similarity) {
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
   * Gets the distance to another node by finding the relevant edge, and getting the distance
   * @param dst - destination node
   * @return - distance to dst
   */
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

  public String getTitle() {
    return title;
  }

  // TODO: write setEntities and setWords
  public void setEntities() {

  }

  public void setWords(String text) {
    this.words = null;
  }
}
