package me.boops;

import me.boops.Configs.TwitchConfig;
import me.boops.cache.Cache;
import me.boops.configs.TwitchConfigRead;
import me.boops.functions.FindPath;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		//Print Welcome Message!
		System.out.println("Chatter-Boop Starting! Version: " + Cache.version);
		
		//Get Current Jar Name And Path Then Cache It
		new FindPath();
		
		//Load Configs
		new TwitchConfigRead().Load();
		
		System.out.println(TwitchConfig.enabled);
		
	}
	
}
