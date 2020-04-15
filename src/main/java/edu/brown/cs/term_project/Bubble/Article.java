package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.INode;
import edu.brown.cs.term_project.TextSimilarity.IText;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Article implements INode<Similarity>, IText {
  private String id;
  private String title;
  private String date;
  private String author;
  private String url;
  private List<Similarity> similarities;
  private HashMap<Entity, Double> entities;
  private HashMap<Vocab, Double> words;

  public Article(String id, String title, String date, String author, String url, List<Similarity> similarities) {
    this.id = id;
    this.title = title;
    this.date = date;
    this.author = author;
    this.url = url;
    this.similarities = similarities;
    setEntities();
    setWords();
  }

  @Override
  public HashMap getFreq(Integer textType) {
    if (textType == 0) {
      return entities;
    } else if (textType == 1) {
      return words;
    } else {
      return null;
    }
  }

  @Override
  public List<Similarity> getEdges() {
    return similarities;
  }

  public void setEdges(List<Similarity> similarities) {
    this.similarities = similarities;
  }

  // TODO: write get similarity
  @Override
  public Similarity getEdge(INode<Similarity> dst) {
    return null;
  }

  public String getID() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  // TODO: write setEntities and setWords
  public void setEntities() {
    this.entities = null;
  }

  public void setWords() {
    this.words = null;
  }
}
