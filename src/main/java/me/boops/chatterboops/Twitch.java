package me.boops.chatterboops;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import me.boops.chatterboops.parsers.TwitchParser;

public class Twitch {
	
	private BufferedWriter writeStream;
	
	public Twitch(Config conf) throws Exception{
		
		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket sock = (SSLSocket) sslsocketfactory.createSocket("irc.chat.twitch.tv", 443);
		
		writeStream = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		BufferedReader readStream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		
		sendMSG("PASS " + conf.getTwitchOauth());
		sendMSG("NICK " + conf.getTwitchOauth());
		
		sendMSG("JOIN #sir_boops");
					
		String line = null;
		while((line = readStream.readLine()) != null){
			new TwitchParser(line.toString(), this);
		}		
	}
	
	
	// Write to chat
	public void sendMSG(String msg) throws Exception{
			
		writeStream.write(msg + "\r\n");
		writeStream.flush();
		
	}
}
