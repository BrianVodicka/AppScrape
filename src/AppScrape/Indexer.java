package AppScrape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 2/24/14
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class Indexer {

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
        String timeStamp2 = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp2);
    }

    public Indexer(){

    }

    public static void index(int category, ArrayList<AppObject> list) throws FileNotFoundException, UnsupportedEncodingException {
        String timeStamp = categories[category] + " - " + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp);
        File dir = new File("C:\\AppDir");
        File targetFile = new File(dir, timeStamp);
        PrintWriter writer = new PrintWriter(targetFile, "UTF-8");
        for (AppObject target : list) {
            //System.out.println(target.getAllData());
            String m = target.getBasicData();
            String l = target.getCategory();
            writer.print(target.getBasicData() + "\n");
        }
        writer.close();
    }


}
