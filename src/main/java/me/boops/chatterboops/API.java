package me.boops.chatterboops;

import org.json.JSONObject;

import me.boops.chatterboops.commands.boop.BoopCommand;
import me.boops.chatterboops.commands.command.CommandCommand;

public class API {
	
	// This is where the parser dumps what i made and that is passed along to the plugins
	public API(JSONObject msg) throws Exception {
		
		// 'Plugins'
		new BoopCommand(msg);
		new CommandCommand(msg);
		
		System.out.println(msg);
		
	}
}
