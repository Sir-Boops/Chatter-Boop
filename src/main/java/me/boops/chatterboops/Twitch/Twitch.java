package me.boops.chatterboops.Twitch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import me.boops.chatterboops.Config;
import me.boops.chatterboops.parsers.TwitchParser;

public class Twitch {
	
	private static BufferedWriter writeStream;
	
	public Twitch(Config conf) throws Exception{
		
		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket sock = (SSLSocket) sslsocketfactory.createSocket("irc.chat.twitch.tv", 443);
		
		writeStream = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		BufferedReader readStream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		
		sendRaw("PASS " + conf.getTwitchOauth());
		sendRaw("NICK " + conf.getTwitchOauth());
		
		sendRaw("JOIN #sir_boops");
		sendRaw("CAP REQ :twitch.tv/tags");
					
		String line = null;
		while((line = readStream.readLine()) != null){
			new TwitchParser(line.toString());
		}		
	}
	
	
	// Write to chat
	public static void sendRaw(String msg) throws Exception{
			
		writeStream.write(msg + "\r\n");
		writeStream.flush();
		
	}
	
	// Write to chat
	public static void sendMSG(String msg) throws Exception{
			
		writeStream.write("PRIVMSG #sir_boops" + msg + "\r\n");
		writeStream.flush();
		
	}
}
