package edu.brown.cs.cchrabas_jgraves1.Stars;

import edu.brown.cs.cchrabas_jgraves1.Stars.Star;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**.
 * Class to Test Stars
 */
public class StarTest {
  private Star star;

  /**.
   * Set up method for testing Stars
   */
  @Before
  public void setUp() {
    double[] data = new double[]{1, 5, -4.3, 9.90980};
    star = new Star(data, 1, "star1");
  }

  /**.
   * Tear Down method for testing Stars
   */
  @After
  public void tearDown() {
    star = null;
  }

  /**.
   * Test Stars distance function
   */
  @Test
  public void testStars() {
    setUp();
    double[] data = new double[]{6.9998, -7, -2, -3.89};
    assertEquals(Math.round(star.getDistance(data) * 100), 1938);
    tearDown();
  }

  /**.
   * Test Stars Distance function on idential locations
   */
  @Test
  public void testStars2() {
    setUp();
    double[] data = new double[]{1, 5, -4.3, 9.90980};
    assertEquals(Math.round(star.getDistance(data) * 100), 0);
    tearDown();
  }
}
