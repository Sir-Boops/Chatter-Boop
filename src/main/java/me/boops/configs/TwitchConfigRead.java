package me.boops.configs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

import me.boops.Configs.TwitchConfig;
import me.boops.cache.Cache;

public class TwitchConfigRead {
	
	public void Load() throws IOException{
		
		System.out.println("Trying To Load Twitch Config!");
		
		//Read The Config File
		String conf = (Cache.jarpath + "configs/Twitch.json");
		BufferedReader br = new BufferedReader(new FileReader(conf));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while(line != null){
			sb.append(line);
			line = br.readLine();
		}
		br.close();
		
		// Parse The Config File
		JSONObject config = new JSONObject(sb.toString());
		
		TwitchConfig.enabled = config.getBoolean("enabled");
	}

}
