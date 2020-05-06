package edu.brown.cs.term_project.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class with connection to the database, contains an instance of a class that
 * controls reading from the database and an instance of a class that controls
 * writing to the database.
 */
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

  /**
   * Gets the object with methods to read from the database.
   * @return the database reader
   */
  public NewsDataRead getDataRead() {
    return dataRead;
  }

  /**
   * Gets the object with methods to write to the database.
   * @return the database writer
   */
  public NewsDataWrite getDataWrite() {
    return dataWrite;
  }

}
