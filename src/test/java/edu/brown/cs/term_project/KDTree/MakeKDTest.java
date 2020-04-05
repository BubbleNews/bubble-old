package edu.brown.cs.term_project.KDTree;

import edu.brown.cs.term_project.REPL.StartREPL;
import edu.brown.cs.term_project.Stars.ReadStars;
import edu.brown.cs.term_project.Stars.Star;
import edu.brown.cs.term_project.Stars.StarMain;

import java.io.PrintWriter;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**.
 * Class to test MakeKD
 */
public class MakeKDTest {
  private Node<Star> kdtree;

  /**.
   * ten-star MakeKD set up method
   */
  @Before
  public void setUp() {
    StartREPL.getStartCM().add("stars", new ReadStars());
    StartREPL.getStartCM().action("stars data/stars/ten-star.csv",
        new PrintWriter(System.out)); // Calls MakeKD, updating kdtree;
    kdtree = StarMain.getKD();
  }

  /**.
   * stardata MakeKD set up method
   */
  @Before
  public void setUp2() {
    StartREPL.getStartCM().add("stars", new ReadStars());
    StartREPL.getStartCM().action("stars data/stars/stardata.csv",
        new PrintWriter(System.out)); // Calls MakeKD, updating kdtree;
    kdtree = StarMain.getKD();
  }

  /**.
   * three-star MakeKD set up method
   */
  @Before
  public void setUp3() {
    StartREPL.getStartCM().add("stars", new ReadStars());
    StartREPL.getStartCM().action("stars data/stars/three-star.csv",
        new PrintWriter(System.out)); // Calls MakeKD, updating kdtree;
    kdtree = StarMain.getKD();
  }

  /**.
   * one-star MakeKD set up method
   */
  @Before
  public void setUp4() {
    StartREPL.getStartCM().add("stars", new ReadStars());
    StartREPL.getStartCM().action("stars data/stars/one-star.csv",
        new PrintWriter(System.out)); // Calls MakeKD, updating kdtree;
    kdtree = StarMain.getKD();
  }

  /**.
   * Tear Down method for MakeKD Test
   */
  @After
  public void tearDown() {
    kdtree = null;
  }

  /**.
   * Tests that node has correct division of data on dimension
   * @param n node to test
   * @return whether satisfies kd tree condition
   */
  private boolean testNode(Node<Star> n) {
    int i = n.getSortDim();
    double val = n.getCoordinate().getDim(i);
    Node<Star> l = n.getLeftNode();
    Node<Star> r = n.getRightNode();
    if (l == null && r == null) {
      return true;
    }
    return testHelper(l, i, val, false) && testHelper(r, i, val, true);
  }

  /**.
   * Helper to recurssively test that node satisfies
   * @param n - Node to test
   * @param dim - dim to divide on
   * @param val - root value
   * @param bigger - whether the values should be bigger than root value
   * @return
   */
  private boolean testHelper(Node<Star> n, int dim, double val, boolean bigger) {

    if (n == null) {
      return true;
    }
    double num = n.getCoordinate().getDim(dim);
    //System.out.println(num + "     " + bigger);
    if (num == val || ((num > val) == bigger)) {
      return testHelper(n.getLeftNode(), dim, val, bigger)
          && testHelper(n.getRightNode(), dim, val, bigger);
    } else {
      return false;
    }
  }

  /**.
   * Test testNode on all nodes in kd tree
   * @param n - root node to test
   * @return whether the node satisfies kdtree condition
   */
  private boolean recTest(Node<Star> n) {
    if (n == null) {
      return true;
    }
    return testNode(n) && recTest(n.getLeftNode()) && recTest(n.getRightNode());
  }

  /**.
   * Tests kd tree for balance
   * @param n node to test
   * @return whether it is balanced as boolean
   */
  private boolean testBal(Node<Star> n) {
    if (n == null) {
      return true;
    }
    int l = count(n.getLeftNode());
    int r = count(n.getRightNode());
    double total = Math.log(l + r + 1) / Math.log(2);
    return Math.abs(l - r) <= total;
  }

  /**.
   * Tests that no stars were dropped in creating KD tree
   * @param n - node to test
   * @param count - desired count
   * @return boolean saying whether count is correct
   */
  private boolean testCount(Node<Star> n, int count) {
    if (n == null) {
      return count == 0;
    }
    int l = count(n.getLeftNode());
    int r = count(n.getRightNode());
    return (l + r + 1) == count;
  }

  /**.
   * Count on each sub node
   * @param n - node to test
   * @return number of nodes below including n
   */
  private int count(Node<Star> n) {
    if (n == null) {
      return 0;
    }
    return 1 + count(n.getLeftNode()) + count(n.getRightNode());
  }

  /**.
   * Tests balance on all nodes
   * @param n - node to test
   * @return boolean for does it pass
   */
  private boolean recBalTest(Node<Star> n) {
    if (n == null) {
      return true;
    }
    return testBal(n) && recBalTest(n.getLeftNode()) && recBalTest(n.getRightNode());
  }

  /**.
   * Test for ten star
   */
  @Test
  public void testReadStars() {
    setUp();
    assertTrue(testNode(kdtree));
    assertTrue(recTest(kdtree));
    assertTrue(testBal(kdtree));
    assertTrue(recBalTest(kdtree));
    assertTrue(testCount(kdtree, 10));
    tearDown();
  }

  /**.
   * Three star test
   */
  @Test
  public void testReadStars3() {
    setUp3();
    assertTrue(testNode(kdtree));
    assertTrue(recTest(kdtree));
    assertTrue(testBal(kdtree));
    assertTrue(recBalTest(kdtree));
    assertTrue(testCount(kdtree, 3));
    tearDown();
  }

  /**.
   * one star test
   */
  @Test
  public void testReadStars4() {
    setUp4();
    assertTrue(testNode(kdtree));
    assertTrue(recTest(kdtree));
    assertTrue(testBal(kdtree));
    assertTrue(recBalTest(kdtree));
    assertTrue(testCount(kdtree, 1));
    tearDown();
  }


}
