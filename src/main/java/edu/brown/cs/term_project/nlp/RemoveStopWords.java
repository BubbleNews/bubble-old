package edu.brown.cs.term_project.nlp;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class RemoveStopWords {
  private static final Set<String> stopWords = setStopWords();

  private RemoveStopWords() {}
  /**.
   * Static method to parse a csv from file name
   * @return Pair containing the filename, and a list of arrays
   *      Each item in list is a line
   *      Each element of the array is a string of the csv line, split on commmas
   * @throws IOException thrown when file is unable to be found
   */
  public static Set<String> setStopWords() {
    Set<String> words = new HashSet<>();
    // BufferedReader to read file
    try {
      FileReader file = new FileReader("data/stopwords.csv");
      BufferedReader csvReader = new BufferedReader(file);

      String row = csvReader.readLine();
      // Splits rows on commas
      while (row != null) {
        words.add(row);
        row = csvReader.readLine();
      }
      csvReader.close();
    } catch (IOException e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
    }
    return words;
  }

  public static boolean testWord(String word) {
    return stopWords.contains(word) || word.length() <= 1 || word.matches("[a-z]+\\.[a-z]+");
  }
}
