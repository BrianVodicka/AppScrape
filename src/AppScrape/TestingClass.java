package AppScrape;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 4/12/14
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestingClass {
    public static void main(String[] args) {
        String s = "~#title:Job Search ~#rank:1 ~#category:Business ~#releaseDate:Nov 08, 2013 ~#totalRatings:190 ~#avgRating:3 ~#url:https://itunes.apple.com/us/app/job-search/id309735670?mt=8";
        String[] split = s.split("~");
        for (int i = 0; i < split.length; i++) {
            System.out.println(i + " " + split[i]);
        }
    }
}
