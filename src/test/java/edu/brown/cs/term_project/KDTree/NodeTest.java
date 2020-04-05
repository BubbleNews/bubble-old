package edu.brown.cs.term_project.KDTree;

import edu.brown.cs.term_project.REPL.StartREPL;
import edu.brown.cs.term_project.Stars.ReadStars;
import edu.brown.cs.term_project.Stars.Star;
import edu.brown.cs.term_project.Stars.StarMain;

import java.io.PrintWriter;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**.
 * Class to test Node
 */
public class NodeTest {
  private Node<Star> kdtree;


  /**.
   * Set up method with ten-star data
   */
  @Before
  public void setUp1() {
    StartREPL.getStartCM().add("stars", new ReadStars());
    StartREPL.getStartCM().action("stars data/stars/ten-star.csv",
        new PrintWriter(System.out)); // Calls MakeKD, updating kdtree;
    kdtree = StarMain.getKD();
  }

  /**.
   * Set up method with three-star data
   */
  @Before
  public void setUp2() {
    StartREPL.getStartCM().add("stars", new ReadStars());
    StartREPL.getStartCM().action("stars data/stars/three-star.csv",
        new PrintWriter(System.out)); // Calls MakeKD, updating kdtree;
    kdtree = StarMain.getKD();
  }

  /**.
   * Method translating List of Stars to array of ID's
   * @param l - list to translate
   * @return array of star id's
   */
  public int[] toArray(List<Star> l) {
    int[] array = new int[l.size()];
    for (int i = 0; i < l.size(); i++) {
      array[i] = l.get(i).getId();
    }
    return array;
  }

  /**.
   * Tear Down method for NodeTest
   */
  @After
  public void tearDown() {
    kdtree = null;
  }

  /**.
   * Method to see if two integer arrays are equal
   * @param a1 first array
   * @param a2 second array
   * @return boolean representing whether they are equal
   */
  public boolean areEqual(int[] a1, int[] a2) {
    if (a1.length == a2.length) {
      for (int i = 0; i < a1.length; i++) {
        if (a1[i] != a2[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**.
   * First method testing Node's radius and neighbor return functions
   * Uses ten star data
   */
  @Test
  public void testNode1() {
    setUp1();
    double[] target = new double[]{0, 0, 0};
    double[] target2 = new double[]{-10, 3, 4.7};
    int[] test1 = new int[]{0, 70667, 71454, 71457, 87666};
    int[] test2 = new int[]{0, 70667, 71454, 71457, 87666, 118721};
    int[] test3 = new int[]{0, 70667, 71454, 71457, 87666, 118721, 3759, 2, 1, 3};
    int[] test4 = new int[]{118721, 0, 70667, 71457, 71454};
    int[] test5 = new int[]{};
    assertTrue(areEqual(toArray(kdtree.pointNeighbors(5, target)), test1));
    assertTrue(areEqual(toArray(kdtree.radiusNeighbors(5, target)), test2));
    assertTrue(areEqual(toArray(kdtree.pointNeighbors(10, target)), test3));
    assertTrue(areEqual(toArray(kdtree.pointNeighbors(5, target2)), test4));
    assertTrue(areEqual(toArray(kdtree.radiusNeighbors(5, target2)), test5));
    assertTrue(areEqual(toArray(kdtree.pointNeighbors(0, target)), test5));
    assertTrue(areEqual(toArray(kdtree.radiusNeighbors(0, target)), test5));
    tearDown();
  }

  /**.
   * Second method testing Node's radius and neighbor return functions
   * Uses three star data
   */
  @Test
  public void testNode2() {
    setUp2();
    double[] target = new double[]{0, 0, 0};
    int[] test1 = new int[]{1, 2, 3};
    int[] test2 = new int[]{};
    assertTrue(areEqual(toArray(kdtree.pointNeighbors(5, target)), test1));
    assertTrue(areEqual(toArray(kdtree.radiusNeighbors(.5, target)), test2));
    assertTrue(areEqual(toArray(kdtree.radiusNeighbors(45, target)), test1));
    tearDown();
  }



}
