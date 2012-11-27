//username: TartanWare
//password: twitter-offers-crawler

import java.net.*;
import java.io.*;
import org.json.*;
import twitter4j.*;
import twitter4j.auth.AccessToken;

public class TwitterOffersCrawler {
	//Twitter API keys
	public static final String CONSUMER_KEY = "czjE9Ghhndoe1zeMxGK4rQ";
	public static final String CONSUMER_SECRET = "8bddKOyv6MpxkpZhtszrEHjimJf5nl5bmOm5no";
	public static final String ACCESS_TOKEN = "966996302-XQaq6U32oMXrjHPHPYcAOQIXwMAdV1vtrseWnwCf";
	public static final String ACCESS_SECRET = "lqLfM5KJcjJWcBhydYGmyqkvsl4sTAzTIiAYweac";
	
	//API response keys
	public static final String RESULTS_KEY = "results";
	public static final String TWEET_CONTENT_KEY = "text";
	public static final String USERNAME_KEY = "from_user";
	public static final String TWEET_ID = "id";
	
	//Twitter custom search query
	public static final String TWITTER_QUERY =
		"http://search.twitter.com/search.json?q=" +
		"retweet%20win%20OR%20\"chance%20to%20win\"" + 							//search query
		"%20OR%20\"chance%20at\"%20OR%20\"chance%20for\"%20OR%20" +
		"giveaway%20OR%20offer%20-please%20-help%20-RT%20-\"shout%20out\"" +
		"%20-\"is%20giving%20away\"" +
		"&rpp=100" +															//results per page
		"&result_type=mixed" +													//result type
		"&src=typd";
	
	public static void main(String[] args) throws Exception {
		//establish Twitter API connection
		TwitterFactory factory = new TwitterFactory();
		Twitter twitter = factory.getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		AccessToken accessToken = new AccessToken(ACCESS_TOKEN, ACCESS_SECRET);
		twitter.setOAuthAccessToken(accessToken);

		//establish http request for search query
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
        
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray tweets = (JSONArray) jsonObject.get(RESULTS_KEY);
        
        for(int i = 1; i < tweets.length(); i++) {
            JSONObject tweet = tweets.getJSONObject(i);
            
            //if the tweet is a reply, then ignore it 
            if(!((String)tweet.get("to_user_id_str")).equals("0")) {
            	continue;
            }
            
            String text = (String) tweet.get(TWEET_CONTENT_KEY);
            String username = (String) tweet.get(USERNAME_KEY);
            Long tweetId = (Long) tweet.get(TWEET_ID);
            
            System.out.println("Tweet: " + text);
            System.out.println("Tweet ID: " + tweetId);
            System.out.println("Username: " + username);
            try {
                twitter.retweetStatus(tweetId);
                twitter.createFriendship(username);
                System.out.println("SUCCESS!\n");
                Thread.sleep(60000);
            }
            catch(Exception e) {
            	System.err.println(e);
				System.out.println("FAILEDFAILEDFAILEDFAILEDFAILED\n");
            }
        }
        
	}
}
