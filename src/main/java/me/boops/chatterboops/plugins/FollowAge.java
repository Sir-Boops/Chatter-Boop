package me.boops.chatterboops.plugins;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

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
import me.boops.chatterboops.SendMSG;
import me.boops.chatterboops.Twitch.Twitch;

public class FollowAge {
	
	public FollowAge(JSONObject msg) throws Exception{
		
		if(msg.getString("msg").split(" ")[0].toLowerCase().equals("~age")){
			
			if(msg.getString("platform").equals("mixer")){
				
				MixerAge(msg);
				
			}
			
			if(msg.getString("platform").equals("twitch")){
				
				TwitchAge(msg);
				
			}	
			
		}
	}
	
	private JSONObject MixerAge(JSONObject msg) throws Exception {
		
		
		
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://mixer.com/api/v1/channels/" + msg.getInt("channel") + "/analytics/tsdb/followers?from=" + getChannelCreationDate(msg.getInt("channel")));
		get.addHeader("Authorization", "Bearer " + Main.conf.getMixerOauth());
		
		HttpResponse res = client.execute(get);
		res.setStatusCode(200);
		String meta = new BasicResponseHandler().handleResponse(res);
		
		System.out.println(meta);
		
		return new JSONObject();
		
	}
	
	private String getChannelCreationDate(int UUID) throws Exception {
		
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://mixer.com/api/v1/channels/" + UUID);
		get.addHeader("Authorization", "Bearer " + Main.conf.getMixerOauth());
		
		HttpResponse res = client.execute(get);
		JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
		
		return meta.getString("createdAt");
		
	}
	
	private void TwitchAge(JSONObject msg) throws Exception {
		
		int ChannelUUID = Twitch.nameToUUID(msg.getString("channel"));
		
		JSONObject init = getMoreTwitchFollowers(0, ChannelUUID);
		
		int total = init.getInt("_total");
		int scanned = 0;
		boolean found = false;
		
		while(scanned<total && !found){
			
			for(int i=0; i<init.getJSONArray("follows").length() && !found; i++){
				
				JSONObject user = init.getJSONArray("follows").getJSONObject(i);
				
				if(msg.getString("userName").toLowerCase().equals(user.getJSONObject("user").getString("name").toLowerCase())){
					
					DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
					TemporalAccessor accessor = timeFormatter.parse(user.getString("created_at"));
					
					Date date = Date.from(Instant.from(accessor));
					
					new SendMSG("@" + user.getJSONObject("user").getString("display_name") + " Followed the channel on " + date.toString(), msg);
					found = true;
				}
				
			}
			
			scanned += init.getJSONArray("follows").length();
			init = getMoreTwitchFollowers(scanned, ChannelUUID);
			
		}
		
		if(!found){
			new SendMSG("@" + msg.getString("userName") + " does not follow the channel", msg);
		}
		
	}
	
	private JSONObject getMoreTwitchFollowers(int offset, int UUID) throws Exception {
		
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://api.twitch.tv/kraken/channels/" + UUID + "/follows?offset=" + offset);
		get.addHeader("Client-ID", Main.conf.getTwitchClientID());
		get.addHeader("Accept", "application/vnd.twitchtv.v5+json");
		
		HttpResponse res = client.execute(get);
		return new JSONObject(new BasicResponseHandler().handleResponse(res));
		
	}
	
}
