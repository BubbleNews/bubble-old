package edu.brown.cs.term_project.similarity;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TextCorpusTest {

  private TextCorpus<TestIWord, TestIText> c;

  @Before
  public void setUp() {
    Map<TestIWord, Double> vocab = new HashMap<>();
    vocab.put(new TestIWord("one"), 1.0);
    vocab.put(new TestIWord("two"), 2.0);
    vocab.put(new TestIWord("three"), 3.0);
    vocab.put(new TestIWord("four"), 4.0);
    vocab.put(new TestIWord("five"), 5.0);
    vocab.put(new TestIWord("six"), 6.0);
    vocab.put(new TestIWord("seven"), 7.0);
    vocab.put(new TestIWord("eight"), 8.0);
    this.c = new TextCorpus<>(vocab, 8,  0);
  }

  @Test
  public void getSimilarityTest() {
    Map<IWord, Double> srcMap = new HashMap<>();
    srcMap.put(new TestIWord("four"), 2.0);
    srcMap.put(new TestIWord("five"), 5.0);
    srcMap.put(new TestIWord("seven"), 3.0);
    srcMap.put(new TestIWord("eight"), 1.0);
    srcMap.put(new TestIWord("six"), 2.0);
    TestIText src = new TestIText(srcMap, null);
    Map<IWord, Double> dstMap = new HashMap<>();
    dstMap.put(new TestIWord("four"), 4.0);
    dstMap.put(new TestIWord("five"), 6.0);
    dstMap.put(new TestIWord("seven"), 1.0);
    dstMap.put(new TestIWord("eight"), 2.0);
    dstMap.put(new TestIWord("three"), 7.0);
    dstMap.put(new TestIWord("two"), 10.0);
    TestIText dst = new TestIText(dstMap, null);
    assertEquals(c.getSimilarity(src, dst), 0.07104927, 0.00001);
  }

  @Test
  public void getImportanceMapTest() {
    Map<IWord, Double> srcMap = new HashMap<>();
    TestIWord src4 = new TestIWord("four");
    TestIWord src5 = new TestIWord("five");
    TestIWord src7 = new TestIWord("seven");
    TestIWord src8 = new TestIWord("eight");
    TestIWord src6 = new TestIWord("six");
    srcMap.put(src4, 2.0);
    srcMap.put(src5, 5.0);
    srcMap.put(src7, 3.0);
    srcMap.put(src8, 1.0);
    srcMap.put(src6, 2.0);
    Map<IWord, Double> srcImportanceMap = c.getImportanceMap(srcMap);
    assertEquals(srcImportanceMap.get(src4), .200158307, 0.00001);
    assertEquals(srcImportanceMap.get(src5), .463123038, 0.00001);
    assertEquals(srcImportanceMap.get(src7), .2441519769, 0.00001);
    assertEquals(srcImportanceMap.get(src8), .0769230769, 0.00001);
    assertEquals(srcImportanceMap.get(src6), .17306749, 0.00001);
    // Second Test
    Map<IWord, Double> dstMap = new HashMap<>();
    TestIWord dst4 = new TestIWord("four");
    TestIWord dst5 = new TestIWord("five");
    TestIWord dst7 = new TestIWord("seven");
    TestIWord dst8 = new TestIWord("eight");
    TestIWord dst3 = new TestIWord("three");
    TestIWord dst2 = new TestIWord("two");
    dstMap.put(dst4, 4.0);
    dstMap.put(dst5, 6.0);
    dstMap.put(dst7, 1.0);
    dstMap.put(dst8, 2.0);
    dstMap.put(dst3, 7.0);
    dstMap.put(dst2, 10.0);
    Map<IWord, Double> dstImportanceMap = c.getImportanceMap(dstMap);
    assertEquals(dstImportanceMap.get(dst4), .173470533, 0.00001);
    assertEquals(dstImportanceMap.get(dst5), .24082398, 0.00001);
    assertEquals(dstImportanceMap.get(dst7), .03526639666, 0.00001);
    assertEquals(dstImportanceMap.get(dst8), .0666666666, 0.00001);
    assertEquals(dstImportanceMap.get(dst3), .332726037, 0.00001);
    assertEquals(dstImportanceMap.get(dst2), .53401999, 0.00001);
  }

  @Test
  public void getSimilarityHash() {
    Map<IWord, Double> srcMap = new HashMap<>();
    srcMap.put(new TestIWord("four"), 2.0);
    srcMap.put(new TestIWord("five"), 5.0);
    srcMap.put(new TestIWord("seven"), 3.0);
    srcMap.put(new TestIWord("eight"), 1.0);
    srcMap.put(new TestIWord("six"), 2.0);
    TestIText src = new TestIText(srcMap, c.getImportanceMap(srcMap));
    Map<IWord, Double> dstMap = new HashMap<>();
    dstMap.put(new TestIWord("four"), 4.0);
    dstMap.put(new TestIWord("five"), 6.0);
    dstMap.put(new TestIWord("seven"), 1.0);
    dstMap.put(new TestIWord("eight"), 2.0);
    dstMap.put(new TestIWord("three"), 7.0);
    dstMap.put(new TestIWord("two"), 10.0);
    TestIText dst = new TestIText(dstMap, c.getImportanceMap(dstMap));
    assertEquals(c.getSimilarity(src, dst), 0.07104927, 0.00001);
    Map<IWord, Double> simHash = c.getSimilarityHash(src, dst);
    assertEquals(simHash.get(new TestIWord("four")), .015419230647, 0.00001);
    assertEquals(simHash.get(new TestIWord("five")), .049528991833, 0.00001);
    assertEquals(simHash.get(new TestIWord("seven")), .00382379788, 0.00001);
    assertEquals(simHash.get(new TestIWord("eight")), .002277344652, 0.00001);
    assertEquals(simHash.size(), 4);
  }

}
