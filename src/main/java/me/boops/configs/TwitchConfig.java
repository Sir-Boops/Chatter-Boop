package me.boops.configs;

import java.io.File;
import java.io.IOException;

import me.boops.cache.DynamicCache;

public class TwitchConfig {
	
	public TwitchConfig() throws IOException{
		
		//Get Directory Strings
		String confdir = DynamicCache.jarpath + "configs/";
		File twitchconf = new File(confdir + "Twitch.Config");
		
		//Check To Make Sure File Exists
		if(twitchconf.exists()){
			
			//It's Real So We Can Load From It!
			
		} else {
			
			
			System.out.println();
			
			//It's Not Real So Time To Create It!
			
			new File(confdir + "configs/Twitch.Config").createNewFile();
			
		}
	}
}
