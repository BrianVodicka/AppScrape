package AppScrape;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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
        "SocialNetworkingFree",
        "SportsPaid",
        "SportsFree",
        "TravelPaid",
        "TravelFree",
        "UtilitiesPaid",
        "UtilitiesFree",
        "WeatherPaid",
        "WeatherFree"
    };

    public Indexer(int category, ArrayList<String> objectList){
        this.category = category;

        ArrayList<AppObject> objects = new ArrayList<>();
        for(int j = 1; j < objectList.size(); j++) {
            String[] target = objectList.get(j).split(" ");
            objects.add(new AppObject(target[1], Integer.parseInt(target[0]), categories[category]));
        }

        this.objectList = objects;
    }

    public Indexer(){
        try {
            Connection c;
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/newDB.db");
            c.setAutoCommit(false);

            Statement stmt = c.createStatement();
            String query = ("SELECT eFifty from MasterTable WHERE name = 'date';");
            ResultSet rs = stmt.executeQuery(query);
            int date = rs.getInt("eFifty");
            rs.close();
            stmt.close();

            query = ("UPDATE MasterTable SET eFifty = " + ++date + " WHERE name = 'date';");
            PreparedStatement update = c.prepareStatement(query);
            update.executeUpdate();
            update.close();
            c.commit();
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void index(int category, ArrayList<AppObject> list) throws FileNotFoundException, UnsupportedEncodingException {

        try {
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

            c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/newDB.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            query = ("INSERT INTO " + categories[category] + "(name, url, rank, date) VALUES('ma', 'as', 4, 6);");
            stmt.executeUpdate(query);
            c.close();

            for (AppObject target : list) {
                try {
                    c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/newDB.db");
                    c.setAutoCommit(false);
                    stmt = c.createStatement();
                    query = ("INSERT INTO " + categories[category] + "(name, url, rank, date) VALUES('" + target.getTitle() + "', '" + target.getURL() + "', " + target.getRank() + ", " + date + ");");
                    stmt.executeUpdate(query);
                    statement.close();

                    stmt = c.createStatement();
                    query = ("SELECT rank, date FROM " + categories[category] + " WHERE url = '" + target.getURL() + "' ORDER BY date DESC;");
                    rs = stmt.executeQuery(query);
                    int today = rs.getInt("date");
                    int rank = rs.getInt("rank");
                    rs.next();
                    int yesterday = rs.getInt("date");
                    if (today - yesterday == 1) {
                        int yesterdayRank = rs.getInt("rank");
                        query = ("UPDATE " + categories[category] + " SET change = " + String.valueOf(yesterdayRank - rank) + " WHERE url = '" + target.getURL() + "';");
                        PreparedStatement sql = c.prepareStatement(query);
                        sql.executeUpdate();
                        sql.close();
                    } // else 1 day change unable to be calculated
                    else {
                        System.out.println("unable to find yesterday");
                    }
                    rs.close();
                    stmt.close();
                    c.commit();
                    c.close();

                } catch (Exception e) {
                        e.printStackTrace();
                }
            }
            c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/newDB.db");
            stmt = c.createStatement();
            query = ("SELECT change FROM " + categories[category] + " WHERE rank BETWEEN 0 AND 50;");
            rs = stmt.executeQuery(query);
            Map<Integer, MutableInt> valuesMap = getValuesMap(rs);
            rs.close(); // TODO: maybe this isn't needed?
            stmt.close();

            updateExpectedValueAndVariance(valuesMap, categories[category], c, 0);

            stmt = c.createStatement();
            query = ("SELECT change FROM " + categories[category] + " WHERE rank BETWEEN 51 AND 120;");
            rs = stmt.executeQuery(query);
            valuesMap = getValuesMap(rs);
            rs.close(); // TODO: maybe this isn't needed?
            stmt.close();

            updateExpectedValueAndVariance(valuesMap, categories[category], c, 1);

            stmt = c.createStatement();
            query = ("SELECT change FROM " + categories[category] + " WHERE rank BETWEEN 121 AND 200;");
            rs = stmt.executeQuery(query);
            valuesMap = getValuesMap(rs);
            rs.close(); // TODO: maybe this isn't needed?
            stmt.close();

            updateExpectedValueAndVariance(valuesMap, categories[category], c, 2);
            c.close();
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private Map<Integer, MutableInt> getValuesMap(ResultSet rs) throws SQLException {
        Map<Integer, MutableInt> valuesMap = new HashMap<>();
        while (rs.next()) {
            int temp = rs.getInt("change");
            MutableInt count = valuesMap.get(temp);
            if (count == null){
                valuesMap.put(temp, new MutableInt());
            } else {
                count.increment();
            }
        }
        rs.close();
        return valuesMap;
    }

    private void updateExpectedValueAndVariance(Map<Integer, MutableInt> valuesMap, String category, Connection c, int val) throws SQLException {
        // computes the expected value and variance for all positive changes
        int i = 0;
        int expectedValue = 0;
        int variance = 0;
        for (Integer m : valuesMap.keySet()) {
            if (Math.signum(m) < 0)
                continue;
            i++; // count of positive numbers
            int temp = valuesMap.get(m).get();
            expectedValue += (m * temp);
            variance += (m * (temp * temp));
        }
        if (i == 0)
            return;

        expectedValue = expectedValue / i;
        variance = variance / i;

        String eValString = null;
        String varianceString = null;
        switch (val) {
            case 0:
                eValString = "eFifty";
                varianceString = "varFifty";
                break;
            case 1:
                eValString = "eOneTwenty";
                varianceString = "varOneTwenty";
                break;
            case 2:
                eValString = "eTwoHundred";
                varianceString = "varTwoHundred";
                break;
        }

        String query = ("UPDATE MasterTable SET " + eValString + " =  " + expectedValue + ", " + varianceString + " = " + variance + " WHERE name = '" + category + "';");
        PreparedStatement sql = c.prepareStatement(query);
        sql.executeUpdate();
        sql.close();
    }

    @Override
    public void run() {
        try {
            index(this.category, this.objectList);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    class MutableInt {
        int value = 1; // note that we start at 1 since we're counting
        public void increment () { ++value;      }
        public int  get ()       { return value; }
    }

}

