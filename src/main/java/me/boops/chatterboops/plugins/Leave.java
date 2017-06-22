package me.boops.chatterboops.plugins;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import me.boops.chatterboops.Main;
import me.boops.chatterboops.Mixer.Mixer;
import me.boops.chatterboops.Twitch.Twitch;

public class Leave {
	
	public Leave(JSONObject msg) throws Exception {
		
		if(msg.getString("msg").toLowerCase().equals("~leave") && msg.getInt("userLevel") == 5){
			
			if(msg.getString("platform").equals("mixer")){
				
				JSONObject body = new JSONObject();
				
				body.put("uuid", msg.getInt("UUID"));
				
				// Add to the joinUsersDB
				HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
				HttpPost post = new HttpPost(Main.API_URL + "v1/mixer/delautojoin");
				post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
				post.setHeader("Content-type", "application/json");
				post.setEntity(new StringEntity(body.toString()));
				
				client.execute(post);
				
				Mixer.sendMSG("Bye @" + msg.getString("userName") + " :(", msg.getInt("channel"));
				
				for(int i=0; i<Mixer.userList.size(); i++){
					
					if(Mixer.userList.get(i) == msg.getInt("channel")){
						
						Mixer.userList.remove(i);
						Mixer.sessionList.remove(i);
						
					}
					
				}
				
				Thread.currentThread().interrupt();
			}
			
			if(msg.getString("platform").equals("twitch")){
						
				JSONObject body = new JSONObject();
				
				body.put("uuid", msg.getInt("UUID"));
				
				// Add to the joinUsersDB
				HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
				HttpPost post = new HttpPost(Main.API_URL + "v1/twitch/delautojoin");
				post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
				post.setHeader("Content-type", "application/json");
				post.setEntity(new StringEntity(body.toString()));
				
				client.execute(post);
				
				String name = Twitch.uuidToName(msg.getInt("UUID"));
				
				Twitch.sendMSG("Bye @" + msg.getString("userName") + " :(", msg.getString("channel"));
				
				Twitch.leaveChannel(name);
				
			}
			
		}
	}
	
}
