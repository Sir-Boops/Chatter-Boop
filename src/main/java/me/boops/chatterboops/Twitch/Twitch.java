package me.boops.chatterboops.Twitch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import me.boops.chatterboops.Main;
import me.boops.chatterboops.parsers.TwitchParser;

public class Twitch {
	
	private static BufferedWriter writeStream;
	
	public Twitch() throws Exception{
		
		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket sock = (SSLSocket) sslsocketfactory.createSocket("irc.chat.twitch.tv", 443);
		
		writeStream = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		BufferedReader readStream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		
		sendRaw("PASS " + Main.conf.getTwitchOauth());
		sendRaw("NICK " + Main.conf.getTwitchOauth());
		
		sendRaw("Join #" + Main.conf.getTwitchName().toLowerCase());
		sendRaw("CAP REQ :twitch.tv/tags");

		JSONArray users = getJoinList();
		for(int i=0; users.length()>i; i++){
			joinChannel(uuidToName(users.getInt(i)));
			
		}		
					
		String line = null;
		while((line = readStream.readLine()) != null){
			new TwitchParser(line.toString());
		}		
	}
	
	
	// Write to chat
	public static void sendRaw(String msg) throws Exception{
			
		writeStream.write(msg + "\r\n");
		writeStream.flush();
		
	}
	
	// Write to chat
	public static void sendMSG(String msg, String channel) throws Exception {
			
		writeStream.write("PRIVMSG #" + channel + " :" + msg + "\r\n");
		writeStream.flush();
		
	}
	
	public static void joinChannel(String channel) throws Exception{
		sendRaw("Join #" + channel);
	}
	
	public static void leaveChannel(String channel) throws Exception{
		sendRaw("part  #" + channel);
	}
	
	// Convert UUID to name
	public static String uuidToName(int UUID) throws Exception {
		
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://api.twitch.tv/kraken/channels/" + UUID);
		get.addHeader("Accept", "application/vnd.twitchtv.v5+json");
		get.addHeader("Client-ID", Main.conf.getTwitchClientID());
		
		HttpResponse res = client.execute(get);
		JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
		
		return meta.getString("name").toLowerCase();
	}
	
	//convert name to UUID
	public static int nameToUUID(String name) throws Exception {
		
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://api.twitch.tv/kraken/users?login=" + name);
		get.addHeader("Accept", "application/vnd.twitchtv.v5+json");
		get.addHeader("Client-ID", Main.conf.getTwitchClientID());
		
		HttpResponse res = client.execute(get);
		JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
		
		return meta.getJSONArray("users").getJSONObject(0).getInt("_id");
	}
	
	public static JSONArray getJoinList() throws Exception {
		
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet(Main.API_URL + "v1/twitch/joinlist");
		get.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
		
		HttpResponse res = client.execute(get);
		JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
		
		return meta.getJSONArray("users");
		
	}
}
