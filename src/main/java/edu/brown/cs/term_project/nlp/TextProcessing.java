package edu.brown.cs.term_project.nlp;

import edu.brown.cs.term_project.Bubble.Article;
import edu.brown.cs.term_project.Bubble.Entity;
import edu.stanford.nlp.simple.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class that extracts the entities from an article using the stanford core NLP library.
 * https://stanfordnlp.github.io/CoreNLP/simple.html
 */
public final class TextProcessing {

  /**
   * Private constructor that is only defined because this is a utility class and we don't want a default constructor.
   */
  private TextProcessing() {
    throw new AssertionError("This constructor should never be called.");
  }

  /**
   * Gets the entity frequencies for a text string.
   * @param articleBody the string text of an article
   * @return a hashmap of entities and the number of times they occurred in an article.
   */
  public static HashMap<Entity, Integer> getEntityFrequencies(String articleBody) {
    HashMap<Entity, Integer> entityFrequencies = new HashMap<>();
    Document doc = new Document(articleBody);
    for (Sentence sent : doc.sentences()) {
      System.out.println(sent);
      System.out.println(sent.nerTags());
    }
    //TODO: implement

    return entityFrequencies;
  }

  public static String[] lemmizeText(String text) {
    return new String[0];
  }

  /**
   * Adds to a hashmap keeping track of number of articles a word has appeared in.
   * @param frequencies the frequency map
   * @param words a string array of words
   */
  public static void updateOccurrenceMap(HashMap<String, Integer> frequencies, String[] words) {
    Set<String> alreadySeen = new HashSet<>();
    for (String word: words) {
      // we are keeping track of number of articles a word has appeared in, so
      // if we already saw word in this article we can ignore
      if (!alreadySeen.contains(word)) {
        frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
        alreadySeen.add(word);
      }
    }
  }

}