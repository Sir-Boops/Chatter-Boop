package me.boops.chatterboops;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONObject;

public class Config {
	
	// Private Strings
	
	// Twitch Strings
	boolean enableTwitch;
	private String twitchBotName;
	private String TwitchOauthKey;
	private String TwitchClientID;
	
	// Mixer Strings
	private boolean enableMixer;
	private String MixerOauth;
	
	// Youtube Strings
	private boolean enableYoutube;
	private String youtubeClientID;
	private String youtubeClientSec;
	private String youtubeToken;
	private String youtubeChatID;
	private String youtubeBotChannelID;
	
	// Boops API Strings
	private String boopAPIKey;
	
	public Config(){
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("config.json"));
			String line = br.readLine();
			while(line != null){
				sb.append(line);
				line = br.readLine();
			}
			
			br.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject config = new JSONObject(sb.toString());
		
		// Get Twitch Strings
		this.enableTwitch = config.getJSONObject("twitch").getBoolean("enable");
		this.twitchBotName = config.getJSONObject("twitch").getString("name").toLowerCase();
		this.TwitchOauthKey = config.getJSONObject("twitch").getString("oauth");
		this.TwitchClientID = config.getJSONObject("twitch").getString("clientID");
		
		// Get Mixer Strings
		this.enableMixer = config.getJSONObject("mixer").getBoolean("enable");
		this.MixerOauth = config.getJSONObject("mixer").getString("oauth");
		
		// Get Youtube strings
		this.enableTwitch = config.getJSONObject("youtube").getBoolean("enable");
		this.youtubeClientID = config.getJSONObject("youtube").getString("clientID");
		this.youtubeClientSec = config.getJSONObject("youtube").getString("clientSec");
		this.youtubeToken = config.getJSONObject("youtube").getString("token");
		this.youtubeChatID = config.getJSONObject("youtube").getString("chatID");
		this.youtubeBotChannelID = config.getJSONObject("youtube").getString("botChannelID");
		
		// Get Boops_API Key
		this.boopAPIKey = config.getJSONObject("boops_api").getString("key");
		
	}
	
	// Return twitch strings
	
	public boolean enableTwitch(){
		return this.enableTwitch;
	}
	
	public String getTwitchName(){
		return this.twitchBotName;
	}
	
	public String getTwitchOauth(){
		return this.TwitchOauthKey;
	}
	
	public String getTwitchClientID(){
		return this.TwitchClientID;
	}
	
	// Return Mixer strings
	
	public boolean enableMixer(){
		return this.enableMixer;
	}
	
	public String getMixerOauth(){
		return this.MixerOauth;
	}
	
	
	// Return Youtube strings
	public boolean enableYoutube(){
		return this.enableYoutube;
	}
	
	public String getYoutubeClientID(){
		return this.youtubeClientID;
	}
	
	public String getYoutubeClientSec(){
		return this.youtubeClientSec;
	}
	
	public String getYoutubeToken(){
		return this.youtubeToken;
	}
	
	public String getYoutubeChatID() {
		return this.youtubeChatID;
	}
	
	public String getYoutubeBotChannelID(){
		return this.youtubeBotChannelID;
	}
	
	//Return Boops_API key
	public String getBoopsAPIKey(){
		return this.boopAPIKey;
	}
	
}
