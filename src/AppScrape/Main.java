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
            //Indexer index = new Indexer();
            urls = test.begin("C:/iTunes/iTunes.exe");
            /*for (int i = 0; i < urls.size(); i++) {
                ArrayList<String> temp = urls.get(i);
                ArrayList<AppObject> objects = new ArrayList<>();
                for(int j = 1; j < temp.size(); j++) {
                    String[] target = temp.get(j).split(" ");
                    //System.out.println(target[0] + " " + Integer.parseInt(target[0]) + " " + target[1]);
                    String m = categories[i];
                    objects.add(new AppObject(target[1], Integer.parseInt(target[0]), categories[i]));
                }
                System.out.println("Indexing " + categories[i] + "...");*/
                //index.index(i, objects);

        } catch (AWTException | InterruptedException | UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }

    /*private static void compareWith(AppObject original, File readFile, PrintWriter writer, PrintWriter trackingWriter, PrintWriter majorWriter, int time){
        FileReader reader = null;
        File dir = new File("C:\\AppDir\\changes");
        try {
            reader = new FileReader(readFile);
            BufferedReader fileReader = new BufferedReader(reader);
            String readLine;
            String title = original.getTitle();
            while ((readLine = fileReader.readLine()) != null) {
                if (readLine.contains(title)) {
                    String[] items = readLine.split("~");
                    int rank = Integer.valueOf(items[2].substring(6, items[2].length() - 1));
                    int difference = original.getRank() - rank;
                    if (difference < 2)
                        continue;

                    if (difference > 15)
                        majorWriter.print("+" + difference + " over last " + time + " day(s) for: " + original.getTitle() + " " + original.getURL() + '\n');

                    // add to tracking list if good app
                    if (difference > 30)
                        trackingWriter.print(original.getTitle());
                    writer.print("+" + difference + " over last " + time + " day(s) for: " + original.getTitle() + " " + original.getURL() + '\n');
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }*/

}
