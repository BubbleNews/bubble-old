package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.TextSimilarity.IWord;

import java.util.Objects;

public class Entity implements IWord {
  private String entity;
  private String classType;

  public Entity(String entity, String classType) {
    this.entity = entity;
    this.classType = classType;
  }
  @Override
  public String getWord() {
    return entity;
  }

  public String getClassType() {
    return classType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Entity entity1 = (Entity) o;
    return Objects.equals(entity, entity1.entity) &&  Objects.equals(classType, entity1.classType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entity, classType);
  }
}
