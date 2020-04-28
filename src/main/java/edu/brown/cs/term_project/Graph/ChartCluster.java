package edu.brown.cs.term_project.Graph;

/**
 * This class is only used to pass cluster information from the database to the front end.
 */
public final class ChartCluster {
    private final int clusterId;
    private final String headline;
    private final int size;

    public ChartCluster(int clusterId, String headline, int size) {
        this.clusterId = clusterId;
        this.headline = headline;
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
