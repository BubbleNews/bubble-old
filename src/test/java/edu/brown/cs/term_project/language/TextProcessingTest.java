package edu.brown.cs.term_project.language;

import edu.brown.cs.term_project.bubble.Entity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TextProcessingTest {
    String text1;
    String text2;
    Set<String> ignoredEntityTypes;

    @Before
    public void setUp() {
        text1 = "a the myself 200 first Trump Jo";
        text2 = "Florida Program for Shark Research Director George H. Burgess discussed the lack of" +
                " knowledge around shark sleep with the Van Winkle's blog and says some sharks may rest " +
                "during \"yo-yo swimming,\" when they actively swim to the surface but rest as they descend. " +
                "Whether they actually rest or dream, and how resting varies among species, we don't really know";
        ignoredEntityTypes = Set.of("NUMBER", "CRIMINAL_CHARGE", "DATE",
                "MONEY", "DURATION", "TIME", "ORDINAL", "O");
    }

    @After
    public void tearDown() {
        text1 = null;
        ignoredEntityTypes = null;
    }

    @Test
    // we can't test the exact output of this function because it uses a neural net, but we can check that
    // it filters out stop words and entities that we want to ignore.
    public void testGetEntitiesIgnoresStopWords() {
        Map<Entity, Integer> entities1 = TextProcessing.getEntityFrequencies(text1);
        // should have only found Trump
        assertEquals(entities1.size(), 1);
    }

    @Test
    public void testGetEntitiesIgnoresCertainEntityTypes() {
        Map<Entity, Integer> entities2 = TextProcessing.getEntityFrequencies(text2);

        // should ignore any entities of the type we want to ignore
        entities2.forEach((k, v) -> assertFalse(ignoredEntityTypes.contains(k.getClassType())));
    }
}