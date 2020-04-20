package edu.brown.cs.term_project.nlp;

import edu.stanford.nlp.simple.*;

/**
 * Utility class that extracts the entities from an article using the stanford core NLP library.
 * https://stanfordnlp.github.io/CoreNLP/simple.html
 */
public final class ExtractEntities {

  /**
   * Private constructor that is only defined because this is a utility class and we don't want a default constructor.
   */
  private ExtractEntities() {
    throw new AssertionError("This constructor should never be called.");
  }

  public static void getEntities(String articleBody) {
    Document doc = new Document(articleBody);
    for (Sentence sent : doc.sentences()) {
      System.out.println(sent);
      System.out.println(sent.nerTags());
    }
  }
}