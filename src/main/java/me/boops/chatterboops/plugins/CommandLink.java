package me.boops.chatterboops.plugins;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import me.boops.chatterboops.Database;
import me.boops.chatterboops.Main;
import me.boops.chatterboops.Twitch.Twitch;

public class CommandLink {
	
	public CommandLink(JSONObject msg) throws Exception {
		
		if(msg.getString("msg").split(" ").length == 3){
			
			if(msg.getString("msg").toLowerCase().split(" ")[0].equals("~link") && msg.getInt("userLevel") == 5){
				
				String platform = msg.getString("msg").toLowerCase().split(" ")[1];
				String userName = msg.getString("msg").toLowerCase().split(" ")[2];
				
				if(platform.equals("mixer")){
					
					//Link X to mixer
					Database DB = new Database(msg.getString("platform") + "_" + msg.get("UUID").toString() + "_commands");
					
					// Convert a users username to UUID
					RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
					HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
					HttpGet get = new HttpGet("https://mixer.com/api/v1/users/search?limit=1&query=" + userName.toLowerCase());
					get.addHeader("Authorization", "Bearer " + Main.conf.getMixerOauth());
					
					HttpResponse res = client.execute(get);
					JSONArray meta = new JSONArray(new BasicResponseHandler().handleResponse(res));
					
					if(res.getStatusLine().getStatusCode() == 200 && meta.length() >= 1){
						
						JSONObject main = new JSONObject();
						
						main.put("platform", platform);
						main.put("UUID", meta.getJSONObject(0).getInt("id"));
						
						DB.setEntry("link", main);
						
						Twitch.sendMSG("Linked to channel: " + meta.getJSONObject(0).getString("username") + " On platform: " + platform, msg.getString("channel"));
						
					} else {
						
						Twitch.sendMSG("Could not find user: " + userName, msg.getString("channel"));
						
					}
				}
				
			}
		}
	}
}
