package me.boops.chatterboops.plugins;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import me.boops.chatterboops.Main;

public class FollowAge {
	
	public FollowAge(JSONObject msg) throws Exception{
		
		if(msg.getString("platform").equals("mixer")){
			
			MixerAge(msg);
			
		}
		
	}
	
	private JSONObject MixerAge(JSONObject msg) throws Exception {
		
		RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).setDefaultRequestConfig(customizedRequestConfig).build();
		HttpGet get = new HttpGet("https://mixer.com/api/v1/channels/32238/analytics/tsdb/followers?from=2015-06-16T14:30:40Z");
		get.addHeader("Authorization", "Bearer " + Main.conf.getMixerOauth());
		
		HttpResponse res = client.execute(get);
		res.setStatusCode(200);
		String meta = new BasicResponseHandler().handleResponse(res);
		
		System.out.println(meta);
		
		return new JSONObject();
		
	}
	
}
