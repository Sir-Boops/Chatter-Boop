package me.boops.chatterboops;

import me.boops.chatterboops.Mixer.Mixer;
import me.boops.chatterboops.Smash.Smash;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		// Load the config
		Config conf = new Config();
		
		// The main class inits all the chats
		
		// Launch the Twitch client
		new Thread(new Runnable(){
			
			public void run(){
				
				try {

					//new Twitch(conf);
					
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}).start();
		
		// Launch the mixer client
		new Thread(new Runnable(){
			
			public void run(){
				
				try {
					
					new Mixer(conf);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
		// Launch the Smash client
		new Thread(new Runnable(){
			
			public void run(){
				
				try {
					
					new Smash(conf);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}
}
