package edu.brown.cs.cchrabas_jgraves1.KDTree;

import edu.brown.cs.cchrabas_jgraves1.KDTree.DimComparator;
import edu.brown.cs.cchrabas_jgraves1.Stars.Star;

import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**.
 * DimComparatorTest Class
 */
public class DimComparatorTest {
  private Star star1;
  private Star star2;
  private DimComparator comp1;
  private DimComparator comp2;
  private DimComparator comp3;

  /**.
   * Setup Method fro DimComparator Test
   */
  @Before
  public void setUp() {
    double[] data1 = new double[]{1, 5, -4.3};
    double[] data2 = new double[]{14.5, 5, -5};
    star1 = new Star(data1, 1, "star1");
    star2 = new Star(data2, 1, "star1");
    comp1 = new DimComparator(1);
    comp2 = new DimComparator(5);
    comp3 = new DimComparator(0);
  }

  /**.
   * TearDown method for DimComparator Test
   */
  @After
  public void tearDown() {
    star1 = null;
    star2 = null;
    comp1 = null;
    comp2 = null;
    comp3 = null;
  }

  /**.
   * Tests DimComparator on different dimensions
   */
  @Test
  public void testReadStars() {
    setUp();
    assertTrue(comp1.compare(star1, star2) == 0);
    assertTrue(comp2.compare(star1, star2) > 0);
    assertTrue(comp3.compare(star1, star2) < 0);
    tearDown();
  }
}
