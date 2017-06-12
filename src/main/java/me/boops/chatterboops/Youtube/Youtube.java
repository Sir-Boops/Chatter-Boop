package me.boops.chatterboops.Youtube;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import me.boops.chatterboops.Config;
import me.boops.chatterboops.parsers.YoutubeParser;

public class Youtube {
	
	private static String authKey;
	private List<String> parsedMessageIDs = new ArrayList<String>();
	private static Config conf;
	
	
	public Youtube(Config confInit) throws Exception {
		
		Youtube.conf = confInit;
		
		// Get the auth key and save it
		authKey = getOauthToken();
		
		
		// Auto refresh token
		new Thread(new Runnable(){
			
			public void run(){
				
				try {
					
					keepTokenAlive();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
		int sleepTime = 2000;
		
		// Loop for messages
		while(true){
			
			Thread.sleep(sleepTime);
			sleepTime = readLiveChat();
			
		}
		
	}
	
	// Get an oauth token from the refresh token
	private String getOauthToken() throws Exception {
		
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
		HttpPost post = new HttpPost("https://www.googleapis.com/oauth2/v4/token");
		
		// Build the post request
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("refresh_token", conf.getYoutubeToken()));
		urlParameters.add(new BasicNameValuePair("client_id", conf.getYoutubeClientID()));
		urlParameters.add(new BasicNameValuePair("client_secret", conf.getYoutubeClientSec()));
		urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
		
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		
		HttpResponse res = client.execute(post);
		JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
		
		return meta.getString("access_token");
		
	}
	
	private void keepTokenAlive() throws Exception{
		
		while (true){
			
			Thread.sleep(3500 * 1000);
			authKey = getOauthToken();
			
		}
		
	}
	
	// Process all messages
	private int readLiveChat() throws Exception {
		
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
		HttpGet get = new HttpGet("https://www.googleapis.com/youtube/v3/liveChat/messages?part=snippet&maxResults=2000&liveChatId=" + conf.getYoutubeChatID());
		get.addHeader("Authorization", "Bearer " + authKey);
		
		HttpResponse res = client.execute(get);
		JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
		
		JSONArray messages = meta.getJSONArray("items");
		List<String> intParsedMessageIDs = new ArrayList<String>();
		
		// Parse messages
		for (int i=0; i<messages.length(); i++){
			
			// Check if this is the first run
			if(parsedMessageIDs.size() <= 0){
				
				// First run so parse nonthing
				intParsedMessageIDs.add(messages.getJSONObject(i).getString("id"));
				
			} else {
				
				// This has already ran so only parse new messages
				// And dump old messages
				// And ignore myself
				
				if(!parsedMessageIDs.contains(messages.getJSONObject(i).getString("id")) && 
						!messages.getJSONObject(i).getJSONObject("snippet").getString("authorChannelId").equals(conf.getYoutubeBotChannelID())){
					
					new YoutubeParser(messages.getJSONObject(i));
					
				}
			}
			
		}
		
		if(parsedMessageIDs.size() <= 0){
			parsedMessageIDs = intParsedMessageIDs;
		}
		
		parsedMessageIDs = getAllMessageIDs(messages);
		
		return meta.getInt("pollingIntervalMillis");
		
	}
	
	// Get all messageIDs in the responce
	private List<String> getAllMessageIDs(JSONArray msg){
		
		List<String> ans = new ArrayList<String>();
		
		for (int i=0; i<msg.length(); i++){
			
			ans.add(msg.getJSONObject(i).getString("id"));
			
		}
		
		return ans;
		
	}
	
	public static String getUserName(String userUUID) throws Exception {
		
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
		HttpGet get = new HttpGet("https://www.googleapis.com/youtube/v3/channels?part=snippet&id=" + userUUID);
		get.addHeader("Authorization", "Bearer " + authKey);
		
		HttpResponse res = client.execute(get);
		JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
		
		return meta.getJSONArray("items").getJSONObject(0).getJSONObject("snippet").getString("title");
		
	}
	
	public static void sendMSG(String msg) throws Exception{
		
		JSONObject body = new JSONObject();
		JSONObject snippet = new JSONObject();
		JSONObject jsonMSG = new JSONObject();
		
		jsonMSG.put("messageText", msg);
		snippet.put("liveChatId", conf.getYoutubeChatID());
		snippet.put("type", "textMessageEvent");
		snippet.put("textMessageDetails", jsonMSG);
		body.put("snippet", snippet);
		
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
		HttpPost post = new HttpPost("https://www.googleapis.com/youtube/v3/liveChat/messages?part=snippet&fields=snippet");
		post.addHeader("Authorization", "Bearer " + authKey);
		post.setHeader("Content-type", "application/json");
		post.setEntity(new StringEntity(body.toString()));
		
		client.execute(post);
		
	}
	
	
}
