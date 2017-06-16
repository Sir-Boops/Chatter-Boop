package me.boops.chatterboops.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.json.JSONObject;

public class FileLoggerRAW {
	
	public FileLoggerRAW(JSONObject msg) throws Exception {
		
		String currentDir = new File(".").getCanonicalPath() + "/";
		
		BufferedWriter bw = null;
		
		bw = new BufferedWriter(new FileWriter(currentDir + "rawChatLog.txt", true));
		
		bw.write(msg.toString());
		
		bw.newLine();
		bw.flush();
		bw.close();
		
	}
}
