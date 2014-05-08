package AppScrape;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * Defines Mouse Movements in iTunes Client
 */
public class RobotController extends  Robot{

    Robot robot;
    Dimension screenSize;
    Toolkit toolkit;
    Clipboard clipBoard;

    final int LEFT_CLICK = InputEvent.BUTTON1_DOWN_MASK;
    final int RIGHT_CLICK = InputEvent.BUTTON3_DOWN_MASK;

    ArrayList<ArrayList<String>> urls = new ArrayList<>();

    public RobotController() throws AWTException {
        robot = new Robot();
        toolkit = Toolkit.getDefaultToolkit();
        clipBoard = toolkit.getSystemClipboard();
    }

    public ArrayList<ArrayList<String>> begin(String target) throws InterruptedException, IOException, UnsupportedFlavorException {
        new Indexer(); // updates the current date
        openItunes(target);
        getBearings();
        getIntoAppStore();
        moveIntoCategory();
        return urls;
    }

    private void openItunes(String target) {
        try{
            Process pro = Runtime.getRuntime().exec(target);
            Thread.sleep(4000);
            while(!pageLoaded()) {
                Thread.sleep(1000);
            }
            Thread.sleep(2000);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void reopenItunes() throws InterruptedException {
        openItunes("C:/iTunes/iTunes.exe");
        getBearings();
        getIntoAppStore();
    }

    private void getBearings() throws InterruptedException {
        // TODO: solve update problem here

        // maximize window
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.keyPress(KeyEvent.VK_X);
        robot.keyRelease(KeyEvent.VK_X);

        // get window dimensions
        screenSize = toolkit.getScreenSize();

    }

    private void getIntoAppStore() throws InterruptedException {

        int width = screenSize.width;

        // Click on "iTunes Store" link
        robot.mouseMove(width - 50, 100);
        robot.mousePress(LEFT_CLICK);
        robot.mouseRelease(LEFT_CLICK);
        do {
            Thread.sleep(500);
        } while(!pageLoaded());
        Thread.sleep(300);

        // Click on "App Store" link
        robot.mouseMove(width/2, 100);
        robot.mousePress(LEFT_CLICK);
        robot.mouseRelease(LEFT_CLICK);
        do {
            Thread.sleep(500);
        } while(!pageLoaded());
        Thread.sleep(2000);
    }

    // assumes page is front page of AppStore
    private void moveIntoCategory() throws InterruptedException, IOException, UnsupportedFlavorException {
        Thread.sleep(2000); // TODO: maybe delete this sleep??
        int categories_y = 525;
        int categories_x = screenSize.width / 2 + 625;

        robot.mouseMove(categories_x, categories_y);

        for (int i = 1; i < 24; i++) { // 24
            long startTime = System.nanoTime();
            // Click into "Categories" - opens drop down menu
            robot.mouseMove(categories_x, categories_y);
            robot.mousePress(LEFT_CLICK);
            robot.mouseRelease(LEFT_CLICK);
            Thread.sleep(800);

            // move into specific category page
            for (int j = 0; j < i; j++) {
                robot.keyPress(KeyEvent.VK_DOWN);
                Thread.sleep(10);
                robot.keyRelease(KeyEvent.VK_DOWN);
            }
            robot.keyPress(KeyEvent.VK_ENTER);
            Thread.sleep(10);
            robot.keyRelease(KeyEvent.VK_ENTER);
            do {
                Thread.sleep(500);
            } while(!pageLoaded());
            Thread.sleep(300);

            // get the apps for this category
            Point position = null;
            position = getPaidApps(i, position);
            getPaidApps(i, position);

            Runtime.getRuntime().exec("taskkill /f /im itunes.exe");
            Thread.sleep(1000);
            reopenItunes();

            // for development: see how long it took to index apps
            long endTime = System.nanoTime();
            long elapsed = (endTime - startTime) / 1000000000;
            System.out.println("Time was: " + elapsed);
        }
    }

    private Point getPaidApps(int counter, Point link_pos) throws InterruptedException, IOException, UnsupportedFlavorException {
        // move into free apps page
        if (link_pos != null) {
            robot.mouseMove(link_pos.x, link_pos.y);
            robot.mousePress(LEFT_CLICK);
            robot.mouseRelease(LEFT_CLICK);
            do {
                Thread.sleep(500);
            } while(!pageLoaded());
            Thread.sleep(300);

            getTopApps(counter, link_pos, 1);
            return null;
        }

        int categories_x = 1526; // TODO: make this variable adjust dynamically to screen size
        Point p = findTopAppLink(categories_x);
        Thread.sleep(1000);
        if (p == null)
            return null;

        // move into paid apps page
        robot.mouseMove(p.x, p.y);
        robot.mousePress(LEFT_CLICK);
        robot.mouseRelease(LEFT_CLICK);
        Thread.sleep(7000);
        getTopApps(counter, link_pos, 0);
        return p;
    }

    private void getTopApps(int counter, Point link_pos, int free) throws IOException, UnsupportedFlavorException, InterruptedException {
        // click on free apps link
        if (link_pos != null) {
            robot.mouseMove(screenSize.width / 2, 140);
            robot.mousePress(LEFT_CLICK);
            robot.mouseRelease(LEFT_CLICK);
            do {
                Thread.sleep(500);
            } while(!pageLoaded());
            Thread.sleep(1000);

        }

        // get names and rankings of all apps on this page
        int x_offset = screenSize.width / 2 - 720;
        int y_offset = 235; // TODO: why is y_offset 235 here, but 204 and 225 down there??
        robot.mouseMove(x_offset, y_offset);
        ArrayList<String> currentUrls = new ArrayList<>();
        currentUrls.add(String.valueOf(counter));
        int count = 1;
        // TODO: improve this loop structure
        // TODO: account for last 8 apps
        // TODO: make adjustments for different screen sizes
        while (count < 192) { // there are 200 apps in total; get each // 192
            for (int i = 0; i < 4; i++) { // move down a row of apps; 4 rows fits on my screen // 4
                y_offset = y_offset + 204 * i;
                for (int j = 0; j < 12; j++) { // move along each row of apps; 12 apps in row // 12
                    boolean valid = checkBackground(x_offset + 130 * j, y_offset);
                    if (!valid) {
                        y_offset = repositionMouse(x_offset + 130 * j, y_offset);
                    }
                    // get the name of the app and its rank
                    quickCopy(x_offset + 130 * j, y_offset);
                    String result = getClipboard();
                    // TODO: determine why count variable is needed
                    if(!currentUrls.contains(result))
                        currentUrls.add(count + " " + result);
                    else { // if it does not copy in time, recopy
                        j--;
                        continue;
                    }
                    Thread.sleep(50);
                    count++;
                }
                // TODO: find why y_offset is being set here?
                y_offset = 225;
            }

            int m = 0;
            while (m < 21) {
                robot.keyPress(KeyEvent.VK_DOWN);
                robot.keyRelease(KeyEvent.VK_DOWN);
                m++;
            }
            Thread.sleep(850);
        }

        // start new thread to index these apps
        Indexer indexer = new Indexer((2 * counter) - 2 + free, currentUrls);
        Thread t = new Thread(indexer);
        t.start();

        System.out.println("SIZE IS:" + currentUrls.size() + "List is: " + counter);
        //urls.add(currentUrls);
        //globalCounter++;

        // move back one page when done indexing
        robot.mouseMove(20, 100);
        Thread.sleep(20);
        robot.mousePress(LEFT_CLICK);
        robot.mouseRelease(LEFT_CLICK);
        do {
            Thread.sleep(500);
        } while(!pageLoaded());
        Thread.sleep(300);
    }

    private Point findTopAppLink(int x_pos) throws InterruptedException {
        int y_pos = 675; // 675
        boolean found = false;
        // TODO: make memory usage better by only capturing part of the screen
        // TODO: fix this loop structure
        while (!found) {
            for (; y_pos < 1000; y_pos += 3) { // 1000
                robot.mouseMove(x_pos, y_pos);
                Thread.sleep(10);
                if (checkBackground(x_pos, y_pos))
                    found = checkAppValid(x_pos, y_pos - 26);
                if (found)
                    return new Point(x_pos, y_pos - 26);
            }
            Thread.sleep(600);
            x_pos += 3;
        }
        // TODO: throw app not found exception
        return null;
    }

    private String getClipboard() throws InterruptedException, IOException, UnsupportedFlavorException {
        String result = null;
        boolean test = false;
        while (!test) {
            try {
                result = (String) clipBoard.getData(DataFlavor.stringFlavor);
                test = true;
            } catch (IllegalArgumentException | IllegalStateException | UnsupportedFlavorException e) {
                Thread.sleep(200);
            }
        }
        return result;
    }

    // error here: fix case when it right clicks but no message box appears
    private void copyToClipboard(int x, int y) throws InterruptedException {
        robot.mouseMove(x, y);
        robot.mousePress(RIGHT_CLICK); // right click on app
        robot.mouseRelease(RIGHT_CLICK);
        Thread.sleep(400);
        Color temp = robot.getPixelColor(x + 35, y + 35);
        //System.out.println("COLORS ARE: " + temp.getRed() + " " + temp.getGreen() + " " + temp.getBlue());
        if (temp.getRed() == 240 && temp.getGreen() == 206 && temp.getBlue() == 135) {
            robot.mouseMove(x + 35, y + 35);
            robot.mousePress(LEFT_CLICK); // copy url
            robot.mouseRelease(LEFT_CLICK);
            Thread.sleep(300);
        }
    }

    private void quickCopy(int x, int y) throws InterruptedException {
        robot.mouseMove(x, y);
        robot.mousePress(RIGHT_CLICK); // right click on app
        robot.mouseRelease(RIGHT_CLICK);
        Thread.sleep(550);
        robot.mouseMove(x + 35, y + 35);
        robot.mousePress(LEFT_CLICK); // copy url
        robot.mouseRelease(LEFT_CLICK);
        Thread.sleep(300);
    }

    private boolean checkBackground(int x, int y){
        //int backgroundColors[] = {236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248};
        int color = robot.getPixelColor(x, y).getRed();
        if (color >= 236 && color <=248) {
            Color col = robot.getPixelColor(x,y);
            if (col.getRed() == col.getBlue() && col.getBlue() == col.getGreen()){
                return false;
            }
        }
        return true;
    }

    private boolean checkAppValid(int x, int y) {
        //int backgroundColors[] = {236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248};
        try {
        if (!checkBackground(x, y))
            return false;
        copyToClipboard(x, y);
        String temp = getClipboard();
        String substring = "https://itunes.apple.com/WebObjects/MZStore.woa/wa/viewTop";
        if (temp.contains(substring)) {
            return true;
        }
        } catch (InterruptedException | IOException | UnsupportedFlavorException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int repositionMouse(int x, int y){
        int plus = 0;
        while(!checkBackground(x, y + plus)) {
            plus += 2;
        }

        return y + plus;
    }

    private boolean pageLoaded(){
        int x = screenSize.width / 2;

        // perform three color checks on top to see if apple logo is there yet
        int color = robot.getPixelColor(x, 34).getRed();
        if (!(color > 87 && color < 95))
            return false;
        color = robot.getPixelColor(x + 5, 34).getRed();
        if (!(color > 87 && color < 95))
            return false;
        color = robot.getPixelColor(x - 5, 34).getRed();
        if (!(color > 87 && color < 95))
            return false;
        return true;
    }

    // TODO: use this method
    private boolean checkRightClickValid(int x, int y){
        Color color = robot.getPixelColor(x + 20, y + 20);
        Color grey = new Color(240, 240, 240);
        if (color.equals(grey))
            return true;
        return false;
    }
}
