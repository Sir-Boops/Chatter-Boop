package me.boops.chatterboops;

import me.boops.chatterboops.Mixer.Mixer;
import me.boops.chatterboops.Twitch.Twitch;

public class Main {
	
	
	// Load the config
	public static Config conf = new Config();
	
	public static void main(String[] args) throws Exception {
		
		// The main class inits all the chats
		
		// Launch Twitch sub thread
		new Thread(new Runnable(){
			
			public void run(){
				
				try {
					
					new Twitch();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
		// Launch mixer sub thread
		new Thread(new Runnable(){
			
			public void run(){
				
				try {
					
					new Mixer();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}
}
