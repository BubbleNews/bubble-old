package edu.brown.cs.term_project.api.handlers;

import edu.brown.cs.term_project.database.NewsData;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class ChartHandlerTest {
  private static NewsData db;
  @Before
  public void setUp() throws SQLException, ClassNotFoundException {
    db = new NewsData("data/news_data_read_test.sqlite3");
  }
  @Test
  public void testHandle() {
  }
}