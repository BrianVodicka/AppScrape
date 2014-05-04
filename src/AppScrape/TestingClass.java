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
        Statement stmt;
        String query;

        query = ("SELECT eFifty FROM MasterTable WHERE name = 'date';");
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
        stmt.executeUpdate(query);

        rs.close();
        c.commit();
        c.close();
        }
}
