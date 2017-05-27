package me.boops.chatterboops.Mixer;

import java.io.IOException;
import java.net.URI;
import java.util.Random;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONObject;

import me.boops.chatterboops.Config;

public class Mixer {
	
	// Random
	Random rand = new Random();
	
	private static Session session;
	
	public Mixer(Config conf) throws Exception {
		
		int userID = getUserID(conf.getMixerOauth());
		String[] authNURL = setupChat(conf.getMixerOauth(), userID);
		
		String authKey = authNURL[0];
		String chatURL = authNURL[1];
		
		WebSocketClient sockWebSocket = new WebSocketClient(new SslContextFactory());
		sockWebSocket.start();
		MixerEvents socket = new MixerEvents();
		Future<Session> fut = sockWebSocket.connect(socket, URI.create(chatURL));
		session = fut.get();
		
		JSONObject login = new JSONObject();
		JSONArray args = new JSONArray();
		
		args.put(32238);
		args.put(userID);
		args.put(authKey);
		
		login.put("type", "method");
		login.put("method", "auth");
		login.put("id", 0);
		login.put("arguments", args);
		
		session.getRemote().sendString(login.toString());
		
	}
	
	// Get bots UserID
	
	private int getUserID(String authToken) throws Exception {
		
		// Get Bot UserID
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://mixer.com/api/v1/users/current");
		get.addHeader("Authorization", "Bearer " + authToken);
		
		HttpResponse res = client.execute(get);
		String meta = new BasicResponseHandler().handleResponse(res);
		
		return new JSONObject(meta).getInt("id");
		
	}
	
	
	// Get chat URL and auth key
	private String[] setupChat(String authToken, int uderID) throws Exception {
		
		// Get the chat auth token
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://mixer.com/api/v1/chats/32238");
		get.addHeader("Authorization", "Bearer " + authToken);
		
		HttpResponse res = client.execute(get);
		String meta = new BasicResponseHandler().handleResponse(res);
		JSONObject json = new JSONObject(meta);
		
		String authKey = json.getString("authkey");
		String chatURL = json.getJSONArray("endpoints").getString(rand.nextInt(json.getJSONArray("endpoints").length()));
		
		String[] ans = {authKey, chatURL};
		
		return ans;
		
	}
	
	// Write to chat
	public static void sendMSG(String msg) {
		
		JSONObject main = new JSONObject();
		JSONArray message = new JSONArray();
		
		main.put("type", "method");
		main.put("method", "msg");
		
		message.put(msg);
		
		main.put("arguments", message);
		main.put("id", 2);
		
			
		try {
			session.getRemote().sendString(main.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
