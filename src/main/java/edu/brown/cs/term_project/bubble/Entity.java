package edu.brown.cs.term_project.bubble;

import edu.brown.cs.term_project.similarity.IWord;

import java.util.Objects;

/**
 * A class to store entities, which are important key-words the Stanford CoreNLP
 * library extracts from texts.
 */
public class Entity implements IWord {
  private final String entity;
  private final String classType;

  /**
   * Constructor for an entity.
   * @param entity the string entity
   * @param classType the entity class
   */
  public Entity(String entity, String classType) {
    this.entity = entity;
    this.classType = classType;
  }
  @Override
  public String getWord() {
    return entity;
  }

  /**
   * Gets the entity class type produced by the CoreNLP library.
   * @return the class type.
   */
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

  @Override
  public String toString() {
    return entity;
  }
}
