package me.boops.chatterboops.plugins;

import org.json.JSONObject;

import me.boops.chatterboops.Mixer.Mixer;
import me.boops.chatterboops.Twitch.Twitch;
import me.boops.chatterboops.Youtube.Youtube;

public class BoopCommand {
	
	public BoopCommand(JSONObject msg) throws Exception {
	
		if(msg.getString("msg").toLowerCase().equals("~boop")){
			
			if(msg.getString("platform").equals("twitch")){
				
				Twitch.sendMSG("/me *boops* @" + msg.getString("userName"));
				
			}
			
			if(msg.getString("platform").equals("mixer")){
				
				Mixer.sendMSG("/me *boops* @" + msg.getString("userName"));
				
			}
			
			if(msg.getString("platform").equals("youtube")){
				
				Youtube.sendMSG("*boops* @" + msg.getString("userName"));
				
			}
			
		}
	}
}
