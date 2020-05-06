package edu.brown.cs.term_project.database;

import edu.brown.cs.term_project.api.response.ChartCluster;
import edu.brown.cs.term_project.bubble.Article;
import edu.brown.cs.term_project.bubble.ArticleVertex;
import edu.brown.cs.term_project.bubble.ArticleWord;
import edu.brown.cs.term_project.bubble.Entity;
import edu.brown.cs.term_project.similarity.IWord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NewsDataReadTest {
    private NewsDataRead db;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        db = new NewsData("data/news_data_read_test.sqlite3").getDataRead();
    }

    @After
    public void tearDown() {
        db = null;
    }

    @Test
    public void getSourcesTest() throws SQLException, ClassNotFoundException {
        setUp();
        Set<String> uniqueSources = db.getSources();
        Set<String> correctUniqueSources = Set.of("CNBC", "CBS News", "The Washington Post");
        assertEquals(uniqueSources, correctUniqueSources);
        tearDown();
    }

    @Test
    public void getArticlesFromClusterTest() throws SQLException, ClassNotFoundException {
        setUp();
        List<Article> articles = db.getArticlesFromCluster(1);
        Set<String> correctTitles = Set.of("Supreme Court streams oral arguments in USPTO v Booking.com",
                "Supreme Court teleconference hearings mark a modest but historic step");
        for (Article article : articles) {
            assertTrue(correctTitles.contains(article.getTitle()));
        }
        tearDown();
    }

    @Test
    public void getArticleVerticesFromArticleIdsTest()
            throws SQLException, ClassNotFoundException {
        setUp();
        Set<ArticleVertex> articleVertices =
                db.getArticleVerticesFromArticleIds(new String[] {"19","25","29"});
        Set<String> correctTitles =   Set.of("Supreme Court streams oral arguments in USPTO v Booking.com",
                "Supreme Court teleconference hearings mark a modest but historic step",
                "New York attorney general looking into sexual harassment concerns at NBC News");
        for (ArticleVertex vertex : articleVertices) {
            assertTrue(correctTitles.contains(vertex.getArticle().getTitle()));
        }
        tearDown();
    }

    @Test
    public void getVocabFreqTest() throws SQLException, ClassNotFoundException {
        setUp();
        Map<ArticleWord, Double> vocab = db.getVocabFreq();
        assertEquals(vocab.size(), 4735);
        tearDown();
    }

    @Test
    public void getEntityFreqTest() throws SQLException, ClassNotFoundException {
        setUp();
        Map<Entity, Double> entities = db.getEntityFreq();
        assertEquals(entities.size(), 1363);
        tearDown();
    }

    @Test
    public void getArticleEntityFreqTest() throws SQLException, ClassNotFoundException {
        setUp();
        Map<IWord, Double> entities1 = db.getArticleEntityFreq(19);
        assertEquals(entities1.size(), 28);
        Map<IWord, Double> entities2 = db.getArticleEntityFreq(25);
        assertEquals(entities2.size(), 51);
        Map<IWord, Double> entities3 = db.getArticleEntityFreq(29);
        assertEquals(entities3.size(), 19);
        tearDown();
    }

    @Test
    public void getMaxVocabCountTest() throws SQLException, ClassNotFoundException {
        setUp();
        int maxCount = db.getMaxVocabCount();
        assertEquals(maxCount, 31);
        tearDown();
    }

    @Test
    public void getClustersTest() throws SQLException, ClassNotFoundException {
        setUp();
        String date = "2020-05-06";
        List<ChartCluster> clusters = db.getClusters(date, 13, 0);
        assertEquals(clusters.size(), 1);
        assertEquals(clusters.get(0).getSize(), 2);
        assertEquals(clusters.get(0).getClusterId(), 1);
        tearDown();
    }

    @Test
    public void getNewestClustersTest() throws SQLException, ClassNotFoundException {
        setUp();
        List<ChartCluster> clusters = db.getNewestClusters();
        assertEquals(clusters.size(), 1);
        assertEquals(clusters.get(0).getSize(), 2);
        assertEquals(clusters.get(0).getClusterId(), 1);
        tearDown();
    }
}
