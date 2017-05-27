package me.boops.chatterboops.parsers;

import org.json.JSONObject;

public class MixerParser {
	
	public MixerParser(JSONObject msg){
		
		// Check if it is an event
		if(msg.has("event")){
			
			// Check if this is a chat msg or not
			if (msg.getString("event").toLowerCase().equals("chatmessage")){
				
				// Now get the full message
				
				String message = "";
				
				for (int i=0; i<msg.getJSONObject("data").getJSONObject("message").getJSONArray("message").length(); i++){
					
					message += msg.getJSONObject("data").getJSONObject("message").getJSONArray("message").getJSONObject(i).getString("text");
					
				}
				
				System.out.println(message);
				
			}
			
		}
		
		//System.out.println(msg);
		
	}
}
