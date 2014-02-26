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

    static String[][] urls;
    static AppObject[][] appObjects;
    static Indexer indexController;

    public static void main(String[] args){
        indexController = new Indexer();
        try {
            RobotController test = new RobotController();
            urls = test.begin("C:/iTunes/iTunes.exe");
            appObjects = new AppObject[urls.length][];
            for (int i = 0; i < urls.length; i++) {
                appObjects = new AppObject[urls.length][urls[i].length];
                int m = urls[i].length;
                for(int j = 0; j < urls[i].length - 1; j++) {
                    String[] temp = urls[i][j].split(" ");
                    appObjects[i][j] = new AppObject(temp[1], Integer.parseInt(temp[0]));
                }
            }

            for (int i = 0; i < appObjects.length; i++) {
                ArrayList<AppObject> temp = new ArrayList<>();
                for (int j = 0; j < appObjects[i].length - 1; j++) {
                    temp.add(appObjects[i][j]);
                }
                indexController.index(temp);
            }
        } catch (AWTException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
