package me.boops.chatterboops.Mixer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import me.boops.chatterboops.Main;

public class UserInfo {
	
	// Current Channel id
	public static int getBotUserINFO() throws Exception {
		
		// Get Bot UserID
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://mixer.com/api/v1/users/current");
		get.addHeader("Authorization", "Bearer " + Main.conf.getMixerOauth());
		
		HttpResponse res = client.execute(get);
		String meta = new BasicResponseHandler().handleResponse(res);
		
		Mixer.botID = new JSONObject(meta).getInt("id");
		Mixer.botChannel = new JSONObject(meta).getJSONObject("channel").getInt("id");
		
		return new JSONObject(meta).getJSONObject("channel").getInt("id");
		
	}
	
	// Basic Channel id
	public static int getBasicUserINFO(int UserID) throws Exception {
		
		// Get Bot UserID
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://mixer.com/api/v1/users/" + UserID);
		get.addHeader("Authorization", "Bearer " + Main.conf.getMixerOauth());
		
		HttpResponse res = client.execute(get);
		String meta = new BasicResponseHandler().handleResponse(res);
		
		return new JSONObject(meta).getJSONObject("channel").getInt("id");
		
	}
	
}
