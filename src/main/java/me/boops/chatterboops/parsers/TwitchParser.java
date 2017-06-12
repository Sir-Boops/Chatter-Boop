package me.boops.chatterboops.parsers;

import org.json.JSONObject;

import me.boops.chatterboops.API;
import me.boops.chatterboops.Twitch.Twitch;

public class TwitchParser {
	
	public TwitchParser(String msg) throws Exception{
		
		// Respond to pings
		if(msg.indexOf("PING") == 0){
			Twitch.sendRaw("PONG :tmi.twitch.tv");
		}
		
		if(msg.contains("PRIVMSG")){
			
			String[] split = msg.split(" ");
			String tags = split[0];
			
			// Get the name
			String name = tags.split(";")[2].replaceFirst("display-name=", "");
			
			// Get the users message
			String realMSG = msg.replaceFirst(split[0], "").replaceFirst(" ", "").replace(split[1], "").replaceFirst(" ", "").replace(split[2], "")
					.replaceFirst(" ", "").replace(split[3], "").replaceFirst(" ", "").replaceFirst(":", "");
			
			// Get the user level
			int userLevel = getUserLevel(tags);
			
			// Send it off to the API
			JSONObject ans = new JSONObject();
			
			ans.put("msg", realMSG);
			ans.put("userLevel", userLevel);
			ans.put("userName", name);
			ans.put("platform", "twitch");
			ans.put("raw", msg);
			
			boolean found = false;
			for(int i=0; i<tags.split(";").length && !found; i++){
				
				if(tags.split(";")[i].contains("user-id=")){
					
					ans.put("UUID", tags.split(";")[i].replaceFirst("user-id=", ""));
					found = true;
					
				}
				
			}
			
			new API(ans);
			
		}
		
	}
	
	private int getUserLevel(String msg){
		
		int ans = 1;
		
		// Check if broadcaster
		if(msg.split(";")[0].contains("@badges=broadcaster/1")){
			ans = 5;
		}
		
		if(msg.split(";")[5].contains("mod=1") && ans == 1){
			ans = 4;
		}
		
		if(msg.split(";")[8].contains("subscriber=1") && ans == 1){
			ans = 3;
		}
		
		if(msg.split(";")[10].contains("turbo=0") && ans == 1){
			ans = 1;
		}
		
		return ans;
		
	}
	
}
