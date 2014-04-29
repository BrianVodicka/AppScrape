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
            for (int i = 0; i < urls.size(); i++) {
                ArrayList<String> temp = urls.get(i);
                ArrayList<AppObject> objects = new ArrayList<>();
                for(int j = 1; j < temp.size(); j++) {
                    String[] target = temp.get(j).split(" ");
                    System.out.println(target[0] + " " + Integer.parseInt(target[0]) + " " + target[1]);
                    String m = categories[i];
                    objects.add(new AppObject(target[1], Integer.parseInt(target[0]), categories[i]));
                }
                System.out.println("Indexing " + categories[i] + "...");
                //Indexer.index(i, objects);
            }


            //Movement charting...
            File folder = new File("C:\\AppDir");
            File listOfFiles[] = folder.listFiles();

            File trackingFoler = new File("C:\\AppDir\\tracking");
            File listOfTracking[] = trackingFoler.listFiles();

            File majorDir = new File("C:\\AppDir\\majorChanges");
            File majorChanges = new File(majorDir, "major changes " + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
            PrintWriter majorWriter = new PrintWriter(majorChanges);

            for (int i = 0; i < categories.length; i++) {
                ArrayList<File> tempFiles = new ArrayList<>();
                for (File file : listOfFiles) {
                    if (file.getName().contains(categories[i])) {
                        tempFiles.add(file);
                    }
                }
                if (tempFiles.size() < 2) {
                    return;
                }

                // todo: determine how to remove items from tracking list after sufficient time
                ArrayList<String> trackingList = new ArrayList<>();
                File trackingFile = null;
                for (File file : listOfTracking) {
                    if (file.getName().contains(categories[i])) {
                        trackingFile = file;
                        FileReader fileReader = new FileReader(file);
                        BufferedReader reader = new BufferedReader(fileReader);
                        String readLine;
                        while ((readLine = reader.readLine()) != null)
                            trackingList.add(readLine);
                    }
                }
                PrintWriter trackingWriter = new PrintWriter(trackingFile, "UTF-8");

                // TODO: change array list to array of tempFiles.size
                File[] orderedTempFiles = new File[tempFiles.size()];
                List<Integer> creationDates = new ArrayList<>();
                int size = tempFiles.size();
                for (int j = 0; j < size; j++) {
                    String name = tempFiles.get(j).getName();
                    String s = name.substring(name.length() - 8, name.length());
                    int mi = 4;
                    creationDates.add(Integer.valueOf(name.substring(name.length() - 8, name.length())));
                }
                Collections.sort(creationDates);
                for (int j = 0; j < size; j++) {
                    for (File file : tempFiles) {
                        if (file.getName().contains(String.valueOf(creationDates.get(j))))
                            orderedTempFiles[j] = file;
                        //tempFiles.remove(file);
                    }
                }
                File mostRecent = orderedTempFiles[0];

                FileReader fileReader = new FileReader(mostRecent);
                BufferedReader reader = new BufferedReader(fileReader);
                String readLine;

                File dir = new File("C:\\AppDir\\changes");
                File targetFile = new File(dir, "changes for " + categories[i] + " - " + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
                PrintWriter writer = new PrintWriter(targetFile, "UTF-8");

                // todo: determine how many histories to go into for difference determining
                while ((readLine = reader.readLine()) != null) {
                    // TODO: instead of creating an app object, just get title from line?
                    // TODO: different methods for tracking list and typical app?
                    AppObject app = new AppObject(readLine);
                    String title = app.getTitle();

                    compareWith(app, orderedTempFiles[2], writer, trackingWriter, majorWriter, 1);

                    if (!trackingList.contains(title))
                        continue;

                    // search extra because it's on the tracking list ----
                    if (orderedTempFiles.length < 3)
                        continue;
                    if (orderedTempFiles.length == 3)
                        compareWith(app, orderedTempFiles[3], writer, trackingWriter, majorWriter, 2);
                    else if (orderedTempFiles.length == 4) {
                        compareWith(app, orderedTempFiles[3], writer, trackingWriter, majorWriter, 2);
                        compareWith(app, orderedTempFiles[4], writer, trackingWriter, majorWriter, 3);
                    } else {
                        compareWith(app, orderedTempFiles[3], writer, trackingWriter, majorWriter, 2);
                        compareWith(app, orderedTempFiles[4], writer, trackingWriter, majorWriter, 3);
                        compareWith(app, orderedTempFiles[5], writer, trackingWriter, majorWriter, 4);
                    }
                }
                writer.close();
                trackingWriter.close();
            }
            majorWriter.close();
        } catch (AWTException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compareWith(AppObject original, File readFile, PrintWriter writer, PrintWriter trackingWriter, PrintWriter majorWriter, int time){
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
    }

}
