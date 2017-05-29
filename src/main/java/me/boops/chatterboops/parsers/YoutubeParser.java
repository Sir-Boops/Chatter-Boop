package me.boops.chatterboops.parsers;

import org.json.JSONObject;

import me.boops.chatterboops.API;
import me.boops.chatterboops.Youtube.Youtube;

public class YoutubeParser {
	
	public YoutubeParser(JSONObject msg) throws Exception{
		
		String userMSG = msg.getJSONObject("snippet").getString("displayMessage");
		String userUUID = msg.getJSONObject("snippet").getString("authorChannelId");
		String userName = Youtube.getUserName(userUUID);
		// Until i find how to view chat levels
		int userLevel = 1;
		
		JSONObject ans = new JSONObject();
		
		ans.put("msg", userMSG);
		ans.put("userLevel", userLevel);
		ans.put("userName", userName);
		ans.put("UUID", userUUID);
		ans.put("platform", "youtube");
		ans.put("raw", msg.toString());
		
		new API(ans);
		Youtube.sendMessage("Hi!");
		
	}
}
