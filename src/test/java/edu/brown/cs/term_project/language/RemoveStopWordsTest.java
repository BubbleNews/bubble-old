package edu.brown.cs.term_project.language;

import edu.brown.cs.term_project.language.RemoveStopWords;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RemoveStopWordsTest {
    private String word1;
    private String word2;
    private String word3;
    private String word4;

    @Before
    public void setUp() {
        word1 = "";
        word2 = "xx";
        word3 = "funnymonkey";
        word4 = "after";
    }

    @After
    public void tearDown() {
        word1 = null;
        word2 = null;
        word3 = null;
        word4 = null;
    }

    @Test
    public void isStopWord() {
        setUp();
        assertTrue(RemoveStopWords.isStopWord(word1));
        assertTrue(RemoveStopWords.isStopWord(word2));
        assertFalse(RemoveStopWords.isStopWord(word3));
        assertTrue(RemoveStopWords.isStopWord(word4));
        tearDown();
    }
}
