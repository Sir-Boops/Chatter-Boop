package me.boops.configs;

import java.io.File;

import me.boops.cache.DynamicCache;

public class CheckConfig {

	public CheckConfig() {

		// Local Strings
		String confdir = DynamicCache.jarpath + "configs/";
		File conffolder = new File(confdir);

		// Check If Folder Exists And We Can Read From It!
		if (!conffolder.exists()) {
			
			//Create A New Configs Folder
			new File(confdir).mkdir();
		}

	}
}
