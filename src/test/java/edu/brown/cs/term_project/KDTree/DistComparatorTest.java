package edu.brown.cs.term_project.KDTree;

import edu.brown.cs.term_project.Stars.Star;

import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**.
 * Class to test DistComparator
 */
public class DistComparatorTest {
  private Star star1;
  private Star star2;
  private Star star3;
  private Star star4;
  private DistComparator comp1;

  /**.
   * Set up method for DistComparatorTest
   */
  @Before
  public void setUp() {
    double[] target = new double[]{1, 5, -4.3};
    double[] data1 = new double[]{3.3, 5, -5};
    double[] data2 = new double[]{3.3, 5, -5};
    double[] data3 = new double[]{17, -3, -13};
    double[] data4 = new double[]{-1, 57.98, -13};
    star1 = new Star(data1, 1, "star1");
    star2 = new Star(data2, 1, "star2");
    star3 = new Star(data3, 1, "star3");
    star4 = new Star(data4, 1, "star4");
    comp1 = new DistComparator(target);
  }

  /**.
   * tear down method for DistComparatorTest
   */
  @After
  public void tearDown() {
    star1 = null;
    star2 = null;
    star3 = null;
    star4 = null;
    comp1 = null;
  }

  /**.
   * Tests Dist Comparator on many different star combos
   */
  @Test
  public void testReadStars() {
    setUp();
    assertTrue(comp1.compare(star1, star2) == 0);
    assertTrue(comp1.compare(star1, star3) > 0);
    assertTrue(comp1.compare(star1, star4) > 0);
    assertTrue(comp1.compare(star2, star3) > 0);
    assertTrue(comp1.compare(star2, star4) > 0);
    assertTrue(comp1.compare(star3, star4) > 0);
    //reverse
    assertTrue(comp1.compare(star2, star1) == 0);
    assertTrue(comp1.compare(star3, star1) < 0);
    assertTrue(comp1.compare(star4, star1) < 0);
    assertTrue(comp1.compare(star3, star2) < 0);
    assertTrue(comp1.compare(star4, star2) < 0);
    assertTrue(comp1.compare(star4, star3) < 0);
    tearDown();
  }
}
