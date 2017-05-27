package me.boops.chatterboops.parsers;

public class TwitchParser {
	
	public TwitchParser(String msg, Object twitch) throws Exception{
		
		// Check if string is a message
		if(msg.contains("PRIVMSG")){
			
			String[] msgSplit = msg.split(" ");
			
			String userName = msgSplit[msgSplit.length - 2].replaceFirst("#", "");
			String userMSG = msgSplit[msgSplit.length - 1].replaceFirst(":", "");
			
			System.out.println(userName + " : " + userMSG);
			
			
		}
		System.out.println(msg);
	}
}
