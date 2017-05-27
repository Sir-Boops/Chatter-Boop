package me.boops.chatterboops.Smash;

import java.net.URI;
import java.util.Random;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONObject;

import me.boops.chatterboops.Config;

public class Smash {
	
	// Random
	Random rand = new Random();
	
	private Session session;
	
	public Smash(Config conf) throws Exception {
		
		// Get Oauth Token
		String authToken = oauthToken(conf.getSmashName(), conf.getSmashPass());
		
		// Get a server IP
		String serverIP = getServer();
		
		WebSocketClient sockWebSocket = new WebSocketClient(new SslContextFactory());
		sockWebSocket.start();
		SmashEvents socket = new SmashEvents();
		Future<Session> fut = sockWebSocket.connect(socket, URI.create(serverIP));
		session = fut.get();
		
		JSONObject join = new JSONObject();
		JSONObject params = new JSONObject();
		
		join.put("method", "joinChannel");
		
		params.put("channel", "sirboops");
		params.put("name", conf.getSmashName());
		params.put("token", authToken);
		params.put("hideBuffered", true);
		
		join.put("params", params);
		
		session.getRemote().sendString(join.toString());
		
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
		
		String url = ("wss://" + json.getJSONObject(rand.nextInt(json.length())).getString("server_ip"));
		
		return url;
		
	}
	
}
