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
import me.boops.chatterboops.Twitch.Twitch;

public class CommandCommand {
	
	public CommandCommand(JSONObject msg) throws Exception {
		
		// The command command
		if(msg.getString("msg").toLowerCase().indexOf("~command") == 0) {
			
			if(msg.getString("msg").split(" ").length >= 3) {
				
				String[] link = getLink(msg);
				
				if(link == null) {
					
					if(msg.getString("msg").toLowerCase().split(" ")[1].equals("set")) {
						
						String[] split = msg.getString("msg").split(" ");
						
						JSONObject body = new JSONObject();
						
						if(msg.getString("platform").equals("twitch")){
							body.put("channel", Twitch.nameToUUID(msg.getString("channel")));
						}
						
						if(msg.getString("platform").equals("mixer")){
							body.put("channel", msg.getInt("channel"));
						}
						
						body.put("command", split[2].toLowerCase());
						
						String msgBody = "";
						
						for(int i=3; i<(split.length - 1); i++){
							
							if(i == 3){
								
								msgBody += split[i];
								
							} else {
								
								msgBody += (" " + split[i]);
								
							}
							
						}
						
						body.put("body", msgBody);
						body.put("level", split[split.length - 1]);
						
						// Add to the joinUsersDB
						HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
						HttpPost post = new HttpPost(Main.API_URL + "v1/" + msg.getString("platform") + "/setcommand");
						post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
						post.setHeader("Content-type", "application/json");
						post.setEntity(new StringEntity(body.toString()));
						
						HttpResponse res = client.execute(post);
						JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
						
						if(meta.getString("status").equals("ok")){
							new SendMSG("Command Set!", msg);
						}
						
					}
					
					if(msg.getString("msg").toLowerCase().split(" ")[1].equals("rm")) {
						
						String[] split = msg.getString("msg").split(" ");
						
						JSONObject body = new JSONObject();
						
						if(msg.getString("platform").equals("twitch")){
							body.put("channel", Twitch.nameToUUID(msg.getString("channel")));
						}
						
						if(msg.getString("platform").equals("mixer")){
							body.put("channel", msg.getInt("channel"));
						}
						
						body.put("command", split[2].toLowerCase());
						
						// Add to the joinUsersDB
						HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
						HttpPost post = new HttpPost(Main.API_URL + "v1/" + msg.getString("platform") + "/rmcommand");
						post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
						post.setHeader("Content-type", "application/json");
						post.setEntity(new StringEntity(body.toString()));
						
						HttpResponse res = client.execute(post);
						JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
						
						if(meta.getString("status").equals("ok")){
							new SendMSG("Command Removed!", msg);
						}
						
					}
					
				} else {
					
					new SendMSG("Please use " + link[0] + " to set commands", msg);
					
				}
				
			}
		}
		
		if(msg.getString("msg").toLowerCase().equals("~commands")) {
			
			String[] link = getLink(msg);
			
			if(link == null){
				
				JSONObject body = new JSONObject();
				
				if(msg.getString("platform").equals("twitch")){
					body.put("channel", Twitch.nameToUUID(msg.getString("channel")));
				}
				
				if(msg.getString("platform").equals("mixer")){
					body.put("channel", msg.getInt("channel"));
				}
				
				// Add to the joinUsersDB
				HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
				HttpPost post = new HttpPost(Main.API_URL + "v1/" + msg.getString("platform") + "/listcommands");
				post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
				post.setHeader("Content-type", "application/json");
				post.setEntity(new StringEntity(body.toString()));
				
				HttpResponse res = client.execute(post);
				JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
				
				if(meta.getJSONArray("commands").length() > 0){
					
					String ans = "Current commands are:";
					
					for(int i=0; i<meta.getJSONArray("commands").length(); i++){
						ans += (" " + meta.getJSONArray("commands").getJSONObject(i).getString("command"));
					}
					
					new SendMSG(ans, msg);
					
				} else {
					
					new SendMSG("You have no commands!", msg);
					
				}
				
			} else {
				
				JSONObject body = new JSONObject();
				
				body.put("channel", (int) new Integer(link[1]));
				
				// Add to the joinUsersDB
				HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
				HttpPost post = new HttpPost(Main.API_URL + "v1/" + link[0] + "/listcommands");
				post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
				post.setHeader("Content-type", "application/json");
				post.setEntity(new StringEntity(body.toString()));
				
				HttpResponse res = client.execute(post);
				JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
				
				if(meta.getJSONArray("commands").length() > 0){
					
					String ans = "Current commands are:";
					
					for(int i=0; i<meta.getJSONArray("commands").length(); i++){
						ans += (" " + meta.getJSONArray("commands").getJSONObject(i).getString("command"));
					}
					
					new SendMSG(ans, msg);
					
				} else {
					
					new SendMSG("You have no commands!", msg);
					
				}
				
			}
			
		}
		
		if(msg.getString("msg").charAt(0) == '~') {
			
			String[] link = getLink(msg);
			
			if(link == null){
				
				JSONObject body = new JSONObject();
				
				if(msg.getString("platform").equals("twitch")){
					body.put("channel", Twitch.nameToUUID(msg.getString("channel")));
				}
				
				if(msg.getString("platform").equals("mixer")){
					body.put("channel", msg.getInt("channel"));
				}
				
				// Add to the joinUsersDB
				HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
				HttpPost post = new HttpPost(Main.API_URL + "v1/" + msg.getString("platform") + "/listcommands");
				post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
				post.setHeader("Content-type", "application/json");
				post.setEntity(new StringEntity(body.toString()));
				
				HttpResponse res = client.execute(post);
				JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
				
				if(meta.getJSONArray("commands").length() > 0){
					
					for(int i=0; i<meta.getJSONArray("commands").length(); i++){
						
						if(meta.getJSONArray("commands").getJSONObject(i).getString("command").equals(msg.getString("msg").toLowerCase())){
							if(msg.getInt("userLevel") >= meta.getJSONArray("commands").getJSONObject(i).getInt("level")){
								new SendMSG(meta.getJSONArray("commands").getJSONObject(i).getString("body"), msg);
							}
						}
					}
					
				}
				
			} else {
				
				JSONObject body = new JSONObject();
				
				body.put("channel", (int) new Integer(link[1]));
				
				// Add to the joinUsersDB
				HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
				HttpPost post = new HttpPost(Main.API_URL + "v1/" + link[0] + "/listcommands");
				post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
				post.setHeader("Content-type", "application/json");
				post.setEntity(new StringEntity(body.toString()));
				
				HttpResponse res = client.execute(post);
				JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
				
				if(meta.getJSONArray("commands").length() > 0){
					
					for(int i=0; i<meta.getJSONArray("commands").length(); i++){
						
						if(meta.getJSONArray("commands").getJSONObject(i).getString("command").equals(msg.getString("msg").toLowerCase())){
							if(msg.getInt("userLevel") >= meta.getJSONArray("commands").getJSONObject(i).getInt("level")){
								new SendMSG(meta.getJSONArray("commands").getJSONObject(i).getString("body"), msg);
							}
						}
					}
					
				}
				
				
			}
		}
	}
	
	private String[] getLink(JSONObject msg) throws Exception {
		
		
		JSONObject body = new JSONObject();
		
		if(msg.getString("platform").equals("twitch")){
			body.put("channel", Twitch.nameToUUID(msg.getString("channel")));
		}
		
		if(msg.getString("platform").equals("mixer")){
			body.put("channel", msg.getInt("channel"));
		}
		
		// Check for a link
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
		HttpPost post = new HttpPost(Main.API_URL + "v1/" + msg.getString("platform") + "/linkcheck");
		post.addHeader("Client-Key", Main.conf.getBoopsAPIKey());
		post.setHeader("Content-type", "application/json");
		post.setEntity(new StringEntity(body.toString()));
		
		HttpResponse res = client.execute(post);
		JSONObject meta = new JSONObject(new BasicResponseHandler().handleResponse(res));
		
		String[] ans = new String[2];
		
		if(meta.getBoolean("linked")){
			
			ans[0] = meta.getString("platform");
			ans[1] = Integer.toString(meta.getInt("link"));
			
		} else {
			ans = null;
		}
		
		return ans;
		
	}
	
}
