package edu.brown.cs.term_project.Graph;

import java.util.List;

public interface ICluster<T extends INode> {

  public List<T> getNodes();

  public Integer getSize();

  public String getName();

  public double getAvgConnections();

  public double getAvgRadius();

  public double getStd();

}
