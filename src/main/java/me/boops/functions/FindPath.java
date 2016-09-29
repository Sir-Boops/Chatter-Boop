package me.boops.functions;

import me.boops.Main;
import me.boops.cache.Cache;

public class FindPath {
	
	public FindPath(){
		
		String loc = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String[] jarname = loc.split("/");
		Cache.jarname = jarname[jarname.length - 1];
		Cache.jarpath = loc.replace(Cache.jarname, "");
		
	}
}
