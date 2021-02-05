package me.enz0z;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import com.github.instagram4j.instagram4j.IGClient;

import me.enz0z.utils.Prop;
import me.enz0z.utils.Utils;

public class Core {

	private static ArrayList<String> Posted = new ArrayList<String>();

	public static void main(String args[]) {
		try {
			new Prop();
			IGClient client = IGClient.builder().username(Prop.getString("Username")).password(Prop.getString("Password")).login();
			
			System.out.println(" ");
			System.out.println("Instagram client has been connected: " + client.getSelfProfile().getUsername());
			System.out.println(" ");
			while (client.isLoggedIn()) {
				for (String subreddit : Prop.getString("Subreddits").split(" ")) {
					String response = Utils.requestURL("https://www.reddit.com/r/" + subreddit + "/new.json?limit=1");
					JSONObject json = new JSONObject(response).getJSONObject("data").getJSONArray("children").getJSONObject(0).getJSONObject("data");
					String permalink = "https://reddit.com" + json.getString("permalink");
					
					if (json.isNull("post_hint")) {
						System.out.println("Found a nulled post_hint post: " + permalink);
						System.out.println(" ");
					} else if (json.getBoolean("over_18")) {
						System.out.println("Found an over_18 post: " + permalink);
						System.out.println(" ");
					} else if (!Posted.contains(json.getString("id")) && json.getString("post_hint").equals("image")) {
						System.out.println("Found a new post: " + permalink);
						URL url = new URL(json.getString("url"));
						BufferedImage image = ImageIO.read(url);
						File temp = File.createTempFile("temp", ".jpg");

						Posted.add(json.getString("id"));
						ImageIO.write(Utils.resizeImage(image, 600, 600), "jpg", temp);
						client.actions().timeline().uploadPhoto(temp, json.getString("title") + "\n\n" + json.getString("selftext") + "\n\n🔗 " + permalink + "\n.\n.\n.\n.\n.\n" + Prop.getString("Tags")).thenAccept(res -> {
							System.out.println("Published a new post: " + permalink);
							System.out.println(" ");
						}).exceptionally(tr -> {
						    tr.printStackTrace();
						    return null;
						}).join();
						temp.delete();
						image.flush();
					}
					Thread.sleep(15000);
				}
			}
			throw new Exception("Instagram client has been disconnected: " + client.getSelfProfile().getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}