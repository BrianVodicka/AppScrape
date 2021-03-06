package AppScrape;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 2/23/14
 * Time: 1:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class AppObject {
    private URL url;
    private int rank;
    private String title;
    private String category;
    private String releaseDate; // TODO: maybe change type to URL?
    private int totalRatings;
    private int avgRating;

    private int oneDayChange;
    private int twoDayChange;
    private int threeDayChange;
    private int fourDayChange;
    private int fiveDayChange;

    public AppObject(String s, int rank, String category) {
        URL url;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            this.url = null;
            this.rank = rank;
            return;
        }
        this.url = url;
        this.rank = rank;

        String[] parts = s.split("app/");
        String[] parts2 = parts[1].split("/"); // TODO: sometimes exception here???
        this.title = parts2[0];
        this.category = category;
        int temp = 18;
        /*String[] pieces = s.split("~");
        this.title = pieces[1].substring(6, pieces[1].length());*/
    }

    public AppObject(String s) {
        String[] pieces = s.split("~");
        this.title = pieces[1].substring(6, pieces[1].length());
        this.rank = Integer.valueOf(pieces[2].substring(6, pieces[2].length() - 1));
        // wait is category necessary for comparison??
        String m = pieces[3].substring(9, pieces[3].length());
        this.category = pieces[3].substring(9, pieces[3].length());
    }

    private void fetchFromWeb(){
        String[] agents = {
                "66.249.64.109 - crawl-66-249-64-109.googlebot.com",
                "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20120101 Firefox/29.0",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/29.0",
                "Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET ",
                "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)"
        };
        Random rand = new Random();
        Document doc = null;

        try {
            Thread.sleep(rand.nextInt(400) + 200);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            doc = Jsoup.connect(url.toString()).ignoreContentType(true).userAgent(agents[rand.nextInt(5)]).get();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings |
            this.title = "Error requesting URL";
            return;
        }
        this.title = getTitle(doc);
        this.category = getCategory(doc);
        this.releaseDate = getRelease(doc);
        this.totalRatings = getTotalRatings(doc);
        this.avgRating = getAvgRating(doc);
    }

    private String getTitle(Document doc){
        try {
        Elements elem = doc.select("div[id=title]");
        Elements elm = elem.select("h1");
        checkSize(elm);
        Element data = elm.get(0);
        String temp = data.ownText();
        return temp;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("URL IS: " + url);
            return "ERROR";
        }
    }

    private String getCategory(Document doc) {
        Elements elem = doc.select("li[class=genre]");
        Elements elm = elem.select("a");
        checkSize(elm);
        Element e = elm.get(0);
        String temp = e.ownText();
        return temp;
    }

    private String getRelease(Document doc) {
        Elements elem = doc.select("li[class=release-date]");
        checkSize(elem);
        Element e = elem.get(0);
        String temp = e.ownText();
        return temp;
    }

    private int getTotalRatings(Document doc){
        try {
            Elements elem = doc.select("span[class=rating-count]");
            checkSize(elem);
            Element e = elem.get(0);
            String temp = e.ownText();
            String[] test = temp.split(" ");
            return Integer.parseInt(test[0]); // TODO: throws number format exception
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    private int getAvgRating(Document doc){
        if (this.totalRatings != 0) {
            Elements parent = doc.select("div .customer-ratings");
            Elements elem = parent.select("div[class=rating]");
            Elements e = elem.select("span[class=rating-star ghost]");
            return 5 - e.size();
        } else {
            return -1;
        }
    }

    private void checkSize(Elements elem){
        if (elem.size() > 1) {
            //System.out.println("Problem: more than 1 h1");
            for (int i = 0; i < elem.size(); i++) {
                Element data = elem.get(i);
                String temp = data.ownText();
                //System.out.println(temp);
            }
        }
    }

    private JsonObject createJsonObject(){
        JsonObject object = new JsonObject();
        object.addProperty("NAME", this.title);
        object.addProperty("RANK", this.rank);
        object.addProperty("CATEGORY", this.category);
        object.addProperty("URL", String.valueOf(this.url));

        JsonObject changes = new JsonObject();
        changes.addProperty("1 day change", this.oneDayChange);
        changes.addProperty("2 day change", this.twoDayChange);
        changes.addProperty("3 day change", this.threeDayChange);
        changes.addProperty("4 day change", this.fourDayChange);
        changes.addProperty("5 day change", this.fiveDayChange);

        object.add("CHANGES", changes);

        return object;
    }



    public URL getURL(){
        return this.url;
    }

    public String getTitle(){
        return this.title;
    }

    public String getCategory(){
        return this.category;
    }

    public String getReleaseDate(){
        return this.releaseDate;
    }

    public int getTotalRatings(){
        return this.totalRatings;
    }

    public int getAvgRating(){
        return this.avgRating;
    }

    public int getRank(){
        return this.rank;
    }

    public String getBasicData(){
        return "~#title: " + this.getTitle() + " ~#rank:" + this.getRank() + " ~#category:" + this.getCategory();
    }

    public String getAllData(){
        return "~#title:" + this.getTitle() + " ~#rank:" + this.getRank() + " ~#category:" + this.getCategory() + " ~#releaseDate:" + this.getReleaseDate() + " ~#totalRatings:" + getTotalRatings() + " ~#avgRating:" + getAvgRating() + " ~#url:" + this.getURL();
    }

}
