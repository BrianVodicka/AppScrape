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

    public static void main(String[] args) throws AWTException, InterruptedException {
        Thread.sleep(1000);
        Robot robot = new Robot();
        int m = 0;
        while (m < 5) {
            robot.keyPress(KeyEvent.VK_DOWN);
            Thread.sleep(10);
            robot.keyRelease(KeyEvent.VK_DOWN);
            m++;
        }
    }

    Robot robot;
    Dimension screenSize;
    Toolkit toolkit;
    Clipboard clipBoard;

    final int LEFT_CLICK = InputEvent.BUTTON1_DOWN_MASK;
    final int RIGHT_CLICK = InputEvent.BUTTON3_DOWN_MASK;

    int globalCounter = 0;
    String[][] urls;

    String[] categories = {
            "Books",
            "Business",
            "Catalogs",
            "Education",
            "Entertainment",
            "Finance",
            "Food & Drink",
            "Games",
            "Health & Fitness",
            "Kids",
            "Lifestyle",
            "Medical",
            "Music",
            "Navigation",
            "News",
            "Newsstand",
            "Photo & Video",
            "Productivity",
            "Reference",
            "Social Networking",
            "Sports",
            "Travel",
            "Utilities",
            "Weather"
    };

    public RobotController() throws AWTException {
        robot = new Robot();
        toolkit = Toolkit.getDefaultToolkit();
        clipBoard = toolkit.getSystemClipboard();
    }

    public String[][] begin(String target) throws InterruptedException, IOException, UnsupportedFlavorException {
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
        // start at 2 bc min move down is 30
        for (int i = 1; i < 2; i++) { // 24
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

            boolean free_apps = false;
            getPaidApps(i, free_apps);
            free_apps = true;
            getPaidApps(i, free_apps);

            robot.mouseMove(20, 100);
            Thread.sleep(20);
            robot.mousePress(LEFT_CLICK);
            robot.mouseRelease(LEFT_CLICK);
            Thread.sleep(3000);
        }
    }

    private void getPaidApps(int counter, boolean free) throws InterruptedException, IOException, UnsupportedFlavorException {

        // scrolls screen down to find free apps link
        if (free) {
            int m = 0;
            while (m < 15) {
                robot.keyPress(KeyEvent.VK_DOWN);
                Thread.sleep(10);
                robot.keyRelease(KeyEvent.VK_DOWN);
                m++;
            }
            Thread.sleep(2500);
        }

        // set up variables for loop to find link
        int categories_x = screenSize.width / 2 + 565;
        int add = 0;
        boolean found = false;

        while (!found){
            int y = findTopAppLink(categories_x);
            Thread.sleep(1000);
            if (y == -1)
                return;
            copyToClipboard(categories_x, y);
            String temp = getClipboard();
            System.out.println(temp);
            String substring = "https://itunes.apple.com/WebObjects/MZStore.woa/wa";
            if (temp.contains(substring)) {
                robot.mouseMove(categories_x, y);
                robot.mousePress(LEFT_CLICK);
                robot.mouseRelease(LEFT_CLICK);
                Thread.sleep(7000);
                getTopApps(counter); // finds paid apps
                found = true;
            } else {
                add += 3;
                if (add > 100) {
                    return;
                }
                categories_x += add;
            }
        }
    }

    private void getTopApps(int counter) throws IOException, UnsupportedFlavorException, InterruptedException {
        int x_offset = screenSize.width / 2 - 720;
        int y_offset = 225;
        robot.mouseMove(x_offset, y_offset);
        ArrayList<String> currentUrls = new ArrayList<>();
        int count = 1;
        while (count < 24) { // 192
            for (int i = 0; i < 4; i++) { // 4
                y_offset = y_offset + 196 * i;
                for (int j = 0; j < 12; j++) { // 12
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
            while (m < 20) {
                robot.keyPress(KeyEvent.VK_DOWN);
                robot.keyRelease(KeyEvent.VK_DOWN);
                m++;
            }
            Thread.sleep(850);
        }
        System.out.println("SIZE IS:" + currentUrls.size() + "List is: " + counter);
        urls = new String[globalCounter + 1][currentUrls.size() + 1];
        int m = currentUrls.size();
        for(int i = 0; i < m; i++) {
            urls[globalCounter][i] = currentUrls.get(i);
        }
        globalCounter++;

        robot.mouseMove(20, 100);
        Thread.sleep(20);
        robot.mousePress(LEFT_CLICK);
        robot.mouseRelease(LEFT_CLICK);
        Thread.sleep(20);
    }

    private int findTopAppLink(int x_pos) {
        int y_pos = 675;

        // TODO: make memory usage better by only capturing part of the screen
        BufferedImage screen = robot.createScreenCapture(new Rectangle(screenSize));
        for (; y_pos < 1000; y_pos += 3) {
            if (testColor(screen, y_pos, x_pos))
                break;
        }
        for (int i = 2; i < 150; i += 2) {
            if (checkGrey(screen, y_pos - i))
                return y_pos - i - 26;
        }
        return -1;
    }

    private boolean testColor(BufferedImage screen, int y_pos, int x_pos) {
        int temp = screen.getRGB(x_pos, y_pos);
        Color c = new Color(temp);
        if (!testGrey(c)) {
            robot.mouseMove(x_pos, y_pos);
            int tempb = screen.getRGB(x_pos, y_pos - 5);
            int tempe = screen.getRGB(x_pos, y_pos + 5);
            int tempc = screen.getRGB(x_pos, y_pos - 10);
            int tempd = screen.getRGB(x_pos, y_pos + 10);
            if (!testGrey(new Color(tempb)) && !testGrey(new Color(tempc)) && !testGrey(new Color(tempd)) && !testGrey(new Color(tempe)))
                return true;
        }
        return false;
    }

    private boolean checkGrey(BufferedImage screen, int test_position) {
        int x_pos = screenSize.width / 2 + 564;
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

    private boolean testGrey(Color c) {
        if (c.getRed() == c.getGreen() && c.getRed() == c.getBlue())
            return true;
        return false;
    }

    private String getClipboard() throws InterruptedException, IOException, UnsupportedFlavorException {
        String result = null;
        boolean test = false;
        while (!test) {
            try {
                result = (String) clipBoard.getData(DataFlavor.stringFlavor);
                test = true;
            } catch (IllegalArgumentException e) {
                Thread.sleep(200);
            } catch (UnsupportedFlavorException e) {
                Thread.sleep(200);
            } catch (IllegalStateException e) {
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
            Thread.sleep(15000);
            robot.mouseMove(x + 35, y + 35);
            robot.mousePress(LEFT_CLICK); // copy url
            robot.mouseRelease(LEFT_CLICK);
            Thread.sleep(1000);
        }
    }

    private void quickCopy(int x, int y) throws InterruptedException {
        robot.mouseMove(x, y);
        robot.mousePress(RIGHT_CLICK); // right click on app
        robot.mouseRelease(RIGHT_CLICK);
        Thread.sleep(400);
        robot.mouseMove(x + 35, y + 35);
        robot.mousePress(LEFT_CLICK); // copy url
        robot.mouseRelease(LEFT_CLICK);
        Thread.sleep(1000);

    }

}
