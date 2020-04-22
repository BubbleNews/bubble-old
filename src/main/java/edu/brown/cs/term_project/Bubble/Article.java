package edu.brown.cs.term_project.Bubble;


import edu.brown.cs.term_project.Graph.INode;
import edu.brown.cs.term_project.TextSimilarity.IText;
import edu.brown.cs.term_project.TextSimilarity.IWord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Article implements IText {
  private int id;
  private String title;
  private String date;
  private String author;
  private String url;
  private HashMap<Entity, Double> entities;
  private HashMap<Vocab, Double> words;

  public Article(int id, String title, String date, String author, String url, String text) {
    this.id = id;
    this.title = title;
    this.date = date;
    this.author = author;
    this.url = url;
    setEntities();
    setWords(text);
  }

  public Article(int id) {
    this.id = id;
  }

  @Override
  public Map<IWord, Double> getFreq(Integer textType) {
    if (textType == 0) {
      return new HashMap<>(entities);
    } else if (textType == 1) {
      return new HashMap<>(words);
    } else {
      return null;
    }
  }

  public int getId() {
    return id;
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
