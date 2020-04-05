package edu.brown.cs.term_project.Stars;

import edu.brown.cs.term_project.REPL.StartREPL;
import edu.brown.cs.term_project.Stars.GetQuery;
import edu.brown.cs.term_project.Stars.ReadStars;
import edu.brown.cs.term_project.Stars.StarMain;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**.
 * Class to test GetQuery
 */
public class GetQueryTest {
  private List<String> info;
  private GetQuery queryN;
  private GetQuery queryR;
  private PrintWriter pw;

  /**.
   * Set Up Method to test neighbors command, with ten-star data
   */
  @Before
  public void setUpN() {
    queryN = new GetQuery(true);
    queryR = new GetQuery(false);
    StartREPL.getStartCM().add("stars", new ReadStars());
    StartREPL.getStartCM().add("neighbors", queryN);
    StartREPL.getStartCM().add("radius", queryR);
    StartREPL.getStartCM().action("stars data/stars/ten-star.csv", new PrintWriter(System.out));
    info = new ArrayList<String>();
    pw = new PrintWriter(System.out);
    info.add("neighbors");
  }

  /**. 
   * Set Up method to test radius command, ten-star data
   */
  @Before
  public void setUpR() {
    queryN = new GetQuery(true);
    queryR = new GetQuery(false);
    StartREPL.getStartCM().add("stars", new ReadStars());
    StartREPL.getStartCM().add("neighbors", queryN);
    StartREPL.getStartCM().add("radius", queryR);
    StartREPL.getStartCM().action("stars data/stars/ten-star.csv", new PrintWriter(System.out));
    info = new ArrayList<String>();
    pw = new PrintWriter(System.out);
    info.add("radius");
  }

  /**.
   * Tear Down method for testing GetQuery
   */
  @After
  public void tearDown() {
    info = null;
    queryN = null;
    queryR = null;
    pw = null;
  }

  /**.
   * test neighbor command and extra element error
   */
  @Test
  public void testGetQueryN1() {
    setUpN();
    info.add(Integer.toString(5));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    assertEquals(queryN.execute(info, pw), "0\n" + "70667\n" + "71454\n"
        + "71457\n" + "87666");
    info.add("another string");
    assertTrue(queryN.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * test neighbors command with a star name
   */
  @Test
  public void testGetQueryN2() {
    setUpN();
    info.add(Integer.toString(5));
    info.add("Sol");
    assertEquals(queryN.execute(info, pw), "70667\n" + "71454\n" + "71457\n"
        + "87666\n" + "118721");
    tearDown();
  }

  /**.
   * test neighbors command with no results returned
   */
  @Test
  public void testGetQueryN3() {
    setUpN();
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    assertEquals(queryN.execute(info, pw), "");
    tearDown();
  }

  /**.
   * test neighbors for negative k value
   */
  @Test
  public void testGetQueryN4() {
    setUpN();
    info.add(Integer.toString(-1));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    assertTrue(queryN.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * test neighbors for star name with multiple words
   */
  @Test
  public void testGetQueryN5() {
    setUpN();
    info.add(Integer.toString(5));
    info.add("\"Rigel");
    info.add("Kentaurus");
    info.add("B\"");
    assertEquals(queryN.execute(info, pw), "71457\n" + "70667\n" + "0\n"
        + "87666\n" + "118721");
    tearDown();
  }

  /**.
   * Test neighbors when k is not a number
   */
  @Test
  public void testGetQueryN6() {
    setUpN();
    info.add("hjgjhgljv");
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    assertTrue(queryN.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * Test Neighbors when star name doesn't exist
   */
  @Test
  public void testGetQueryN7() {
    setUpN();
    info.add(Integer.toString(5));
    info.add("raffley");
    assertTrue(queryN.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * test radius command and extra element error
   */
  @Test
  public void testGetQueryNR() {
    setUpR();
    info.add(Integer.toString(5));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    assertEquals(queryR.execute(info, pw), "0\n" + "70667\n" + "71454\n"
        + "71457\n" + "87666\n" + "118721");
    info.add("another string");
    assertTrue(queryR.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * test radius command with a star name
   */
  @Test
  public void testGetQueryR2() {
    setUpR();
    info.add(Integer.toString(5));
    info.add("\"Sol\"");
    assertEquals(queryR.execute(info, pw), "70667\n" + "71454\n" + "71457\n"
        + "87666\n" + "118721");
    tearDown();
  }

  /**.
   * test radius command with no results returned
   */
  @Test
  public void testGetQueryR3() {
    setUpR();
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    assertEquals(queryR.execute(info, pw), "");
    tearDown();
  }

  /**.
   * test radius for negative k value
   */
  @Test
  public void testGetQueryR4() {
    setUpR();
    info.add(Integer.toString(-1));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    assertTrue(queryR.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * test radius for star name with multiple words
   */
  @Test
  public void testGetQueryR5() {
    setUpR();
    info.add(Double.toString(5.01));
    info.add("\"Rigel");
    info.add("Kentaurus");
    info.add("B\"");
    assertEquals(queryR.execute(info, pw), "71457\n" + "70667\n" + "0\n"
        + "87666\n" + "118721");
    tearDown();
  }

  /**.
   * Test radius when k is not a number
   */
  @Test
  public void testGetQueryR6() {
    setUpR();
    info.add("hjgjhgljv");
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    info.add(Integer.toString(0));
    assertTrue(queryR.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * Test radius when star name doesn't exist
   */
  @Test
  public void testGetQueryR7() {
    setUpR();
    info.add(Integer.toString(5));
    info.add("raffley");
    assertTrue(queryR.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * Test Neighbors when no attributes given
   */
  @Test
  public void testGetQueryN8() {
    setUpN();
    assertTrue(queryN.execute(info, pw).contains("ERROR:"));
    tearDown();
  }


}

