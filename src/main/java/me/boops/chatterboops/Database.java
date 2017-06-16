package me.boops.chatterboops;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import org.json.JSONObject;

public class Database {
	
	
	private String dbPath;
	private static JSONObject db;
	
	public Database(String databaseName) throws Exception {
		
		String currentDir = new File(".").getCanonicalPath() + "/";
		
		// Check if the file is already there or not
		if(new File(currentDir + "DBs").exists()){
			
			// Check if it's also a folder
			if(new File(currentDir + "DBs").isDirectory()){
				
				// Now check to see if the DB is there
				if(new File(currentDir + "DBs/" + databaseName + ".json").exists()){
					
					// Now make sure it's a file
					if(new File(currentDir + "DBs/" + databaseName + ".json").isFile()){
						
						// Perfect! now load it!
						loadDB(currentDir, databaseName);
						
					}
					
				} else {
					initDBSetup(currentDir, databaseName);
				}
				
			}
			
		} else {
			
			// Create a new folder and DB
			new File(currentDir + "DBs").mkdir();
			
			initDBSetup(currentDir, databaseName);
		}
		
	}
	
	private void initDBSetup(String path, String dbName) throws Exception {
		
		PrintWriter out = new PrintWriter(path + "DBs/" + dbName + ".json");
		
		db = new JSONObject();
		
		out.print(db);
		out.flush();
		out.close();
		
		loadDB(path, dbName);
		
	}
	
	private void loadDB(String path, String dbName) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		
		// Read The Config File
		BufferedReader br = new BufferedReader(new FileReader(path + "DBs/" + dbName + ".json"));
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			line = br.readLine();
		}
		br.close();
		
		db = new JSONObject(sb.toString());
		dbPath = (path + "DBs/" + dbName + ".json");
		
		
	}
	
	private void saveDB() throws Exception {
		
		PrintWriter out = new PrintWriter(dbPath);
		
		out.print(db);
		out.flush();
		out.close();
		
	}
	
	
	public void setEntry(String name, Object body) throws Exception {
		
		db.put(name, body);
		saveDB();
		
	}
	
	public void delEntry(String name) throws Exception {
		
		db.remove(name);
		saveDB();
		
	}
	
	public Object getEntry(String name){
		
		// Check if real
		if(db.has(name)){
			return db.get(name);
		} else {
			return null;
		}
		
	}
	
	
}
