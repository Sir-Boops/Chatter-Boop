package me.boops;

import me.boops.cache.DynamicCache;
import me.boops.cache.StaticCache;
import me.boops.configs.CheckConfig;
import me.boops.configs.TwitchConfig;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		//Print Welcome Message!
		System.out.println("Chatter-Boop Starting!, Version: " + StaticCache.version);
		
		//Get Current Jar Name And Path Then Cache It
		String loc = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String[] jarname = loc.split("/");
		DynamicCache.jarname = jarname[jarname.length - 1];
		DynamicCache.jarpath = loc.replace(DynamicCache.jarname, "");
		
		//Load Or Create The Configs
		//Check For Config Path
		new CheckConfig();
		
		//Load Twitch
		new TwitchConfig();
	}
	
}
