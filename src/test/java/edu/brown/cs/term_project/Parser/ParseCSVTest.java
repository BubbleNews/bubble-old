package edu.brown.cs.term_project.Parser;

import edu.brown.cs.term_project.Parser.Pair;
import edu.brown.cs.term_project.Parser.ParseCSV;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**.
 * Class to test ParseCSV
 */
public class ParseCSVTest {

  /**.
   * Tests that one star csv parses correctly
   * @throws IOException - If files couldn't be read
   */
  @Test
  public void testReadStars() throws IOException {
    String[] array = new String[]{Integer.toString(1), "Lonely Star", Integer.toString(5),
        Double.toString(-2.24), Double.toString(10.04)};
    List<String[]> list = new ArrayList<>();
    list.add(array);
    Pair<String, List<String[]>> pair
        = new Pair<String, List<String[]>>("data/stars/one-star.csv", list);
    assertEquals(ParseCSV.parse("data/stars/one-star.csv", 1).getFirst(), pair.getFirst());
    int len = pair.getSecond().get(0).length;
    for (int i = 0; i < len; i++) {
      assertTrue(ParseCSV.parse("data/stars/one-star.csv", 1).getSecond().get(0)[0].equals(
          pair.getSecond().get(0)[0]));
    }
  }

  /**.
   * Tests that IOException is thrown with non-existant file
   * @throws IOException
   */
  @Test(expected = FileNotFoundException.class)
  public void testReadStars2() throws IOException {
    Pair<String, List<String[]>> parsed = ParseCSV.parse("data/stars/garbage.csv", 1);
  }
}
