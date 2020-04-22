package edu.brown.cs.term_project.Graph;

import java.util.List;
import java.util.Set;

public interface ICluster<T extends INode> {

  public Integer getId();

  public Set<T> getNodes();

  public Integer getSize();

  public T getHeadNode();

  public double getAvgConnections();

  public double getAvgRadius();

  public double getStd();

  public void addNode(T node);

  public void adjustHead();

}
