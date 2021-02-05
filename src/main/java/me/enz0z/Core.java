package me.enz0z;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class Core {

	public static void main(String args[]) {
		
	}
	
	public static void call_me() throws Exception {
	     URL obj = new URL("https://www.reddit.com/r/argentina/new.json?limit=3");
	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	     con.setRequestMethod("GET");
	     con.setRequestProperty("User-Agent", "DankPoster/1.0");
	     BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	     String inputLine;
	     StringBuffer response = new StringBuffer();

	     while ((inputLine = in.readLine()) != null) {
	     	response.append(inputLine);
	     }
	     in.close();
	     JSONObject json = new JSONObject(response.toString());
	     
	     json.get
	     for (JSONObject obj : json.getJSONObject("data.children").keys()) {
	    	 
	     }
	}
}