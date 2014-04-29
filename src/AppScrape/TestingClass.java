package AppScrape;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
    public static void main(String[] args) throws FileNotFoundException, AWTException, InterruptedException {
        RobotController robot = new RobotController();
        boolean tr = true;

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:/AppDir/apps/testDB.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String sql = "INSERT INTO test (name) " +
                    "VALUES ('temp123');";
            stmt.executeUpdate(sql);

            // ("SELECT name FROM " + category + " WHERE name = ' + app.getName() + "';");
            //String st = "SELECT name FROM test WHERE name = 'brian';";
            //Statement state = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //stmt = c.prepareStatement(st, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery( "SELECT name FROM test WHERE name = 'brian';" );
            int size = 0;
            while ( rs.next() ) {
                size++;
            }
            rs = stmt.executeQuery( "SELECT name FROM test WHERE name = 'brian';" );
            if(size == 1) {
                //rs.next();
                String name = rs.getString("name");
                System.out.println(name);
                System.out.println("1");
            } else if (size == 0) {
                System.out.println(0);
            } else { // size == 0
                // create app in table
                System.out.println(-1);
            }

            //rs.last();
            //System.out.println(rs.getRow());
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            return;
        }
        System.out.println("Opened database successfully");
        System.out.println("Records created successfully");

        /*Gson gson = new GsonBuilder().create();
        gson.toJson("Hello", System.out);
        gson.toJson(123, System.out);

        /*while (tr) {
            Point p = MouseInfo.getPointerInfo().getLocation();

            Color color = robot.getPixelColor(p.x,p.y);
            System.out.println(color);
            /*if (color >= 240 && color <=247) {
                Color col = robot.getPixelColor(p.x,p.y);
                if (!(col.getRed() == col.getBlue()) || !(col.getBlue() == col.getGreen())){
                    System.out.println(robot.getPixelColor(p.x,p.y));
                }
            }
            else if (!(color >= 240 && color <=247)) {
                System.out.println(robot.getPixelColor(p.x,p.y));
            }*/

            //System.out.println(MouseInfo.getPointerInfo().getLocation());
            //Thread.sleep(1000);
        //}
        //File dir = new File("C:\\AppDir\\tracking");
        //for (int i = 0; i < categories.length; i++){
            /*File temp = new File(dir, categories[i]);
            PrintWriter writer = new PrintWriter(temp);
            writer.print("");
            writer.close();*/
        //}

        /*String s = "~#title:Job Search ~#rank:1 ~#category:Business ~#releaseDate:Nov 08, 2013 ~#totalRatings:190 ~#avgRating:3 ~#url:https://itunes.apple.com/us/app/job-search/id309735670?mt=8";
        String[] split = s.split("~");
        for (int i = 0; i < split.length; i++) {
            System.out.println(i + " " + split[i]);
        }*/
    }
}
