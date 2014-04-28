package AppScrape;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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

        Gson gson = new GsonBuilder().create();
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
