package me.boops.chatterboops;

import org.json.JSONObject;

import me.boops.chatterboops.commands.boop.BoopCommand;

public class API {
	
	// This is where the parser dumps what i made and that is passed along to the plugins
	public API(JSONObject msg) throws Exception {
		
		// 'Plugins'
		new BoopCommand(msg);
		
		System.out.println(msg);
		
	}
}
