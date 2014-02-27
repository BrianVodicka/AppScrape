package AppScrape;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 2/21/14
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    static ArrayList<ArrayList<String>> urls;

    public static void main(String[] args){
        try {
            RobotController test = new RobotController();
            urls = test.begin("C:/iTunes/iTunes.exe");
            for (int i = 0; i < urls.size(); i++) {
                ArrayList<String> temp = urls.get(i);
                ArrayList<AppObject> objects = new ArrayList<>();
                for(int j = 0; j < temp.size(); j++) {
                    String[] target = temp.get(j).split(" ");
                    System.out.println(target[0] + " " + Integer.parseInt(target[0]) + " " + target[1]);
                    objects.add(new AppObject(target[1], Integer.parseInt(target[0])));
                }
                Indexer.index(objects);
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
