package me.boops.chatterboops;

import me.boops.chatterboops.Mixer.Mixer;
import me.boops.chatterboops.Twitch.Twitch;
import me.boops.chatterboops.Youtube.Youtube;

public class SendMSG {
	
	public SendMSG(String msg, String platform) throws Exception {
		
		// Send a message to twitch
		if(platform.equals("twitch")){
			Twitch.sendMSG(msg);
		}
		
		// Send a message to mixer
		if(platform.equals("mixer")){
			Mixer.sendMSG(msg);
		}
		
		// Send a message to youtube
		if(platform.equals("youtube")){
			Youtube.sendMSG(msg);
		}
		
	}
}
