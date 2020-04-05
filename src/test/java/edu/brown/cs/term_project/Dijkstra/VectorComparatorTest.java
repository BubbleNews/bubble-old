package edu.brown.cs.term_project.Dijkstra;

import edu.brown.cs.term_project.TIMDB.Actor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**.
   * DimComparatorTest Class
   */
  public class VectorComparatorTest {
    private GenericVertex v1;
    private GenericVertex v2;
    private GenericVertex v3;
    private Actor a1;
    private Actor a2;
    private Actor a3;
    private VertexComparator comp;

    /**.
     * Setup Method for VectorComparator Test
     */
    @Before
    public void setUp() {
      a1 = new Actor("a1", "a1");
      a1.setWeight(35);
      a2 = new Actor("a2", "a2");
      a2.setWeight(30.2);
      a3 = new Actor("a3", "a3");
      a3.setWeight(35.0);
      v1 = new GenericVertex("v1");
      v1.setWeight(40.7);
      v2 = new GenericVertex("v2");
      v2.setWeight(40.8);
      v3 = new GenericVertex("v3");
      v3.setWeight(40.9);
      comp = new VertexComparator();
    }

  /**
   * VectorComparator TearDown method
   */
  public void tearDown() {
      a1 = null;
      a2 = null;
      a3 = null;
      v1 = null;
      v2 = null;
      v3 = null;
      comp = null;
    }

  /**.
   * Tests VectorComparator on different vectors
   */
  @Test
  public void testReadStars() {
    setUp();
    assertTrue(comp.compare(a1, a3) == 0);
    assertTrue(comp.compare(a1, a2) > 0);
    assertTrue(comp.compare(a2, a3) < 0);
    assertTrue(comp.compare(v1, v2) < 0);
    assertTrue(comp.compare(v3, v2) > 0);
    assertTrue(comp.compare(v3, v1) > 0);
    tearDown();
  }
}
