package edu.brown.cs.term_project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abstract class for connections to database.
 */
public abstract class Database {
  private Connection conn;

  //TODO: Remove filename from this function
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
    Class.forName("com.mysql.cj.jdbc.Driver");
//    String urlToDB = "jdbc:sqlite:" + filename;
    // for gcp below
    //String urlToDB = "jdbc:sqlite::resource:" + filename;
    this.conn = DriverManager.getConnection(
        "jdbc:mysql:///Bubble?cloudSqlInstance=bubble-277622:us-east1:bubble-news" +
            "-database&socketFactory=com.google.cloud.sql.mysql" +
            ".SocketFactory&user=root&password=benianjohnkshitij");
//    this.conn = DriverManager.getConnection("jdbc:mysql://35.243.149.133/Bubble",
//        "root", "benianjohnkshitij");
  }

  /**
   * Getter for connection.
   * @return connection to a database
   */
  public Connection getConn() {
    return conn;
  }
}
