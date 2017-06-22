package me.boops.chatterboops.plugins;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import me.boops.chatterboops.Main;
import me.boops.chatterboops.Mixer.Mixer;
import me.boops.chatterboops.Mixer.UserInfo;
import me.boops.chatterboops.Twitch.Twitch;

public class ChannelJoin {
	
	public ChannelJoin(JSONObject msg) throws Exception {
		
		if(msg.getString("msg").toLowerCase().equals("~join")){
			
			if(msg.getString("platform").equals("mixer")){
				
				// Check to make sure the bot is in the bots channel
				if(msg.getInt("channel") == Mixer.botChannel){
					
					boolean found = false;
					
					// Check to see if i'm already in the chat
					JSONArray userList = Mixer.getJoinList();
					
					for(int i=0; i<userList.length(); i++){
						if(userList.getInt(i) == msg.getInt("UUID")){
							
							Mixer.sendMSG("I am already in your channel @" + msg.getString("userName"), msg.getInt("channel"));
							found = true;
							
						}
					}
					
					if(!found){
						
						JSONObject body = new JSONObject();
						
						body.put("uuid", msg.getInt("UUID"));
						
						// Add to the joinUsersDB
						HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
						HttpPost post = new HttpPost(Main.API_URL + "v1/mixer/addautojoin");
						post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
						post.setHeader("Content-type", "application/json");
						post.setEntity(new StringEntity(body.toString()));
						
						client.execute(post);
						
						Mixer.JoinChannel(UserInfo.getBasicUserINFO(msg.getInt("UUID")));
						
						Mixer.sendMSG("Joined channel @" + msg.getString("userName"), msg.getInt("channel"));
						
					}
					
					
				}
			}
			
			if(msg.getString("platform").equals("twitch")){
				
				// Check to make sure the bot is in the bots channel
				if(msg.getString("channel").equals(Main.conf.getTwitchName().toLowerCase())){
					
					boolean found = false;
					
					// Check to see if i'm already in the chat
					JSONArray userList = Twitch.getJoinList();
					
					for(int i=0; i<userList.length(); i++){
						if(userList.getInt(i) == msg.getInt("UUID")){
							
							Twitch.sendMSG("I am already in your channel @" + msg.getString("userName"), msg.getString("channel"));
							found = true;
							
						}
					}
					
					if(!found){
						
						JSONObject body = new JSONObject();
						
						body.put("uuid", msg.getInt("UUID"));
						
						// Add to the joinUsersDB
						HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
						HttpPost post = new HttpPost(Main.API_URL + "v1/twitch/addautojoin");
						post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
						post.setHeader("Content-type", "application/json");
						post.setEntity(new StringEntity(body.toString()));
						
						client.execute(post);
						
						String name = Twitch.uuidToName(msg.getInt("UUID"));
						
						Twitch.joinChannel(name);
						
						Twitch.sendMSG("Joined channel @" + msg.getString("userName"), msg.getString("channel"));
						
					}
				}
			}
			
		}
	}
}
