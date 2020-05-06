package edu.brown.cs.term_project.language;

import edu.brown.cs.term_project.bubble.Entity;
import edu.stanford.nlp.simple.*;

import java.util.*;

/**
 * Utility class that extracts the entities from an article using the stanford core NLP library.
 * https://stanfordnlp.github.io/CoreNLP/simple.html
 */
public final class TextProcessing {
  private static final Set<String> ignoredEntities = new HashSet<>(
          Arrays.asList("NUMBER", "CRIMINAL_CHARGE", "DATE", "MONEY", "DURATION", "TIME", "ORDINAL", "O"));

  /**
   * Private constructor that is only defined because this is a utility class
   * and we don't want a default constructor.
   */
  private TextProcessing() {
    throw new AssertionError("This constructor should never be called.");
  }

  /**
   * Gets the entity frequencies for a text string. Ignores entities of the following types because
   * they aren't useful in calculating similarity between articles:
   * "NUMBER", "CRIMINAL_CHARGE", "DATE", "MONEY", "DURATION", "TIME", "ORDINAL", "O"
   * @param articleBody the string text of an article
   * @return a hashmap of entities and the number of times they occurred in an article.
   */
  public static HashMap<Entity, Integer> getEntityFrequencies(String articleBody) {
    HashMap<Entity, Integer> entityFrequencies = new HashMap<>();
    Document doc = new Document(articleBody);
    for (Sentence sent : doc.sentences()) {
      List<String> entityTypes = sent.nerTags();
      for (int i = 0; i < entityTypes.size(); i++) {
        String word = sent.word(i);
        String entityType = entityTypes.get(i);
        if (!RemoveStopWords.isStopWord(word) && !TextProcessing.ignoredEntities.contains(entityType)) {
          Entity entity = new Entity(word, entityType);
          if (entityFrequencies.containsKey(entity)) {
            entityFrequencies.replace(entity, entityFrequencies.get(entity) + 1);
          } else {
            entityFrequencies.put(entity, 1);
          }
        }
      }
    }
    return entityFrequencies;
  }

  /**
   * Uses the Stanford NLP library to lemmatize an article (this is a more intelligent version
   * of stemming)
   * @param text input article
   * @return array where each word is lemmatized
   */
  public static String[] lemmatizeText(String text) {
    List<String> lemmas = new ArrayList<>();
    Document doc = new Document(text.toLowerCase());
    for (Sentence sent : doc.sentences()) {
      for (String s: sent.lemmas()) {
        if (!RemoveStopWords.isStopWord(s)) {
          lemmas.add(s);
        }
      }
    }
    return lemmas.toArray(new String[0]);
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
      if (word.length() == 1) {
        int w = word.length();
      }
      if (word.equals("and")) {
        int w = word.length();
      }
      if (!alreadySeen.contains(word) && !RemoveStopWords.isStopWord(word)) {
        int w = word.length();
        frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
        alreadySeen.add(word);
      }
    }
  }
}
