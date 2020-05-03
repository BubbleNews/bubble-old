package edu.brown.cs.term_project.Graph;

import edu.brown.cs.term_project.Bubble.Article;

import java.util.List;

/**
 * This class is only used to pass cluster information from the database to the front end.
 */
public class ChartCluster {
    private int clusterId;
    private String headline;
    private int size;
    private List<Article> articles;

    public ChartCluster(int clusterId, String headline, int size, List<Article> articles) {
        this.clusterId = clusterId;
        this.headline = headline;
        this.size = size;
        this.articles = articles;
    }

    public int getSize() {
        return size;
    }
}
