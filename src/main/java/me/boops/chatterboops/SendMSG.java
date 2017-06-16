package me.boops.chatterboops;

import org.json.JSONObject;

import me.boops.chatterboops.Mixer.Mixer;
import me.boops.chatterboops.Twitch.Twitch;
import me.boops.chatterboops.Youtube.Youtube;

public class SendMSG {
	
	public SendMSG(String msg, JSONObject raw) throws Exception {
		
		// Send a message to twitch
		if(raw.getString("platform").equals("twitch")){
			Twitch.sendMSG(msg, raw.getString("channel"));
		}
		
		// Send a message to mixer
		if(raw.getString("platform").equals("mixer")){
			Mixer.sendMSG(msg, raw.getInt("channel"));
		}
		
		// Send a message to youtube
		if(raw.getString("platform").equals("youtube")){
			Youtube.sendMSG(msg);
		}
		
	}
}
