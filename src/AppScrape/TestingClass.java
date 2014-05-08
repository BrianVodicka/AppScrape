package AppScrape;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 4/12/14
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestingClass {
    public static void main(String[] args) throws FileNotFoundException, AWTException, InterruptedException, ClassNotFoundException, SQLException {
        Connection c;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/newDB.db");
        c.setAutoCommit(false);
        Statement stmt, statement;
        String query;

        /*c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/newDB.db");
        c.setAutoCommit(false);
        stmt = c.createStatement();
        query = ("INSERT INTO " + categories[category] + "(name, url, rank, date) VALUES('" + target.getTitle() + "', '" + target.getURL() + "', " + target.getRank() + ", " + date + ");");
        stmt.executeUpdate(query);*/
        //statement.close();

        stmt = c.createStatement();
        query = ("SELECT rank, date FROM GamesPaid WHERE url = 'https://itunes.apple.com/us/app/blek/id742625884?mt=8' ORDER BY date DESC;");
        ResultSet rs = stmt.executeQuery(query);
        int today = rs.getInt("date");
        int rank = rs.getInt("rank");
        System.out.println("today is: " + today);
        System.out.println("rank is: " + rank);
        try {
        rs.next();
        rs.next();
        } catch (SQLException e) {
            System.out.println("go fuck yourself");
            return;
        }
        int yesterday = rs.getInt("date");
        System.out.println("yesterday is: " + yesterday);
        if (today - yesterday == 1) {
            int yesterdayRank = rs.getInt("rank");
            System.out.println("yay");
            //query = ("UPDATE " + categories[category] + " SET change = " + String.valueOf(yesterdayRank - rank) + " WHERE url = '" + target.getURL() + "';");
            //PreparedStatement sql = c.prepareStatement(query);
            //sql.executeUpdate();
            //sql.close();
        } // else 1 day change unable to be calculated
        else {
            System.out.println("unable to find yesterday");
        }
        rs.close();
        stmt.close();
        c.commit();
        c.close();

        /*query = ("SELECT eFifty FROM MasterTable WHERE name = 'date';");
        PreparedStatement statement = c.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        int date = rs.getInt("eFifty");
        rs.close();
        statement.close();
        c.commit();
        c.close();

        c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/newDB.db");
        c.setAutoCommit(false);
        stmt = c.createStatement();
        query = ("INSERT INTO BooksFree(name, url, rank, date) VALUES('ma', 'as', 4, 6);");
        stmt.executeUpdate(query);*/

        rs.close();
        //c.commit();
        c.close();
        }
}
