package me.boops.chatterboops;

import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class SmashEvents extends WebSocketAdapter {
	
	@Override
	public void onWebSocketText(String message){
		
		super.onWebSocketText(message);
		System.out.println("Received TEXT message: " + message);
	}
	
}
