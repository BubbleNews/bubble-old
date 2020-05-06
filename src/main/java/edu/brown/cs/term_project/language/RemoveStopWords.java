package edu.brown.cs.term_project.language;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for removing stop words from articles.
 */
public final class RemoveStopWords {
  private static final Set<String> STOP_WORDS = setStopWords();

  /**
   * Constructor should not be called.
   */
  private RemoveStopWords() {
    // should not be called
  }

  /**.
   * Static method to parse a csv from file name.
   * @return Pair containing the filename, and a list of arrays
   *      Each item in list is a line
   *      Each element of the array is a string of the csv line, split on commmas
   */
  private static Set<String> setStopWords() {
    Set<String> words = new HashSet<>();
    try {
      FileReader file = new FileReader("data/stopwords.csv");
      try (BufferedReader csvReader = new BufferedReader(file)) {
        String row = csvReader.readLine();
        // Splits rows on commas
        while (row != null) {
          words.add(row);
          row = csvReader.readLine();
        }
      }
    } catch (IOException e) {
      // this should never happen because we are providing the file
      System.out.println(Arrays.toString(e.getStackTrace()));
    }
    return words;
  }

  /**
   * Returns true for words with less than 2 characters and words that are in
   * the hashset of common stop words.
   * @param word input words
   * @return true if it is a stop word, false otherwise
   */
  public static boolean isStopWord(String word) {
    return STOP_WORDS.contains(word) || word.length() <= 2;
  }
}
