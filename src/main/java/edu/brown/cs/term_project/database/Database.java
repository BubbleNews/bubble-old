package edu.brown.cs.term_project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Database {
  private Connection conn;

  /**
   * A constructor to setup connection to SQLDatabase. Sets up for querying the sql database and
   * error checks the file.
   *
   * @param filename the filename that the user wants to query from
   * @throws SQLException           if sql error
   * @throws ClassNotFoundException if issue setting up database connection
   */
  public Database(String filename) throws SQLException, ClassNotFoundException {
    // setup sql connection to given file
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    this.conn = DriverManager.getConnection(urlToDB);
  }

  /**
   * Getter for connection.
   * @return connection to a database
   */
  public Connection getConn() {
    return conn;
  }
}
