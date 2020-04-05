package edu.brown.cs.term_project.TIMDB;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to test LoadDB methods.
 */
public class LoadDBTest {
  private List<String> info;
  private LoadDB db;
  private PrintWriter pw;

  /**.
   * Set Up method for testing LoadDB
   */
  @Before
  public void setUp() {
    db = new LoadDB();
    info = new ArrayList<String>();
    pw = new PrintWriter(System.out);
    info.add("mdb");
  }

  /**.
   * TearDown Method for LoadDB
   */
  @After
  public void tearDown() {
    info = null;
    db = null;
    pw = null;
  }

  /**.
   * Tests LoadDB for various valid and invalid inputs
   */
  @Test
  public void testReadStars() {
    setUp();
    info.add("data/timdb/smallTimdb.sqlite3");
    assertEquals(db.execute(info, pw), "db set to data/timdb/smallTimdb.sqlite3");
    info.add("another string");
    assertTrue(db.execute(info, pw).contains("ERROR:"));
    tearDown();
    setUp();
    info.add("Not/A/File");
    assertTrue(db.execute(info, pw).contains("ERROR:"));
    tearDown();
    // One more
    setUp();
    info.add("data/stars/noStars.txt");
    assertTrue(db.execute(info, pw).contains("ERROR:"));
    tearDown();
    setUp();
    info.add("data/timdb/timdb.sqlite3");
    assertEquals(db.execute(info, pw), "db set to data/timdb/timdb.sqlite3");
    tearDown();
  }
}
