package AppScrape;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

    int globalCounter = 0;
    ArrayList<ArrayList<String>> urls = new ArrayList<>();

    public RobotController() throws AWTException {
        robot = new Robot();
        toolkit = Toolkit.getDefaultToolkit();
        clipBoard = toolkit.getSystemClipboard();
    }

    public ArrayList<ArrayList<String>> begin(String target) throws InterruptedException, IOException, UnsupportedFlavorException {
        //openItunes(target);
        getBearings();
        //getIntoAppStore();
        moveIntoCategory();
        return urls;
    }

    private void openItunes(String target) {
        try{
            Runtime run = Runtime.getRuntime();
            Process pro = run.exec(target);
            Thread.sleep(10000); // wait 10 seconds for program to load
        }
        catch(Exception ex){
            System.out.println(ex);
        }
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
        Thread.sleep(6000);

        // Click on "App Store" link
        robot.mouseMove(width/2, 100);
        robot.mousePress(LEFT_CLICK);
        robot.mouseRelease(LEFT_CLICK);
        Thread.sleep(6000);

    }

    // assumes page is front page of AppStore
    private void moveIntoCategory() throws InterruptedException, IOException, UnsupportedFlavorException {
        Thread.sleep(2000);
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

            for (int j = 0; j < i; j++) {
                robot.keyPress(KeyEvent.VK_DOWN);
                Thread.sleep(10);
                robot.keyRelease(KeyEvent.VK_DOWN);
            }
            robot.keyPress(KeyEvent.VK_ENTER);
            Thread.sleep(10);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(6000);

            Point position = null;
            position = getPaidApps(i, position);
            getPaidApps(i, position);

            robot.mouseMove(20, 100);
            Thread.sleep(20);
            robot.mousePress(LEFT_CLICK);
            robot.mouseRelease(LEFT_CLICK);
            Thread.sleep(3000);
            long endTime = System.nanoTime();
            long elapsed = (endTime - startTime) / 1000000000;
            System.out.println("Time wass: " + elapsed);
        }
    }

    private Point getPaidApps(int counter, Point link_pos) throws InterruptedException, IOException, UnsupportedFlavorException {
        if (link_pos != null) {
            robot.mouseMove(link_pos.x, link_pos.y);
            robot.mousePress(LEFT_CLICK);
            robot.mouseRelease(LEFT_CLICK);
            Thread.sleep(7000);
            getTopApps(counter, link_pos, 1);
            return null;
        }

        // set up variables for loop to find link
        int categories_x = 1526;

        Point p = findTopAppLink(categories_x);
        Thread.sleep(1000);
        if (p == null)
            return null;

        robot.mouseMove(p.x, p.y);
        robot.mousePress(LEFT_CLICK);
        robot.mouseRelease(LEFT_CLICK);
        Thread.sleep(7000);
        getTopApps(counter, link_pos, 0);
        return p;
    }

    private void getTopApps(int counter, Point link_pos, int free) throws IOException, UnsupportedFlavorException, InterruptedException {
        if (link_pos != null) {
            robot.mouseMove(screenSize.width / 2, 140);
            robot.mousePress(LEFT_CLICK);
            robot.mouseRelease(LEFT_CLICK);
            Thread.sleep(6000);
        }
        int x_offset = screenSize.width / 2 - 720;
        int y_offset = 235;
        robot.mouseMove(x_offset, y_offset);
        ArrayList<String> currentUrls = new ArrayList<>();
        currentUrls.add(String.valueOf(counter));
        int count = 1;
        // TODO: improve this loop structure
        while (count < 192) { // 192
            for (int i = 0; i < 4; i++) { // 4
                y_offset = y_offset + 204 * i;
                for (int j = 0; j < 12; j++) { // 12
                    boolean valid = checkBackground(x_offset + 130 * j, y_offset);
                    if (!valid) {
                        y_offset = repositionMouse(x_offset + 130 * j, y_offset);
                    }
                    quickCopy(x_offset + 130 * j, y_offset);
                    String result = getClipboard();
                    if(!currentUrls.contains(result))
                        currentUrls.add(count + " " + result);
                    else {
                        System.out.println("DID NOT ADD: " + result);
                        j--;
                        continue;
                    }
                    Thread.sleep(50);
                    count++;
                }
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
        urls.add(currentUrls);
        globalCounter++;

        robot.mouseMove(20, 100);
        Thread.sleep(20);
        robot.mousePress(LEFT_CLICK);
        robot.mouseRelease(LEFT_CLICK);
        Thread.sleep(7000);
    }

    private Point findTopAppLink(int x_pos) throws InterruptedException {
        int y_pos = 675; // 675
        boolean found = false;
        // TODO: make memory usage better by only capturing part of the screen
        BufferedImage screen = robot.createScreenCapture(new Rectangle(screenSize));
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
        /*for (int i = 2; i < 125; i += 2) {
            if (checkGrey(screen, y_pos - i))
                return y_pos - i - 26;
        }*/
        // TODO: throw app not found exception
        return null;
    }

    private boolean testColor(BufferedImage screen, int y_pos, int x_pos) {
        //robot.mouseMove(x_pos, y_pos);
        ///int temp = screen.getRGB(x_pos, y_pos);
        //Color c = new Color(temp);
        if (checkAppValid(x_pos, y_pos)) {
            robot.mouseMove(x_pos, y_pos);
            /*int tempb = screen.getRGB(x_pos, y_pos - 5);
            int tempe = screen.getRGB(x_pos, y_pos + 5);
            int tempc = screen.getRGB(x_pos, y_pos - 10);
            int tempd = screen.getRGB(x_pos, y_pos + 10);
            if (!testGrey(new Color(tempb)) && !testGrey(new Color(tempc)) && !testGrey(new Color(tempd)) && !testGrey(new Color(tempe)))
                return true;*/
            return true;
        }
        return false;
    }

    // currently unused
    private boolean checkGrey(BufferedImage screen, int test_position) {
        int x_pos = screenSize.width / 2 + 569;
        int y_pos = test_position;
        int temp = screen.getRGB(x_pos, y_pos);
        Color c = new Color(temp);
        if (testGrey(c)) {
            int tempb = screen.getRGB(x_pos - 5, y_pos);
            int tempe = screen.getRGB(x_pos + 5, y_pos);
            int tempc = screen.getRGB(x_pos - 10, y_pos);
            int tempd = screen.getRGB(x_pos + 10, y_pos);
            if (testGrey(new Color(tempb)) && testGrey(new Color(tempc)) && testGrey(new Color(tempd)) && testGrey(new Color(tempe)))
                return true;
        }
        return false;
    }

    // currently unused
    private boolean testGrey(Color c) {
        return c.getRed() == c.getGreen() && c.getRed() == c.getBlue();
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
        BufferedImage screen = robot.createScreenCapture(new Rectangle(screenSize));
        Color temp = new Color(screen.getRGB(x + 35, y + 35));
        System.out.println("COLORS ARE: " + temp.getRed() + " " + temp.getGreen() + " " + temp.getBlue());
        if (temp.getRed() == 240 && temp.getGreen() == 206 && temp.getBlue() == 135) {
            Thread.sleep(1000);
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
        System.out.println(temp);
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

}
