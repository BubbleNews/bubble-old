package edu.brown.cs.term_project.TIMDB;

import edu.brown.cs.term_project.REPL.StartREPL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to test ActorExplorer methods.
 */
public class ActorExplorerTest {
  private ActorExplorer ae;
  private Actor a1;
  private Actor a2;
  private Actor a3;
  private Actor a4;
  private Movie m1;
  private Movie m2;
  private Movie m3;
  private List<Movie> l1 = new ArrayList<Movie>();
  private List<Movie> l2 = new ArrayList<Movie>();
  private List<Movie> l3 = new ArrayList<Movie>();
  private List<Movie> l4 = new ArrayList<Movie>();
  private List<Actor> l5 = new ArrayList<Actor>();
  private List<Actor> l6 = new ArrayList<Actor>();

  /**.
   * Set Up Method to Connect
   */
  @Before
  public void setUpAE() {
    StartREPL.getStartCM().add("mdb", new LoadDB());
    StartREPL.getStartCM().action("mdb data/timdb/smallTimdb.sqlite3", new PrintWriter(System.out));
    ae = TIMDBMain.getActorExplorer();
    a1 = new Actor("/m/01nfys", "Steve Coogan");
    a2 = new Actor("/m/0154qm", "Cate BlanChett");
    a3 = new Actor("/m/0gn30", "Sylvester Stallone");
    a4 = new Actor("/m/0194r1", "Amy Stiller");
    m1 = new Movie(a1, a2, "Hot Fuzz", "/m/08k40m", 1/2);
    m2 = new Movie(a4, a3, "Lovers and Other Strangers", "/m/04x3kb", 1/2);
    m3 = new Movie(null, null, "Tropic Thunder", "/m/02ryz24", 0);
    l1.add(m1);
    l2.add(m2);
    l4.add(m3);
    l4.add(m2);
    l5.add(a2);
    l5.add(a1);
  }

  /**.
   * Tear Down method for testing GetQuery
   */
  @After
  public void tearDown() {
    ae = null;
    a1 = null;
    a2 = null;
    a3 = null;
    a4 = null;
    m1 = null;
    m2 = null;
    m3 = null;
    l1 = null;
    l2 = null;
    l3 = null;
    l4 = null;
    l5 = null;
    l6 = null;
  }

  /**
   * Check if two movie lists are equal.
   */
  public boolean testEqualMovies(List<Movie> list1, List<Movie> list2) {
    for (int i=0; i < list1.size(); i++) {
      if (!list1.get(i).getID().equals(list2.get(i).getID())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if two actor lists are equal.
   */
  public boolean testEqualActors(List<Actor> list1, List<Actor> list2) {
    for (int i=0; i < list1.size(); i++) {
      if (!list1.get(i).getID().equals(list2.get(i).getID())) {
        return false;
      }
    }
    return true;
  }

  /**.
   * test neighbor command and extra element error
   */
  @Test
  public void testGetQueryN1() throws SQLException {
    setUpAE();
    assertTrue(testEqualMovies(ae.explore(a1), l1));
    assertTrue(testEqualMovies(ae.explore(a3), l3));
    assertTrue(testEqualMovies(ae.explore(a4), l2));
    assertEquals(ae.lookupName("/m/01nfys"), "Steve Coogan");
    assertEquals(ae.lookupID("Amy Stiller"), "/m/0194r1");
    assertEquals(ae.lookupMovie("/m/02ryz24"), "Tropic Thunder");
    assertEquals(ae.lookupName("none"), null);
    assertEquals(ae.lookupID("None"), null);
    assertEquals(ae.lookupMovie("none"), null);
    assertTrue(testEqualMovies(ae.findMovies("/m/0194r1"), l4));
    assertTrue(testEqualActors(ae.findActors("/m/08k40m"), l5));
    assertTrue(testEqualMovies(ae.findMovies("none"), l3));
    assertTrue(testEqualActors(ae.findActors("none"), l6));

    tearDown();
  }


}
