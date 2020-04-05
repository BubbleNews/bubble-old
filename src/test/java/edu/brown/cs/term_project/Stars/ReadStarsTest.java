package edu.brown.cs.term_project.Stars;

import edu.brown.cs.term_project.Stars.ReadStars;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**.
 * Class to test Read Stars
 */
public class ReadStarsTest {
  private List<String> info;
  private ReadStars read;
  private PrintWriter pw;

  /**.
   * Set Up method for testing ReadStars
   */
  @Before
  public void setUp() {
    read = new ReadStars();
    info = new ArrayList<String>();
    pw = new PrintWriter(System.out);
    info.add("stars");
  }

  /**.
   * TearDown Method for ReadStars
   */
  @After
  public void tearDown() {
    info = null;
    read = null;
    pw = null;
  }

  /**.
   * Tests Read Stars for ten-star data, various unreadable docs
   */
  @Test
  public void testReadStars() {
    setUp();
    info.add("data/stars/ten-star.csv");
    assertEquals(read.execute(info, pw), "Read 10 stars from data/stars/ten-star.csv");
    info.add("another string");
    assertTrue(read.execute(info, pw).contains("ERROR:"));
    tearDown();
    // Another Test
    setUp();
    info.add("Not/A/File");
    assertTrue(read.execute(info, pw).contains("ERROR:"));
    tearDown();
    // One more
    setUp();
    info.add("data/stars/noStars.txt");
    assertTrue(read.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

}
