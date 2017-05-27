package me.boops.chatterboops;

import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.json.JSONObject;

import me.boops.chatterboops.parsers.MixerParser;

public class MixerEvents extends WebSocketAdapter {
	
	@Override
	public void onWebSocketText(String message){
		
		super.onWebSocketText(message);
		new MixerParser(new JSONObject(message));
	}
}
