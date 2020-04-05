package edu.brown.cs.cchrabas_jgraves1.Dijkstra;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**.
 * Class to Test Dijkstra
 */
public class DijkstraTest {
  private Dijkstra<GenericVertex, GenericEdge> d1;



  /**.
   * Set up method for testing Dijkstra
   */
  @Before
  public void setUp1() {
    GenericVertex a = new GenericVertex("a");
    d1 = new Dijkstra<GenericVertex, GenericEdge>(a, a);
  }

  /**.
   * Set up method for testing Dijkstra
   */
  @Before
  public void setUp2() {
    GenericVertex a = new GenericVertex("a");
    GenericVertex b = new GenericVertex("b");
    GenericEdge ab = new GenericEdge(a, b, 2);
    List<GenericEdge> aEdges = new ArrayList<>();
    aEdges.add(ab);
    a.setEdges(aEdges);
    d1 = new Dijkstra<GenericVertex, GenericEdge>(a, b);
  }
  /**.
   * Set up method for testing Dijkstra (No Path)
   */
  @Before
  public void setUpNoPath() {
    GenericVertex a = new GenericVertex("a");
    GenericVertex b = new GenericVertex("b");
    GenericEdge ab = new GenericEdge(a, b, 2);
    List<GenericEdge> aEdges = new ArrayList<>();
    aEdges.add(ab);
    a.setEdges(aEdges);
    d1 = new Dijkstra<GenericVertex, GenericEdge>(b, a);
  }

  /**.
   * Set up method for testing Dijkstra - 3 stars
   */
  @Before
  public void setUp3() {
    GenericVertex a = new GenericVertex("a");
    GenericVertex b = new GenericVertex("b");
    GenericVertex c = new GenericVertex("c");
    GenericEdge ab = new GenericEdge(a, b, 15);
    GenericEdge ba = new GenericEdge(b, a, 20);
    GenericEdge ac = new GenericEdge(a, c, 5);
    GenericEdge cb = new GenericEdge(c, b, 5);
    List<GenericEdge> aEdges = new ArrayList<>();
    List<GenericEdge> bEdges = new ArrayList<>();
    List<GenericEdge> cEdges = new ArrayList<>();
    aEdges.add(ab);
    aEdges.add(ac);
    bEdges.add(ba);
    cEdges.add(cb);
    a.setEdges(aEdges);
    b.setEdges(bEdges);
    c.setEdges(cEdges);
    d1 = new Dijkstra<GenericVertex, GenericEdge>(a, b);
  }

  /**.
   * Set up method for testing Dijkstra - 4 stars
   */
  @Before
  public void setUp4() {
    GenericVertex a = new GenericVertex("a");
    GenericVertex b = new GenericVertex("b");
    GenericVertex c = new GenericVertex("c");
    GenericEdge ab = new GenericEdge(a, b, 15);
    GenericEdge ba = new GenericEdge(b, a, 20);
    GenericEdge ac = new GenericEdge(a, c, 5);
    GenericEdge cb = new GenericEdge(c, b, 5);
    List<GenericEdge> aEdges = new ArrayList<>();
    List<GenericEdge> bEdges = new ArrayList<>();
    List<GenericEdge> cEdges = new ArrayList<>();
    aEdges.add(ab);
    aEdges.add(ac);
    bEdges.add(ba);
    cEdges.add(cb);
    a.setEdges(aEdges);
    b.setEdges(bEdges);
    c.setEdges(cEdges);
    d1 = new Dijkstra<GenericVertex, GenericEdge>(b, a);
  }

  /**.
   * Set up method for testing Dijkstra - 10 stars
   */
  @Before
  public void setUp10() {
    GenericVertex a = new GenericVertex("a");
    GenericVertex b = new GenericVertex("b");
    GenericVertex c = new GenericVertex("c");
    GenericVertex d = new GenericVertex("d");
    GenericVertex e = new GenericVertex("e");
    GenericVertex f = new GenericVertex("f");
    GenericVertex g = new GenericVertex("g");
    GenericVertex h = new GenericVertex("h");
    GenericVertex i = new GenericVertex("i");
    GenericVertex j = new GenericVertex("j");
    GenericEdge ab = new GenericEdge(a, b, 35);
    GenericEdge bc = new GenericEdge(b, c, 5);
    GenericEdge ca = new GenericEdge(c, a, 20);
    GenericEdge db = new GenericEdge(d, b, 22);
    GenericEdge df = new GenericEdge(d, f, 35);
    GenericEdge dg = new GenericEdge(d, g, 51);
    GenericEdge ed = new GenericEdge(e, d, 18);
    GenericEdge fd = new GenericEdge(f, d, 35);
    GenericEdge fj = new GenericEdge(f, j, 162);
    GenericEdge ga = new GenericEdge(g, a, 100);
    GenericEdge gd = new GenericEdge(g, d, 51);
    GenericEdge gi = new GenericEdge(g, i, 12);
    GenericEdge ig = new GenericEdge(i, g, 12);
    GenericEdge ji = new GenericEdge(j, i, 11);
    List<GenericEdge> aEdges = new ArrayList<>();
    List<GenericEdge> bEdges = new ArrayList<>();
    List<GenericEdge> cEdges = new ArrayList<>();
    List<GenericEdge> dEdges = new ArrayList<>();
    List<GenericEdge> eEdges = new ArrayList<>();
    List<GenericEdge> fEdges = new ArrayList<>();
    List<GenericEdge> gEdges = new ArrayList<>();
    List<GenericEdge> hEdges = new ArrayList<>();
    List<GenericEdge> iEdges = new ArrayList<>();
    List<GenericEdge> jEdges = new ArrayList<>();
    aEdges.add(ab);
    bEdges.add(bc);
    cEdges.add(ca);
    dEdges.add(db);
    dEdges.add(df);
    dEdges.add(dg);
    eEdges.add(ed);
    fEdges.add(fd);
    fEdges.add(fj);
    gEdges.add(ga);
    gEdges.add(gd);
    gEdges.add(gi);
    iEdges.add(ig);
    jEdges.add(ji);
    a.setEdges(aEdges);
    b.setEdges(bEdges);
    c.setEdges(cEdges);
    d.setEdges(dEdges);
    e.setEdges(eEdges);
    f.setEdges(fEdges);
    g.setEdges(gEdges);
    h.setEdges(hEdges);
    i.setEdges(iEdges);
    j.setEdges(jEdges);
    d1 = new Dijkstra<GenericVertex, GenericEdge>(e, j);
  }

  /**
   * Method to convert path to String
   * @param list - list of edges in path
   * @return - path as a string
   */
  public String convert(List<GenericEdge> list) {
    String toReturn = "";
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getStart() != null) {
        toReturn += list.get(i).getStart().getName();
      }
      if (i == list.size() - 1) {
        toReturn += list.get(i).getEnd().getName();
      }
    }
    return toReturn;
  }

  /**.
   * Tear Down method for testing Dijkstra
   */
  @After
  public void tearDown() {
    d1 = null;
  }

  /**.
   * Tests that NullPointerException is thrown with no path
   * @throws NullPointerException
   */
  @Test(expected = NullPointerException.class)
  public void testNoShortest() throws NullPointerException {
    setUpNoPath();
    double path = d1.findDistance();
  }

  /**.
   * Tests that NullPointerException is thrown with no shortest distance
   * @throws NullPointerException
   */
  @Test(expected = NullPointerException.class)
  public void testNoPath() throws NullPointerException {
    setUpNoPath();
    String path = convert(d1.findPath());
  }

  /**.
   * Test Dijkstra path to itself
   */
  @Test
  public void testDijkstra1() throws SQLException, ExecutionException {
    setUp1();
    assertEquals(convert(d1.findPath()), "a");
    assertTrue(d1.findDistance() == 0);
    tearDown();
  }

  /**.
   * Test Dijkstra function on 2 stars
   */
  @Test
  public void testDijkstra2() throws SQLException, ExecutionException {
    setUp2();
    assertEquals(convert(d1.findPath()), "ab");
    assertTrue(d1.findDistance() == 2);
    tearDown();
  }

  /**.
   * Test Dijkstra on 3 stars
   */
  @Test
  public void testDijkstra3() throws SQLException, ExecutionException {
    setUp3();
    assertEquals(convert(d1.findPath()), "acb");
    assertTrue(d1.findDistance() == 10);
    tearDown();
  }




  /**.
   * Test Dijkstra on 4 stars - shortest path is more steps
   */
  @Test
  public void testDijkstra4() throws SQLException, ExecutionException {
    setUp4();
    assertEquals(convert(d1.findPath()), "ba");
    assertTrue(d1.findDistance() == 20);
    tearDown();
  }

  /**.
   * Test Dijkstra on 10 stars
   */
  @Test
  public void testDijkstra10() throws SQLException, ExecutionException {
    setUp10();
    assertEquals(convert(d1.findPath()), "edfj");
    assertTrue(d1.findDistance() == 215);
    tearDown();
  }

}
