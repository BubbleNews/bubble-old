package edu.brown.cs.term_project.database;

import java.sql.Connection;
import java.sql.SQLException;

public final class NewsData extends Database {
  private final Connection conn;
  private final NewsDataRead dataRead;
  private final NewsDataWrite dataWrite;

  /**
   * A constructor to setup connection to SQLDatabase. Sets up for querying the sql database and
   * error checks the file.
   *
   * @param filename the filename that the user wants to query from
   * @throws SQLException           if sql error
   * @throws ClassNotFoundException if issue setting up database connection
   */
  public NewsData(String filename) throws SQLException, ClassNotFoundException {
    super(filename);
    conn = super.getConn();
    dataRead = new NewsDataRead(conn);
    dataWrite = new NewsDataWrite(conn);
  }

  public NewsDataRead getDataRead() {
    return dataRead;
  }

  public NewsDataWrite getDataWrite() {
    return dataWrite;
  }

}
