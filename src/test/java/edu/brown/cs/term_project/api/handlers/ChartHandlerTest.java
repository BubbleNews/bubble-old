package edu.brown.cs.term_project.api.handlers;

import edu.brown.cs.term_project.database.NewsData;
import edu.brown.cs.term_project.main.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class ChartHandlerTest {
  private static NewsData db;
  @Before
  public void setUp() throws SQLException, ClassNotFoundException {
    db = new NewsData("data/for_database_tests.sqlite3");
  }

  @After
  public void tearDown() {
    db = null;
  }
  @Test
  public void testHandle() throws SQLException, ClassNotFoundException {
  }
}