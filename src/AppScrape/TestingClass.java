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
        Connection c = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/masterDB.db");
        c.setAutoCommit(false);
        //for (AppObject target : list) {
            try {
                Statement stmt = null;
                stmt = c.createStatement();
                //System.out.println(target.getTitle());
                String query = ("SELECT COUNT(*) AS total FROM BooksPaid WHERE name = 'testll';");
                PreparedStatement statement = c.prepareStatement(query);
                ResultSet rs = stmt.executeQuery(query);
                //ResultSet rs = statement.getGeneratedKeys();
                int i = rs.getInt("total");
                System.out.println(i);
                rs.close();
                /*ResultSet ss = statement.getGeneratedKeys();
                if(size == 1) {
                    System.out.println("-- Size 1 -- \n -- For: " + target.getTitle());
                    Integer oldRank = ss.getInt("today");
                    int newRank = target.getRank();
                    int diff = newRank - oldRank;
                    query = ("UPDATE name FROM " + categories[category] + " SET today = " + newRank + ", yesterday = " + diff + " WHERE name = '" + target.getTitle() + "';");
                    PreparedStatement sql = c.prepareStatement(query);
                    sql.executeUpdate();
                } else if (size == 0) {
                    System.out.println(" -- Size 0 -- \n -- For: " + target.getTitle());
                    System.out.println("INSERT INTO " + categories[category] + " (name, url, today) VALUES ('" + target.getTitle() +"', '" + target.getURL() + "', " + target.getRank() + ");");
                    query = ("INSERT INTO " + categories[category] + " (name, url, today) VALUES ('" + target.getTitle() +"', '" + target.getURL() + "', " + target.getRank() + ");");
                    PreparedStatement sql = c.prepareStatement(query);
                    sql.executeUpdate();
                } else {
                    throw new Exception("Multiple apps found for title: " + target.getTitle());
                }*/
                stmt.close();
                c.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        c.close();
        }
}
