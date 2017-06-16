package me.boops.chatterboops.plugins;

import org.json.JSONObject;

import me.boops.chatterboops.Database;
import me.boops.chatterboops.SendMSG;

public class CommandCommand {
	
	public CommandCommand(JSONObject msg) throws Exception {
		
		// Load the dababase
		Database DB = new Database("mixer_" + msg.get("channel").toString() + "_commands");
		
		// The command command
		if(msg.getString("msg").toLowerCase().indexOf("~command") == 0){
			
			// The base command
			if(msg.getString("msg").toLowerCase().equals("~command")){
				//Twitch.sendMSG("'~command help' for help");
			}
			
			// The add command
			if(msg.getString("msg").split(" ").length >= 5 && msg.getInt("userLevel") >= 4){
				
				if(msg.getString("msg").split(" ")[1].toLowerCase().equals("add") || msg.getString("msg").split(" ")[1].toLowerCase().equals("modify")){
					
					
					// Add to the DB
					JSONObject body = new JSONObject();
					
					String comm = msg.getString("msg").split(" ")[2].toLowerCase();
					int level = new Integer(msg.getString("msg").split(" ")[msg.getString("msg").split(" ").length - 1]);
					String commMSG = "";
					
					for (int i=3; i<(msg.getString("msg").split(" ").length - 1); i++){
						
						if(i == 3){
							
							commMSG += (msg.getString("msg").split(" ")[i]);
							
						} else {
							
							commMSG += (" " + msg.getString("msg").split(" ")[i]);
							
						}
						
					}
					
					body.put("userLevel", level);
					body.put("msg", commMSG);
					
					DB.setEntry(comm, body);
					new SendMSG("Set command!", msg);
					
				}
				
			}
			
			if(msg.getString("msg").split(" ").length == 3 && msg.getInt("userLevel") >= 4){
				
				if(msg.getString("msg").split(" ")[1].toLowerCase().equals("del")){
					
					DB.delEntry(msg.getString("msg").split(" ")[2].toLowerCase());
					new SendMSG("Deleted the command", msg);
					
				}
				
			}
			
		}
		
		if(msg.getString("msg").charAt(0) == '~'){
			
			JSONObject body = (JSONObject) DB.getEntry(msg.getString("msg").split(" ")[0].toLowerCase());
			
			if(body != null){
				
				new SendMSG(body.getString("msg"), msg);
				
			}
			
		}
		
	}
}
