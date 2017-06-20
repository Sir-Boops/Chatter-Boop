package me.boops.chatterboops.plugins;

import java.util.Random;

import org.json.JSONObject;

import me.boops.chatterboops.SendMSG;

public class Roll {
	
	public Roll(JSONObject msg) throws Exception {
		
		if(msg.getString("msg").toLowerCase().indexOf("~roll") == 0 && msg.getString("msg").split(" ").length == 2){
			
			Random rand = new Random();
			
			int max = -1;
			
			try {
				
				max = Integer.parseInt(msg.getString("msg").split(" ")[1]);
				
				if(max <= 1){
					new SendMSG("Rude trying to crash me!", msg);
					max = 0;
				}
				
			} catch (Exception e){
				e.printStackTrace();
			}
			
			if(max > 0){
				
				int roll = rand.nextInt(max - 1) + 1;
				
				new SendMSG("Your random number is: " + roll, msg);
				
			}
			
		}
		
	}
	
}
