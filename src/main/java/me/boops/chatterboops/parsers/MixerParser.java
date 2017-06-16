package me.boops.chatterboops.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import me.boops.chatterboops.API;

public class MixerParser {
	
	public MixerParser(JSONObject msg) throws Exception {
		
		// Check if it is an event
		if(msg.has("event")){
			
			// Check if this is a chat msg or not
			if (msg.getString("event").toLowerCase().equals("chatmessage")){
				
				// Send the parsed message off to the API
				// And check if it's a bot message
				JSONObject ans = cleanMessage(msg);
				
				if(ans != null){
					new API(ans);
				}
				
			}
			
		}
		
		//System.out.println(msg);
		
	}
	
	private JSONObject cleanMessage(JSONObject msg){
		
		JSONObject ans = new JSONObject();
		
		// Get the message
		String message = "";
		for (int i=0; i<msg.getJSONObject("data").getJSONObject("message").getJSONArray("message").length(); i++){
			
			message += msg.getJSONObject("data").getJSONObject("message").getJSONArray("message").getJSONObject(i).getString("text");
			
		}
		
		ans.put("msg", message);
		
		// Get the user role
		int userLevel = 0;
		JSONArray roleList = msg.getJSONObject("data").getJSONArray("user_roles");
		for (int i=0; i<roleList.length(); i++){
			
			// If owner
			if(roleList.get(i).toString().toLowerCase().equals("owner")){
				userLevel = 5;
			}
			
			// If Mod
			if(userLevel < 5 && roleList.get(i).toString().toLowerCase().equals("owner")){
				userLevel = 4;
			}
			
			// I don't know the sub role name
			
			// If pro
			if(userLevel < 2 && roleList.get(i).toString().toLowerCase().equals("pro")){
				userLevel = 2;
			}
			
			// If normal user
			if(userLevel < 1 && roleList.get(i).toString().toLowerCase().equals("user")){
				userLevel = 1;
			}
			
		}
		
		ans.put("userLevel", userLevel);
		ans.put("userName", msg.getJSONObject("data").getString("user_name"));
		ans.put("UUID", msg.getJSONObject("data").getInt("user_id"));
		ans.put("platform", "mixer");
		ans.put("channel", msg.getJSONObject("data").getInt("channel"));
		ans.put("raw", msg.toString());
		
		if(!msg.getJSONObject("data").getString("user_name").toLowerCase().equals("boop_bot")){
			return ans;
		} else {
			return null;
		}
	}
	
}
