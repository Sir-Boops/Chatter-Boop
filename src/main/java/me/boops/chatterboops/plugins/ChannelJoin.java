package me.boops.chatterboops.plugins;

import org.json.JSONArray;
import org.json.JSONObject;

import me.boops.chatterboops.Database;
import me.boops.chatterboops.Main;
import me.boops.chatterboops.Mixer.Mixer;
import me.boops.chatterboops.Mixer.UserInfo;
import me.boops.chatterboops.Twitch.Twitch;

public class ChannelJoin {
	
	public ChannelJoin(JSONObject msg) throws Exception{
		
		if(msg.getString("msg").toLowerCase().equals("~join")){
			
			if(msg.getString("platform").equals("mixer")){
				
				// Check to make sure the bot is in the bots channel
				if(msg.getInt("channel") == Mixer.botChannel){
					
					Database DB = new Database("mixer_users");
					
					JSONArray users = (JSONArray) DB.getEntry("users");
					
					if(users == null){
						
						users = new JSONArray();
						
					}
					
					users.put(msg.getInt("UUID"));
					
					DB.setEntry("users", users);
					
					Mixer.JoinChannel(UserInfo.getBasicUserINFO(msg.getInt("UUID")));
					
					Mixer.sendMSG("Joined channel @" + msg.getString("userName"), msg.getInt("channel"));
					
					
				}
			}
			
			if(msg.getString("platform").equals("twitch")){
				
				// Check to make sure the bot is in the bots channel
				if(msg.getString("channel").equals(Main.conf.getTwitchName().toLowerCase())){
					
					Database DB = new Database("twitch_users");
					
					JSONArray users = (JSONArray) DB.getEntry("users");
					
					if(users == null){
						
						users = new JSONArray();
						
					}
					
					users.put(msg.getInt("UUID"));
					
					DB.setEntry("users", users);
					
					String name = Twitch.uuidToName(msg.getInt("UUID"));
					
					Twitch.joinChannel(name);
					
					Twitch.sendMSG("Joined channel @" + msg.getString("userName"), msg.getString("channel"));
					
					
				}
			}
			
		}
	}
}
