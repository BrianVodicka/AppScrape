package AppScrape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 4/12/14
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestingClass {
    public static void main(String[] args) throws FileNotFoundException {
        final String[] categories = {
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
        File dir = new File("C:\\AppDir\\tracking");
        for (int i = 0; i < categories.length; i++){
            File temp = new File(dir, categories[i]);
            PrintWriter writer = new PrintWriter(temp);
            writer.print("");
            writer.close();
        }

        /*String s = "~#title:Job Search ~#rank:1 ~#category:Business ~#releaseDate:Nov 08, 2013 ~#totalRatings:190 ~#avgRating:3 ~#url:https://itunes.apple.com/us/app/job-search/id309735670?mt=8";
        String[] split = s.split("~");
        for (int i = 0; i < split.length; i++) {
            System.out.println(i + " " + split[i]);
        }*/
    }
}
