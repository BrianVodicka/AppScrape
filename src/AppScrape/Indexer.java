package AppScrape;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.sql.*;

/**
 * User: Brian
 * Date: 2/24/14
 */
public class Indexer {

    //private static Connection c;

    static final String[] categories = {
        "BooksPaid",
        "BooksFree",
        "BusinessPaid",
        "BusinessFree",
        "CatalogsPaid",
        "CatalogsFree",
        "EducationPaid",
        "EducationFree",
        "EntertainmentPaid",
        "EntertainmentFree",
        "FinancePaid",
        "FinanceFree",
        "FoodPaid",
        "FoodFree",
        "GamesPaid",
        "GamesFree",
        "HealthPaid",
        "HealthFree",
        "KidsPaid",
        "KidsFree",
        "LifestylePaid",
        "LifestyleFree",
        "MedicalPaid",
        "MedicalFree",
        "MusicPaid",
        "MusicFree",
        "NavigationPaid",
        "NavigationFree",
        "NewsPaid",
        "NewsFree",
        "NewsstandPaid",
        "NewsstandFree",
        "PhotoVideoPaid",
        "PhotoVideoFree",
        "ProductivityPaid",
        "ProductivityFree",
        "ReferencePaid",
        "ReferenceFree",
        "SocialNetworkingPaid",
        "SocialNetworkingFree"
    };

    public static void main(String[] args){
        String timeStamp2 = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp2);
    }

    public Indexer(){
        /*c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/masterDB.db");
            c.setAutoCommit(false);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
        }*/
    }

    public static void index(int category, ArrayList<AppObject> list) throws FileNotFoundException, UnsupportedEncodingException {
        try {
            Connection c = null;
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/masterDB.db");
            c.setAutoCommit(false);
            for (AppObject target : list) {
                try {
                    Statement stmt = null;
                    stmt = c.createStatement();
                    System.out.println(target.getTitle());
                    /*String query = ("SELECT name FROM " + categories[category] + " WHERE name = '" + target.getTitle() + "';");
                    PreparedStatement statement = c.prepareStatement(query);
                    ResultSet rs = statement.getGeneratedKeys();
                    int size = 0;
                    while( rs.next() ) {
                        size++;
                        System.out.println(size);
                    }*/

                    String query = ("SELECT COUNT(*) AS total FROM " + categories[category] + " WHERE name = '" + target.getTitle() + "';");
                    ResultSet rs = stmt.executeQuery(query);
                    int i = rs.getInt("total");
                    rs.close();

                    query = ("SELECT * FROM " + categories[category] + " WHERE name = '" + target.getTitle() + "';");

                    if(i == 1) {
                        ResultSet ss = stmt.executeQuery(query);
                        System.out.println("-- Size 1 -- \n -- For: " + target.getTitle());
                        Integer oldRank = ss.getInt("today");
                        int newRank = target.getRank();
                        int diff = newRank - oldRank;
                        query = ("UPDATE " + categories[category] + " SET today = " + newRank + ", yesterday = " + diff + " WHERE name = '" + target.getTitle() + "';");
                        PreparedStatement sql = c.prepareStatement(query);
                        sql.executeUpdate();
                    } else if (i == 0) {
                        PreparedStatement statement = c.prepareStatement(query);
                        ResultSet ss = statement.getGeneratedKeys();
                        System.out.println(" -- Size 0 -- \n -- For: " + target.getTitle());
                        System.out.println("INSERT INTO " + categories[category] + " (name, url, today) VALUES ('" + target.getTitle() +"', '" + target.getURL() + "', " + target.getRank() + ");");
                        query = ("INSERT INTO " + categories[category] + " (name, url, today) VALUES ('" + target.getTitle() +"', '" + target.getURL() + "', " + target.getRank() + ");");
                        PreparedStatement sql = c.prepareStatement(query);
                        sql.executeUpdate();
                    } else {
                        throw new Exception("Multiple apps found for title: " + target.getTitle());
                    }
                    stmt.close();
                    c.commit();

                } catch (Exception e) {
                        e.printStackTrace();
                }
            }
            c.close();
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
