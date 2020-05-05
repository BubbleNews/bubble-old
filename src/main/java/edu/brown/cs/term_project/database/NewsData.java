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

  public static void main(String[] args) throws Exception {
//    Article testArticle = new Article("BuzzFeed", new String[]{"Kayla Suazo"},
//        "23 Top-Rated Cleaning Products That Are Popular For A Reason",
//        "So good, they have *a ton* of 4- and 5-star reviews.",
//        "https://www.buzzfeed.com/kaylasuazo/top-rated-cleaning-products-"
//            + "that-are-popular-for-a-reason",
//        "2020-04-27T17:22:24.963019Z",
//        "all you have to do be let it sit for 15 minute and wipe -- minimal work on you "
//            + "part . check out BuzzFeed 's full write-up on this Feed-N-Wax Wood Polish to learn "
//            + "more!and ! it have 4,700 + positive review on amazon.promising review : `` OMG ! "
//            + "this be the most amazing product ! we inherit some antique furniture from the '30s"
//            + " that have be in storage forever ... it be dry and dirty and not much to look at ."
//            + " I use this product on it and the oak wood literally come alive show the beautiful"
//            + " grain and texture of the wood . I have since use it on my oak kitchen cabinet and"
//            + " they look AMAZING ! I will never use anything else other than this product on my "
//            + "wood surface ! no greasy feel -- and a fantastic smell ! '' -- Tiffany SadowskiGet"
//            + " it from Amazon for $ 8.48 + -lrb- available in eight size -rrb- ."
//    );
//    NewsData db = new NewsData("data/backloaded.db");
//    db.insertArticle(testArticle);
  }
}
