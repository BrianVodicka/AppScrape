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

    private Connection c;

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
        c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/masterDB.db");
            c.setAutoCommit(false);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void index(int category, ArrayList<AppObject> list) throws FileNotFoundException, UnsupportedEncodingException {
        Statement stmt = null;
        try {
            for (AppObject target : list) {
                stmt = c.createStatement();
                String query = ("SELECT name FROM " + categories[category] + " WHERE name = '" + target.getTitle() + "';");
                ResultSet rs = stmt.executeQuery(query);
                int size = 0;
                while( rs.next() ) {
                    size++;
                }
                rs = stmt.executeQuery(query);
                if(size == 1) {
                    Integer oldRank = rs.getInt("rank");
                    int newRank = target.getRank();
                    int diff = newRank - oldRank;
                    query = ("UPDATE name FROM " + categories[category] + " SET today = " + newRank + ", yesterday = " + diff + " WHERE name = '" + target.getTitle() + "';");
                    stmt.executeQuery(query);
                } else if (size == 0) {
                    query = ("INSERT INTO " + categories[category] + " (name, url, rank) VALUES ('" + target.getTitle() +"', '" + target.getURL() + "', " + target.getRank());
                    stmt.executeQuery(query);
                } else {
                    throw new Exception("Multiple apps found for title: " + target.getTitle());
                }

            }
            stmt.close();
            c.commit();
        } catch (SQLException e) {
            e.getStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
