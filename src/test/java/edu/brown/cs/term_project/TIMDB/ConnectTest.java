package edu.brown.cs.term_project.TIMDB;

import edu.brown.cs.term_project.REPL.StartREPL;
import edu.brown.cs.term_project.Stars.GetQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to test Connect methods.
 */
public class ConnectTest {
  private List<String> info;
  private Connect con;
  private PrintWriter pw;

  /**.
   * Set Up Method to Connect
   */
  @Before
  public void setUpC1() {
    con = new Connect();
    StartREPL.getStartCM().add("mdb", new LoadDB());
    StartREPL.getStartCM().add("connect", con);
    StartREPL.getStartCM().action("mdb data/timdb/smallTimdb.sqlite3", new PrintWriter(System.out));
    pw = new PrintWriter(System.out);
    info = new ArrayList<String>();
    info.add("connect");
}

  /**.
   * Tear Down method for testing GetQuery
   */
  @After
  public void tearDown() {
    info = null;
    con = null;
    pw = null;
  }

  /**.
   * test neighbor command and extra element error
   */
  @Test
  public void testGetQueryN1() {
    setUpC1();
    info.add("\"Arnold");
    info.add("Schwarzenegger\"");
    info.add("\"Cate");
    info.add("Blanchett\"");
    assertEquals(con.execute(info, pw), "Arnold Schwarzenegger -> Steve Coogan : Around the World in 80 Days\n" +
            "Steve Coogan -> Cate Blanchett : Hot Fuzz");
    info.add("another string");
    assertTrue(con.execute(info, pw).contains("ERROR:"));
    tearDown();
  }




  /**.
   * test neighbor command and extra element error
   */
  @Test
  public void testGetQueryN3() {
    setUpC1();
    info.add("\"Arnold");
    info.add("Schwarzenegger\"");
    info.add("\"Jeff");
    info.add("Bridges\"");
    assertEquals(con.execute(info, pw), "Arnold Schwarzenegger -/- Jeff Bridges");
    tearDown();
  }

  /**.
   * test neighbor command and extra element error
   */
  @Test
  public void testGetQueryN4() {
    setUpC1();
    info.add("Arnold");
    info.add("Schwarzenegger");
    info.add("\"Jeff");
    info.add("Bridges\"");
    assertTrue(con.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * test neighbor command and extra element error
   */
  @Test
  public void testGetQueryN5() {
    setUpC1();
    info.add("\"Unknown");
    info.add("Name\"");
    info.add("\"Jeff");
    info.add("Bridges\"");
    assertTrue(con.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * test neighbor command and extra element error
   */
  @Test
  public void testGetQueryN6() {
    setUpC1();
    info.add("\"Jeff");
    info.add("Bridges\"");
    assertTrue(con.execute(info, pw).contains("ERROR:"));
    tearDown();
  }

  /**.
   * Tests that error is thrown with two of the same name
   */
  @Test
  public void testSameName()  {
    setUpC1();
    info.add("\"Arnold");
    info.add("Schwarzenegger\"");
    info.add("\"Arnold");
    info.add("Schwarzenegger\"");
    assertTrue(con.execute(info, pw).contains("ERROR:"));
    tearDown();
  }
}
