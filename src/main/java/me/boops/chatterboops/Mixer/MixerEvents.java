package me.boops.chatterboops.Mixer;

import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import me.boops.chatterboops.parsers.MixerParser;

public class MixerEvents extends WebSocketAdapter {
	
	@Override
	public void onWebSocketText(String message){
		
		super.onWebSocketText(message);
		try {
			System.out.print(message);
			new MixerParser(new JSONObject(message));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
