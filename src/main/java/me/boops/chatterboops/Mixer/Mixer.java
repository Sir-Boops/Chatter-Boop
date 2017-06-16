package me.boops.chatterboops.Mixer;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
import me.boops.chatterboops.Database;

public class Mixer {
	
	// Random
	private static Random rand = new Random();
	private static Config conf;
	
	// Bot ID
	public static int botID = 0;
	public static int botChannel = 0;
	
	//User List
	private static List<Session> sessionList = new ArrayList<Session>();
	public static List<Integer> userList = new ArrayList<Integer>();
	
	
	public Mixer(Config conf) throws Exception {
		
		Mixer.conf = conf;
		
		// Join the bots channel
		JoinChannel(UserInfo.getBotUserINFO());
		
		// On inital startup get other channels to join
		Database DB = new Database("mixer_users");
		
		JSONArray users = (JSONArray) DB.getEntry("users");
		
		if(users != null){
			
			for(int i=0; users.length()>i; i++){
				
				JoinChannel(UserInfo.getBasicUserINFO((int) users.get(i)));
			}
			
		}
		
	}
	
	public static void JoinChannel(int channelID) throws Exception{
		
		String[] authURL = setupChat(conf.getMixerOauth(), channelID);
		
		String authKey = authURL[0];
		String chatURL = authURL[1];
		
		// Launch mixer sub thread
		new Thread(new Runnable(){
			
			public void run(){
				
				try {
					
					Session session;
					
					WebSocketClient sockWebSocket = new WebSocketClient(new SslContextFactory());
					sockWebSocket.start();
					MixerEvents socket = new MixerEvents();
					Future<Session> fut = sockWebSocket.connect(socket, URI.create(chatURL));
					session = fut.get();
					
					JSONObject login = new JSONObject();
					JSONArray args = new JSONArray();
					
					args.put(channelID);
					args.put(Mixer.botID);
					args.put(authKey);
					
					login.put("type", "method");
					login.put("method", "auth");
					login.put("id", 0);
					login.put("arguments", args);
					
					session.getRemote().sendString(login.toString());
					
					Mixer.sessionList.add(session);
					Mixer.userList.add(channelID);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}
	
	
	// Get chat URL and auth key
	private static String[] setupChat(String authToken, int ChannelID) throws Exception {
		
		// Get the chat auth token
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://mixer.com/api/v1/chats/" + ChannelID);
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
	public static void sendMSG(String msg, int channelID) {
		
		JSONObject main = new JSONObject();
		JSONArray message = new JSONArray();
		
		main.put("type", "method");
		main.put("method", "msg");
		
		message.put(msg);
		
		main.put("arguments", message);
		main.put("id", 2);
		
			
		try {
			
			for(int i=0; Mixer.userList.size() > i; i++){
				
				if(Mixer.userList.get(i) == channelID){
					
					Mixer.sessionList.get(i).getRemote().sendString(main.toString());
					
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
