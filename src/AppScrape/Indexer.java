package AppScrape;

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

    public static void main(String[] args){
        String timeStamp2 = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp2);
    }

    public Indexer(){

    }

    public static void index(ArrayList<AppObject> list) throws FileNotFoundException, UnsupportedEncodingException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        PrintWriter writer = new PrintWriter(timeStamp +".txt", "UTF-8");
        for (AppObject target : list) {
            System.out.println(target.getAllData());
            writer.print(target.getAllData() + "\n");
        }
        writer.close();
    }


}
