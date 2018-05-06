
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        /*RSSFeedParser parser = new RSSFeedParser(
                "http://rss.nytimes.com/services/xml/rss/nyt/World.xml");
        Feed feed = parser.readFeed();
        System.out.println(feed);
        for (FeedMessage message : feed.getMessages()) {
            System.out.println(message);

        }




*/
        // Open the file
        FileInputStream fstream = new FileInputStream("rssFeed.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

//Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
            // Print the content on the console
            System.out.println (strLine);
            RSSFeedParser parser = new RSSFeedParser(strLine);
            Feed feed = parser.readFeed();
            System.out.println(feed);
            for (FeedMessage message : feed.getMessages()) {
                System.out.println(message);
		Thread.sleep(100);

            }
        }

//Close the input stream
        br.close();
    }
}
