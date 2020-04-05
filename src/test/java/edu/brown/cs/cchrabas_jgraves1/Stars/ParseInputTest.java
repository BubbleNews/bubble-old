package edu.brown.cs.cchrabas_jgraves1.Stars;

import edu.brown.cs.cchrabas_jgraves1.Stars.ParseInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**.
 * Class to test ParseInput
 */
public class ParseInputTest {

  /**.
   * tests parse input on complicated star name set of strings
   */
  @Test
  public void testReadStars() {
    List<String> list = new ArrayList<String>(
        Arrays.asList("radius", "lll\"", "\"jjj\"jj", "kkkk", "klj\"iuou\"", "Not this"));
    assertEquals(ParseInput.parseName(list).get(2), "jjj\"jj kkkk klj\"iuou");
    assertEquals(ParseInput.parseName(list).get(1), "lll\"");
    assertEquals(ParseInput.parseName(list).get(3), "Not this");
  }


}
