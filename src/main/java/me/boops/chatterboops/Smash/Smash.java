package me.boops.chatterboops.Smash;

import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import me.boops.chatterboops.Config;

public class Smash {
	
	// Random
	Random rand = new Random();
	
	public Smash(Config conf) throws Exception {
		
		// Get Oauth Token
		String authToken = oauthToken(conf.getSmashName(), conf.getSmashPass());
		
		//Get the server IP
		String serverIP = getServer();
		
		// Create the login msg
		JSONObject main = new JSONObject();
		JSONObject params = new JSONObject();
		
		main.put("method", "joinChannel");
		
		params.put("channel", "sirboops");
		params.put("name", conf.getSmashName());
		params.put("token", authToken);
		params.put("hideBuffered", true);
		
		main.put("params", params);
		
		// Connect here
		// This needs a good socketIO lib
		
	}
	
	// Get a oauth token for chat
	private String oauthToken(String userName, String passWord) throws Exception {
		
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
		HttpPost post = new HttpPost("https://api.smashcast.tv/auth/token");
		
		JSONObject auth = new JSONObject();
		auth.put("login", userName);
		auth.put("pass", passWord);
		auth.put("app", "desktop");
		
		StringEntity stringEntity = new StringEntity(auth.toString());
		post.setEntity(stringEntity);
		post.setHeader("Content-type", "application/json");
		
		HttpResponse res = client.execute(post);
		
		String meta = new BasicResponseHandler().handleResponse(res);
		JSONObject json = new JSONObject(meta);
		
		return json.getString("authToken");
		
	}
	
	private String getServer() throws Exception {
		
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
		HttpGet get = new HttpGet("https://api.smashcast.tv/chat/servers");
		HttpResponse res = client.execute(get);
		
		String meta = new BasicResponseHandler().handleResponse(res);
		JSONArray json = new JSONArray(meta);
		
		String url = ("" + json.getJSONObject(rand.nextInt(json.length())).getString("server_ip"));
		
		return url;
		
	}
	
}
