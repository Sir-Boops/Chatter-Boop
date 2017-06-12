package me.boops.chatterboops;

import org.json.JSONObject;

import me.boops.chatterboops.plugins.BoopCommand;
import me.boops.chatterboops.plugins.CommandCommand;
import me.boops.chatterboops.plugins.FileLogger;
import me.boops.chatterboops.plugins.Roll;

public class API {
	
	// This is where the parser dumps what i made and that is passed along to the plugins
	public API(JSONObject msg) throws Exception {
		
		// 'Plugins'
		new BoopCommand(msg);
		new CommandCommand(msg);
		new Roll(msg);
		
		
		
		new FileLogger(msg);
		
		System.out.println(msg);
		
	}
}
