package edu.brown.cs.term_project.api.response;

import edu.brown.cs.term_project.bubble.Article;

import java.util.List;

/**
 * This class is only used to pass cluster information from the database to the front end.
 */
public class ChartCluster {
    private final int clusterId;
    private final String headline;
    private final int size;
    private final double meanRadius;
    private final List<Article> articles;

    public ChartCluster(int clusterId, String headline, int size,
                        double meanRadius, List<Article> articles) {
        this.clusterId = clusterId;
        this.headline = headline;
        this.size = size;
        this.meanRadius = meanRadius;
        this.articles = articles;
    }

    public int getClusterId() { return clusterId; }

    public int getSize() {
        return size;
    }
}
