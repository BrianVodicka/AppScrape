package AppScrape;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.sql.*;

/**
 * User: Brian
 * Date: 2/24/14
 */
public class Indexer implements Runnable{

    //private static Connection c;
    private int category;
    private ArrayList<AppObject> objectList;

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

    public Indexer(int category, ArrayList<String> objectList){
        this.category = category;

        ArrayList<AppObject> objects = new ArrayList<>();
        for(int j = 1; j < objectList.size(); j++) {
            String[] target = objectList.get(j).split(" ");
            //System.out.println(target[0] + " " + Integer.parseInt(target[0]) + " " + target[1]);
            objects.add(new AppObject(target[1], Integer.parseInt(target[0]), categories[category]));
        }

        this.objectList = objects;
        /*c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/masterDB.db");
            c.setAutoCommit(false);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
        }*/
    }

    public void index(int category, ArrayList<AppObject> list) throws FileNotFoundException, UnsupportedEncodingException {
        try {
            Connection c = null;
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/masterDB.db");
            c.setAutoCommit(false);
            Statement stmt = null;
            String query;
            for (AppObject target : list) {
                try {

                    stmt = c.createStatement();

                    query = ("SELECT COUNT(*) AS total FROM " + categories[category] + " WHERE name = '" + target.getTitle() + "';");
                    ResultSet rs = stmt.executeQuery(query);
                    int i = rs.getInt("total");
                    rs.close();

                    query = ("UPDATE " + categories[category] + " SET updated = 0;");
                    stmt.executeUpdate(query);

                    query = ("SELECT * FROM " + categories[category] + " WHERE name = '" + target.getTitle() + "';");

                    if(i == 1) {
                        ResultSet ss = stmt.executeQuery(query);
                        System.out.println("-- Size 1 -- \n -- For: " + target.getTitle());
                        Integer oldRank = ss.getInt("today");
                        int newRank = target.getRank();
                        int diff = oldRank - newRank;
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
            // for all leftover apps...
            stmt = c.createStatement();
            query = ("UPDATE " + categories[category] + " SET today = 201 WHERE updated = " + 0 + ";");
            stmt.executeUpdate(query);
            stmt.close();
            c.close();
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            index(this.category, this.objectList);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
