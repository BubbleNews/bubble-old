package edu.brown.cs.term_project.Bubble;

import edu.brown.cs.term_project.Graph.Graph;

import java.util.*;

/**
 * Class extending graph which clusters articles
 */
public class ArticleGraph extends Graph<Cluster, Article, Similarity> {
  private Set<Article> nodes;
  private List<Similarity> edges;
  private Set<Article> adjNodes; // this set of nodes is adjusted during clustering
  private List<Similarity> adjEdges; // this list of edges is adjusted during clustering
  private double threshold;
  private double radiusThreshold;
  private Set<Cluster> clusters;
  private Integer count = 0; //used to label clusters with unique id's

  /**
   * Constructor which creates random graph and sets thresholds
   */
  public ArticleGraph() {
    super();
    this.nodes = new HashSet<Article>();
    this.edges = new ArrayList<Similarity>();
    this.clusters = new HashSet<>();
    createGraph(1000); // create random graph
    System.out.println("nodes: " + nodes.size());
    System.out.println("edges: " + edges.size());
    setThreshold(); // set threshold based on node count
    System.out.println("thresh: " + threshold);
    setRadiusThreshold(nodes); // sets threshold for mean radius of cluster
    System.out.println("Radius Now: " + radiusThreshold);
  }

  /**
   * constructor which creates a graph from articles and their edges
   * @param nodes - Set of Articles for graph
   * @param edges - list of edges of graph
   */
  public ArticleGraph(Set<Article> nodes, List<Similarity> edges) {
    super();
    this.nodes = nodes;
    this.edges = edges;
    this.clusters = new HashSet<>();
    System.out.println("nodes: " + nodes.size());
    System.out.println("edges: " + edges.size());
    setThreshold(); // set threshold based on node count
    System.out.println("thresh: " + threshold);
    setRadiusThreshold(nodes); // sets threshold for mean radius of cluster
    System.out.println("Radius Now: " + radiusThreshold);
  }

  /**
   * First method to create clusters - It sorts the edges by increasing weight and adds them in sequentially.
   * As it adds the edges in it determines whether the two nodes should be in the same cluster, and if the nodes
   * are already clustered, whether the two clusters should be combined.
   */
  @Override
  public void createClusters() {
    trimData(); // trims graph by removing edges based on threshold, and nodes that no longer have any edges
    System.out.println("nodes_adj: " + adjNodes.size());
    System.out.println("edges_adj: " + adjEdges.size());
    setRadiusThreshold(adjNodes); // sets new radius threshold based on the updated graph
    System.out.println("radius_adj: " + radiusThreshold);
    Map<Integer, Cluster> tempClusters = new HashMap<>(); // keeps track of what cluster each node is in
    for (int i = 0; i < adjEdges.size(); i++) {
      if (i%100 == 0) {
        System.out.println(i);
      }
      Similarity curr = adjEdges.get(i);
      Article src = curr.getSource();
      Article dst = curr.getDest();
      if (tempClusters.containsKey(src.getId()) && tempClusters.containsKey(dst.getId())) {
        // sees if both nodes of current edge are clustered, if so decides whether to combine clusters
        combine(tempClusters.get(src.getId()), tempClusters.get(dst.getId()), tempClusters);
      } else if (tempClusters.containsKey(src.getId())) {
        // if one node is cluster, determine whether to add other node
        add(tempClusters.get(src.getId()), dst, tempClusters);
      } else if (tempClusters.containsKey(dst.getId())) {
        add(tempClusters.get(dst.getId()), src, tempClusters);
      } else {
        // if none are clustered, create a cluster with both nodes
        Set<Article> add_nodes = new HashSet<Article>();
        add_nodes.add(src);
        add_nodes.add(dst);
        Cluster new_cluster = new Cluster(count, src, add_nodes);
        tempClusters.put(src.getId(), new_cluster);
        tempClusters.put(dst.getId(), new_cluster);
        count++;
      }
    } // once every edge within threshold has been considered, add cluster in tempClusters to clusters
    tempClusters.forEach((k, v) -> {
      if (!clusters.contains(v)) {
        v.adjustHead(); // need to optimize the head node of the cluster, to make headline most fitting
        clusters.add(v);
        System.out.println("Cluster: " + v.getHeadNode().getId() + " size: " + v.getSize());
        String toPrint = "";
        for(Article n: v.getNodes()) { //prints out nodes in cluster
          toPrint += n.getId();
          toPrint += " ";
        }
        System.out.println(toPrint);
      }
    });
  }

  /**
   * Second version of clustering - This one uses some of the principles of K-Means clustering to find the optimal
   * number of clusters. Up to a max integer n, it tries to find n nodes that are all far apart, so that none of them
   * should probably be in the same cluster. It adds each of the remaining nodes to a cluster with the apart node
   * they are closest to. For each number of nodes it calculates the mean radius, and tries to find an "elbow point"
   * by maximizing the change in the slope between intervals of n. Then the clustering based on the optimal n is used
   * with a bit more elaborate method to get more accurate results.
   */
  public void createClusters2() {
    trimData2(); // trims graph by removing nodes that have no edges in threshold, and edges that have no nodes left
    System.out.println("nodes_adj: " + adjNodes.size());
    System.out.println("edges_adj: " + adjEdges.size());
    setRadiusThreshold(adjNodes); //sets radius threshold based on trimmed data
    System.out.println("radius_adj: " + radiusThreshold);
    int maxClusters = Math.min((int)Math.ceil(adjNodes.size()/2) + 1, 50); //experiment - max number of clusters
    List<Article> clusterNodes = findClusterNodes(maxClusters);
    double maxDiff = Integer.MIN_VALUE; // value to maximize to find optimal number of clusters
    int numClusters = 0;
    double prevRadius2 = meanRadiusSet(adjNodes)*adjNodes.size();
    double prevRadius1 = meanRadiusSet(adjNodes)*adjNodes.size();;
    for (Article d: clusterNodes) {
      System.out.print(d.getId() + " ");
    }
    System.out.println();
    System.out.println("Count: " + 1 + " - " + prevRadius1);
    for (int i=2; i <= maxClusters; i++) {
      List<Cluster> tempClusters = approxFindClusters(clusterNodes, i); // approximate clustering with i clusters
      double newRadius = totalRadius(tempClusters);
      double diff = (prevRadius2 - prevRadius1)/Math.max(prevRadius1 - newRadius, 0.5); //experiment - diff formula
      System.out.println("Diff: " + (i - 1) + " - " + diff);
      if (diff > maxDiff) { // if diff is larger than maxDiff, update
        maxDiff = diff;
        numClusters = i - 1;
      }
      prevRadius2 = prevRadius1;
      prevRadius1 = newRadius;
    }
    // instead of picking just best cluster, could make a queue of top 5 or so, go from there
    List<Cluster> finalClusters = findClusters(clusterNodes, numClusters); // finds optimal clustering with numClusters
    for (Cluster c: finalClusters) {
      c.adjustHead(); // optimzes cluster head
      clusters.add(c); // adds cluster to graphs clusters
      System.out.println("Cluster: " + c.getHeadNode().getId() + " size: " + c.getSize());
      String toPrint = "";
      for(Article n: c.getNodes()) { // prints clustering
        toPrint += n.getId();
        toPrint += " ";
      }
      System.out.println(toPrint);
    }
  }

  /**
   * Method to trim data for CreateClusters method. Removes all edges beyond threshold, and any nodes without any
   * edges remaining.
   */
  public void trimData() {
    adjEdges = new ArrayList<>(edges); // creates alternative edges to modify
    adjNodes = new HashSet<>(nodes); // creates alternative nodes to modify
    adjEdges.sort(new SimilarityComparator()); // sorts edges by weight
    int size = adjEdges.size();
    adjEdges = new ArrayList<>(adjEdges.subList(0, (int)Math.floor(size * threshold))); // removes edges above threshold
    Set<Article> newNodes = new HashSet<>();
    for (Similarity e: adjEdges) { // removes nodes with no edges left
      newNodes.add(e.getSource());
      newNodes.add(e.getDest());
    }
    adjNodes = newNodes;
  }

  /**
   * Method to trim data for CreateClusters2 method. Removes all nodes that have no edges below threshold and all
   * edges with either their source or destination deleted.
   */
  public void trimData2() {
    adjEdges = new ArrayList<>(edges); // creates alternative edges to modify
    adjNodes = new HashSet<>(nodes); // creates alternative nodes to modify
    adjEdges.sort(new SimilarityComparator());
    int size = adjEdges.size();
    // temporarily removes edges above threshold
    List<Similarity> edges2 = new ArrayList<>(adjEdges.subList(0, (int)Math.floor(size * threshold)));
    Set<Article> newNodes = new HashSet<>();
    for (Similarity e: edges2) { // removes nodes that have no edges left after temporary removal
      newNodes.add(e.getSource());
      newNodes.add(e.getDest());
    }
    adjNodes = newNodes;
    List<Similarity> newEdges = new ArrayList<>();
    for (Similarity e: adjEdges) { // removes edges that have one of their nodes removed
      if (adjNodes.contains(e.getSource()) && adjNodes.contains(e.getDest())) {
        newEdges.add(e);
      }
    }
    adjEdges = newEdges;
  }

  /**
   * Method to find n nodes that should not be in the same cluster
   * @param n - number of nodes to find
   * @return - List of Article nodes that shouldn't be in the same cluster no matter what
   */
  public List<Article> findClusterNodes(Integer n) {
    int count = 1;
    List<Article> clusterNodes = new ArrayList<>();
    while (count < n) { //
      if (clusterNodes.size() == 0) { //if no articles found yet, add the two with the farthest edge
        adjEdges.sort(new SimilarityComparator());
        Collections.reverse(adjEdges);
        Similarity edge = adjEdges.get(0);
        clusterNodes.add(edge.getSource());
        clusterNodes.add(edge.getDest());
      } else { // if clusterNodes isn't empty, find node that has the largest minimum distance to any clusterNodes
        double max = Integer.MIN_VALUE;
        Article currMax = null;
        for (Article node1: adjNodes) {
          double tempMin = Integer.MAX_VALUE;
          for (Article node2: clusterNodes) {
            tempMin = Math.min(tempMin, node1.getDistance(node2));
          }
          if (tempMin > max) {
            max = tempMin;
            currMax = node1;
          }
        }
        clusterNodes.add(currMax);
      }
      count++;
    }
    return clusterNodes;
  }

  /**
   * Method to optimally divide node into n clusters
   * @param clusterNodes - the head nodes to start each cluster with
   * @param n - the number of clusters to make
   * @return
   */
  public List<Cluster> findClusters(List<Article> clusterNodes, Integer n) {
    List<Cluster> tempClusters = new ArrayList<>();
    Set<Article> tempNodes = new HashSet<>(adjNodes);
    for (int i=0; i < n; i++) { //create n clusters each with one of the clusterNodes in it, add to arraylist
      Article curr1 = clusterNodes.get(i);
      Set<Article> tempSet = new HashSet<Article>();
      tempSet.add(curr1);
      tempClusters.add(new Cluster(count, curr1, tempSet));
      count++;
      tempNodes.remove(curr1);
    }
    Map<Integer, double[]> nodeDistance = new HashMap<>();
    Map<Integer, Integer> minDistIndex = new HashMap<>();
    for (Article n1: tempNodes) { //initialize an array for each node with the distance to each of the clusterNodes
      double[] distances = new double[n];
      double minDist = Integer.MAX_VALUE;
      int minIndex = -1;
      for (int i = 0; i < n; i++) {
        double dist = tempClusters.get(i).meanRadiusNode(n1);
        if (dist < minDist) {
          minDist = dist;
          minIndex = i;
        }
        distances[i] = dist;
        nodeDistance.put(n1.getId(), distances);
      }
      minDistIndex.put(n1.getId(), minIndex);
    }
    while (tempNodes.size() > 0) { // until tempNodes is empty, find tempNode with smallest distance to a cluster
      // add that node to the respective cluster
      double min = Integer.MAX_VALUE;
      Article minNode = null;
      int minClusterIdx = -1;
      for (Article n1: tempNodes) { // find node closest to a cluster by iterating through minDistIndex map
        // this finds the index of the shortest distance in the nodeDistance map.
        int id = n1.getId();
        int clusterIdx = minDistIndex.get(id);
        double dist = nodeDistance.get(id)[clusterIdx];
        if (dist < min) {
          min = dist;
          minNode = n1;
          minClusterIdx = clusterIdx;
        }
      }
      tempClusters.get(minClusterIdx).addNode(minNode);
      tempNodes.remove(minNode);
      for (Article n1: tempNodes) { // update every array in nodeDistance map at the minClusterIdx with new distance
        int id = n1.getId();
        Cluster c = tempClusters.get(minClusterIdx);
        double tempDist = c.meanRadiusNode(n1)*Math.max(.8, 1.02-.02*c.getSize()); //experiment
        double[] distances = nodeDistance.get(id);
        distances[minClusterIdx] = tempDist;
        if (tempDist < distances[minDistIndex.get(id)]) { //if less than min distance, update the minDistIndex hashmap
          minDistIndex.replace(id, minClusterIdx);
        } else if (minDistIndex.get(id) == minClusterIdx) {
          // if old min was current cluster, but no longer is, find new min and update minDistIndex
          double minDist = Integer.MAX_VALUE;
          int minIndex = -1;
          for (int i = 0; i < n; i++) {
            double dist = tempClusters.get(i).meanRadiusNode(n1);
            if (dist < minDist) {
              minDist = dist;
              minIndex = i;
            }
          }
          minDistIndex.replace(id, minIndex);
        }
      }
    }
    return tempClusters;
  }

  /**
   * Method to find an approximate best clustering with n clusters and one of clusterNodes in each cluster
   * The method is a faster and less accurate version of findClusters, since it doesn't add nodes to clusters in
   * sequential order from smallest to largest distance
   * @param clusterNodes - nodes to start each cluster with
   * @param n - number of clusters
   * @return - List of approximate clustering with n clusters
   */
  public List<Cluster> approxFindClusters(List<Article> clusterNodes, Integer n) {
    List<Cluster> tempClusters = new ArrayList<>();
    List<Article> tempNodes = new ArrayList<>(adjNodes);
    for (int i=0; i < n; i++) { //create n clusters each with one of the clusterNodes in it, add to arraylist
      Article curr1 = clusterNodes.get(i);
      Set<Article> tempSet = new HashSet<Article>();
      tempSet.add(curr1);
      tempClusters.add(new Cluster(count, curr1, tempSet));
      count++;
      tempNodes.remove(curr1);
    }
    Map<Integer, Double> minDistance = new HashMap<>();
    for (Article n1: tempNodes) { //initialize an array for each node with the distance to each of the clusterNodes
      double minDist = Integer.MAX_VALUE;
      for (int i = 0; i < n; i++) {
        double dist = tempClusters.get(i).meanRadiusNode(n1);
        if (dist < minDist) {
          minDist = dist;
        }
      }
      minDistance.put(n1.getId(), minDist);
    }
    tempNodes.sort(new ArticleComparator(minDistance)); // sorts articles by distance to initial clusters
    // this is an approximation, since it doesn't take into consideration when the clusters add nodes
    for (Article n1: tempNodes) { // for node, finds closest cluster (including nodes already clustered from this loop)
      double min = Integer.MAX_VALUE;
      Cluster minCluster = null;
      for (Cluster c: tempClusters) {
        // gives slight advantage to clusters with more nodes since meanRadius will likely be larger than min
        double dist = c.meanRadiusNode(n1)*Math.max(.8, 1.02-.02*c.getSize()); //experiment
        if (dist < min) {
          min = dist;
          minCluster = c;
        }
      }
      minCluster.addNode(n1);
    }
    return tempClusters;
  }

  /**
   * Method to calculate the "total Radius" of a group of clusters. This is not actually the total radius, it is
   * the mean radius multiplied by number of nodes (true total radius would be mean radius times edge count).
   * It isn't true total Radius, since that would advantage more clusters
   * (smaller cluster sizes decrease intra-cluster edge counts).
   * @param currClusters - Collection of clusters to find total radius of
   * @return - double representing sum of average radius times node count of each cluster
   */
  public double totalRadius(Collection<Cluster> currClusters) {
    double sum = 0;
    for (Cluster c: currClusters) {
      sum += c.getAvgRadius()*c.getSize();
    }
    return sum;
  }

  /**
   * Finds mean radius (edge weight) of all connections of Articles in a Collection
   * @param nodes1 - collection of nodes to find mean radius of
   * @return - double representing the mean radius of nodes
   */
  public double meanRadiusSet(Collection<Article> nodes1) {
    double sum = 0;
    int count = 0;
    for (Article n1: nodes1) {
      for (Article n2: nodes1) {
        if (!n1.equals(n2)) {
          sum += n1.getDistance(n2);
          count++;
        }
      }
    }
    if (count == 0) {
      return 0;
    } else {
      return sum/count;
    }
  }

  /**
   * Checks if node should be added to cluster, if it should adds it, and updates hash map
   * @param c1 - cluster to see if node should be added to
   * @param node - node
   * @param clusters - map from node id to cluster, must be updated if node is added
   */
  public void add(Cluster c1, Article node, Map<Integer, Cluster> clusters) {
    double oldMean = c1.getAvgRadius();
    double newMean = c1.meanRadiusNode(node);
    double maxMult = Math.max(2 - (0.05 * c1.getSize()), 1.3); //experiment with this
    double diff2 = newMean/(0.5*radiusThreshold); //experiment
    // checks if mean radius of new cluster is less than threshold, and the either new mean isn't too much more than
    // old mean or if the new mean is small enough to compensate for increasing old mean significantly
    if (newMean < radiusThreshold && (newMean < maxMult*oldMean || diff2 < 1)) { //add to cluster
      c1.addNode(node);
      clusters.put(node.getId(), c1);
    }
  }

  /**
   * Checks if two clusters should be combined and are not equal, if they should combine them and update hashmap
   * @param c1 - first cluster to check for combination
   * @param c2 - second cluster to check for combination
   * @param clusters - map from node id to cluster, clusters for nodes from smaller cluster must be updated if combined
   */
  public void combine(Cluster c1, Cluster c2, Map<Integer, Cluster> clusters) {
    double meanC1 = c1.getAvgRadius();
    double meanC2 = c2.getAvgRadius();
    double newMean = c1.meanRadiusClusters(c2);
    double maxMult1 = Math.min(2 - (0.05 * c1.getSize()/c2.getSize()), 1.3); //experiment with this
    double maxMult2 = Math.min(2 - (0.05 * c2.getSize()/c2.getSize()), 1.3); //experiment with this
    double diff2 = newMean/(0.5*radiusThreshold); //experiment
    // checks if mean radius of new cluster is less than threshold, and the either new mean isn't too much more than
    // both old means or if the new mean is small enough to compensate for increasing old mean significantly
    if (!c1.equals(c2) && newMean < radiusThreshold &&
        ((newMean < maxMult1*meanC1 && newMean < maxMult2*meanC2) || diff2 < 1)) {
      System.out.println("merging: " + c1.getSize() + " - " + c2.getSize());
      // finds smaller cluster, so combination can be more efficient
      if (c1.getSize() >= c2.getSize()) {
        c1.addNodes(c2);
        for (Article a: c2.getNodes()) {
          clusters.replace(a.getId(), c1);
        }
      } else {
        c2.addNodes(c1);
        for (Article a: c1.getNodes()) {
          clusters.replace(a.getId(), c2);
        }
      }
    } // Could have an else that decides a better split of the clusters
  }

  /**
   * Sets the threshold (precent) for which edges to keep from the original graph
   */
  @Override
  public void setThreshold() {
    System.out.println(nodes.size());
    System.out.println(edges.size());
    this.threshold = 2.0*nodes.size()/edges.size(); //set so that number of edges will be twice the number of nodes
  }

  /**
   * Sets the radius threshold for clusters based on both the normal mean of graph and specifics of this graph
   * @param articles
   */
  public void setRadiusThreshold(Set<Article> articles) {
    double graphMeanRadius = meanRadiusSet(articles);
    System.out.println("mean Radius: " + graphMeanRadius);
    double normalGraphMeanRadius = .5; //experiment
    double percentCurrent = .5; //experiment with this value -- how much to use current graph in threshold
    double percentFullGraph = 1.2; //experiment -- how many times tighter clusters should be than normal graph
    this.radiusThreshold = (percentCurrent*graphMeanRadius + (1-percentCurrent)*normalGraphMeanRadius)/percentFullGraph;
  }

  /**
   * Method to create a random graph with edge weights between 0 and 1
   * @param size - number of nodes for the graph
   */
  public void createGraph(int size) {
    for (int i = 0; i < size; i++) {
      Article node = new Article(i);
      this.nodes.add(node);
    }
    for (Article a: nodes) {
      for (Article b: nodes) {
        if (a.getId() < b.getId()) {
          this.edges.add(new Similarity(a, b, Math.random())); // + Math.abs(Math.sin(a.getId())) + Math.abs(Math.sin(b.getId()))));
        }
      }
    }
    for (Article a: nodes) {
      a.setEdges(edges);
    }
  }


}






