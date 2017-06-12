package me.boops.chatterboops.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.json.JSONObject;

public class FileLogger {
	
	public FileLogger(JSONObject msg) throws Exception {
		
		String currentDir = new File(".").getCanonicalPath() + "/";
		
		BufferedWriter bw = null;
		
		bw = new BufferedWriter(new FileWriter(currentDir + "chatLog.txt", true));
		
		if(msg.getString("platform").equals("twitch")){
			bw.write("[Twitch - " + msg.getString("userName") + "]: " + msg.getString("msg"));
		}
		
		if(msg.getString("platform").equals("mixer")){
			bw.write("[Mixer - " + msg.getString("userName") + "]: " + msg.getString("msg"));
		}
		
		if(msg.getString("platform").equals("youtube")){
			bw.write("[Youtube - " + msg.getString("userName") + "]: " + msg.getString("msg"));
		}
		
		bw.newLine();
		bw.flush();
		bw.close();
		
	}
}
