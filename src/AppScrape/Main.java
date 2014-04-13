package AppScrape;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.net.URL;
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
            "Books - Paid",
            "Books - Free",
            "Business - Paid",
            "Business - Free",
            "Catalogs - Paid",
            "Catalogs - Free",
            "Education - Paid",
            "Education - Free",
            "Entertainment - Paid",
            "Entertainment - Free",
            "Finance - Paid",
            "Finance - Free",
            "Food & Drink - Paid",
            "Food & Drink - Free",
            "Games - Paid",
            "Games - Free",
            "Health & Fitness - Paid",
            "Health & Fitness - Free",
            "Kids - Paid",
            "Kids - Free",
            "Lifestyle - Paid",
            "Lifestyle - Free",
            "Medical - Paid",
            "Medical - Free",
            "Music - Paid",
            "Music - Free",
            "Navigation - Paid",
            "Navigation - Free",
            "News - Paid",
            "News - Free",
            "Newsstand - Paid",
            "Newsstand - Free",
            "Photo & Video - Paid",
            "Photo & Video - Free",
            "Productivity - Paid",
            "Productivity - Free",
            "Reference - Paid",
            "Reference - Free",
            "Social Networking - Paid",
            "Social Networking - Free"
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
                    objects.add(new AppObject(target[1], Integer.parseInt(target[0])));
                }
                Indexer.index(Integer.parseInt(temp.get(0)), objects);
            }


            //Movement charting...
            File folder = new File("C:\\AppDir");
            File listOfFiles[] = folder.listFiles();

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
                File[] orderedTempFiles = new File[tempFiles.size()];
                List<Integer> creationDates = new ArrayList<>();
                int size = tempFiles.size();
                for (int j = 0; j < size; j++) {
                    String name = tempFiles.get(j).getName();
                    creationDates.add(Integer.valueOf(name.substring(name.length() - 14, name.length())));
                }
                Collections.sort(creationDates);
                for (int j = 0; j < size; j++) {
                    for (File file : tempFiles) {
                        if (file.getName().contains(String.valueOf(creationDates.get(j))))
                            orderedTempFiles[j] = file;
                    }
                }
                File mostRecent = orderedTempFiles[0];

                FileReader fileReader = new FileReader(mostRecent);
                BufferedReader reader = new BufferedReader(fileReader);
                String readLine;
                String targetFileName = "changes for " + categories[i];
                File dir = new File("C:\\AppDir");
                File targetFile = new File(dir, targetFileName);
                // todo: determine how many histories to go into for difference determining
                while ((readLine = reader.readLine()) != null) {
                    AppObject app = new AppObject(readLine);
                    String title = app.getTitle();
                    FileReader secondFileReader = new FileReader(orderedTempFiles[1]);
                    BufferedReader secondReader = new BufferedReader(secondFileReader);
                    String secondReadLine;
                    while ((secondReadLine = secondReader.readLine()) != null) {
                        if (secondReadLine.contains(title)) {
                            String[] items = secondReadLine.split("~");
                            int rank = Integer.valueOf(items[2].substring(6, items[2].length()));
                            PrintWriter writer = new PrintWriter(targetFile, "UTF-8");
                            int difference = app.getRank() - rank;
                            writer.write(app.getTitle() + " " + difference);
                            break;
                        }
                    }
                }
            }

            // TODO FIX OBO ERROR
            /*for (int i = 0; i < appObjects.length; i++) {
                ArrayList<AppObject> temp = new ArrayList<>();
                for (int j = 0; j < appObjects[i].length - 1; j++) {
                    temp.add(appObjects[i][j]);
                }
                indexController.index(temp);
            }*/
        } catch (AWTException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
