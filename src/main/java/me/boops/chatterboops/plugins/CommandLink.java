package me.boops.chatterboops.plugins;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import me.boops.chatterboops.Main;
import me.boops.chatterboops.SendMSG;
import me.boops.chatterboops.Mixer.Mixer;
import me.boops.chatterboops.Twitch.Twitch;

public class CommandLink {
	
	public CommandLink(JSONObject msg) throws Exception {
		
		if(msg.getString("msg").split(" ").length >= 2){
			
			if(msg.getString("msg").toLowerCase().split(" ")[0].equals("~link") && msg.getInt("userLevel") == 5){
				
				if(!msg.getString("msg").split(" ")[1].toLowerCase().equals("remove")){
					
					String platform = msg.getString("msg").toLowerCase().split(" ")[1];
					String userName = msg.getString("msg").toLowerCase().split(" ")[2];
					int userID = 0;
					
					// convert username to ID
					if(platform.equals("twitch")){
						userID = Twitch.nameToUUID(userName);
					}
					
					if(platform.equals("mixer")){
						userID = Mixer.searchUser(userName).getJSONObject("channel").getInt("id");
					}
					
					JSONObject body = new JSONObject();
					
					body.put("platform", platform);
					body.put("link", userID);
					
					if(msg.getString("platform").equals("twitch")){
						body.put("channel", Twitch.nameToUUID(msg.getString("channel")));
					}
					
					if(msg.getString("platform").equals("mixer")){
						body.put("channel", msg.getInt("channel"));
					}
					
					HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
					HttpPost post = new HttpPost(Main.API_URL + "v1/" + msg.getString("platform") + "/linkadd");
					post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
					post.setHeader("Content-type", "application/json");
					post.setEntity(new StringEntity(body.toString()));
					
					HttpResponse res = client.execute(post);
					JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
					
					if(meta.getString("status").equals("ok")){
						new SendMSG("Linked to channel!", msg);
					}
					
				} else {
					
					JSONObject body = new JSONObject();
					
					if(msg.getString("platform").equals("twitch")){
						body.put("channel", Twitch.nameToUUID(msg.getString("channel")));
					}
					
					if(msg.getString("platform").equals("mixer")){
						body.put("channel", msg.getInt("channel"));
					}
					
					HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
					HttpPost post = new HttpPost(Main.API_URL + "v1/" + msg.getString("platform") + "/linkdel");
					post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
					post.setHeader("Content-type", "application/json");
					post.setEntity(new StringEntity(body.toString()));
					
					HttpResponse res = client.execute(post);
					JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
					
					if(meta.getString("status").equals("ok")){
						new SendMSG("Removed channel link", msg);
					}
					
				}
			}
		}
	}
}
