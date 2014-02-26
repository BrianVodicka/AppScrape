package AppScrape;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

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

    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://itunes.apple.com/us/app/puppy-doctor/id786185899?mt=8").get();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public AppObject(String s, int rank) {
        URL url = null;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            this.url = null;
            this.rank = rank;
            return;
        }
        this.url = url;
        setup();
    }

    private void setup(){
        Document doc = null;
        try {
            doc = Jsoup.connect(url.toString()).get();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        this.title = getTitle(doc);
        this.category = getCategory(doc);
        this.releaseDate = getRelease(doc);
        this.totalRatings = getTotalRatings(doc);
        this.avgRating = getAvgRating(doc);
    }

    private String getTitle(Document doc){
        Elements elem = doc.select("div[id=title]");
        Elements elm = elem.select("h1");
        checkSize(elm);
        Element data = elm.get(0);
        String temp = data.ownText();
        return temp;
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
        Elements elem = doc.select("span[class=rating-count]");
        checkSize(elem);
        Element e = elem.get(0);
        String temp = e.ownText();
        String[] test = temp.split(" ");
        return Integer.parseInt(test[0]); // TODO: throws number format exception
    }

    private int getAvgRating(Document doc){
        Elements parent = doc.select("div .customer-ratings");
        Elements elem = parent.select("div[class=rating]");
        Elements e = elem.select("span[class=rating-star ghost]");
        return 5 - e.size();
    }

    private void checkSize(Elements elem){
        if (elem.size() > 1) {
            System.out.println("Problem: more than 1 h1");
            for (int i = 0; i < elem.size(); i++) {
                Element data = elem.get(i);
                String temp = data.ownText();
                System.out.println(temp);
            }
        }
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

    public String getAllData(){
        return "~#title:" + this.getTitle() + " ~#rank:" + this.getRank() + " ~#category:" + this.getCategory() + " ~#releaseDate:" + this.getReleaseDate() + " ~#totalRatings:" + getTotalRatings() + " ~#avgRating:" + getAvgRating() + " ~#url:" + this.getURL();
    }

}
