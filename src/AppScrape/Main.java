package AppScrape;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 2/21/14
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    static ArrayList<ArrayList<String>> urls;

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
        try {
            RobotController test = new RobotController();

            urls = test.begin("C:/iTunes/iTunes.exe");


        } catch (AWTException | InterruptedException | UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }

}
