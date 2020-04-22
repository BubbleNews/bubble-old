package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.TextSimilarity.IWord;

public class Entity implements IWord {
  private String entity;
  private String classType;

  public Entity(String entity, String classType) {
    this.entity = entity;
    this.classType = classType;
  }
  @Override
  public String getWord() {
    return null;
  }

  public String getClassType() {
    return classType;
  }
}
