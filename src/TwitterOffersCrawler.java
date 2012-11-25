//username: TartanWare
//password: twitter-offers-crawler

import java.net.*;
import java.io.*;
import com.google.gson.Gson;
import org.json.*;

public class TwitterOffersCrawler {
	public static final String TWITTER_QUERY =
		"http://search.twitter.com/search.json?q=" +
		"retweet%20win%20OR%20\"chance%20to%20win\"" +
		"%20OR%20\"chance%20at\"%20OR%20\"chance%20for\"%20OR%20" +
		"giveaway%20OR%20offer%20-please%20-help%20-RT%20-\"shout%20out\"" +
		"%20-\"is%20giving%20away\"&src=typd";
	
	public static void main(String[] args) throws Exception {
		URL twitterQuery = new URL(TWITTER_QUERY);
        URLConnection tweetConnection = twitterQuery.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                		tweetConnection.getInputStream()));
        String inputLine;
        String jsonString = "";
        
        while ((inputLine = in.readLine()) != null) 
            jsonString += inputLine;
        
        in.close();
        
        JSONObject json = new JSONObject(jsonString);  
        System.out.println(json);
	}
}
