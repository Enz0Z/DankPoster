package me.enz0z;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.requests.media.MediaEditRequest;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import me.enz0z.utils.Prop;
import me.enz0z.utils.Utils;

public class Core {

	private static ArrayList<String> Posted = new ArrayList<String>();

	public static void main(String args[]) throws IGLoginException {
		new Prop();
		IGClient client = IGClient.builder().username(Prop.getString("Username")).password(Prop.getString("Password")).login();

		System.out.println(" ");
		System.out.println("Instagram client has been connected: " + client.getSelfProfile().getUsername());
		System.out.println(" ");
		while (client.isLoggedIn()) {
			try {
				for (String subreddit : Prop.getString("Subreddits").split(" ")) {
					Thread.sleep(15000);
					HttpResponse<String> response = Unirest.get("https://www.reddit.com/r/" + subreddit + "/new.json?limit=1").asString();
					JSONObject json = new JSONObject(response.getBody()).getJSONObject("data").getJSONArray("children").getJSONObject(0).getJSONObject("data");
					String permalink = "https://reddit.com" + json.getString("permalink");

					if (json.isNull("post_hint") || json.getBoolean("over_18")) continue;
					if (!Posted.contains(json.getString("id")) && json.getString("post_hint").equals("image")) {
						System.out.println("Found a new post: " + permalink);
						URL url = new URL(json.getString("url"));
						BufferedImage image = ImageIO.read(url);
						File temp = File.createTempFile("temp", ".jpg");
						String caption = json.getString("title") + "\n\n" + json.getString("selftext") + "\n\n🔗 " + permalink + "\n.\n.\n.\n.\n.\n" + Prop.getString("Tags");

						Posted.add(json.getString("id"));
						ImageIO.write(Utils.resizeImage(image, 600, 600), "jpg", temp);
						client.actions().timeline().uploadPhoto(temp, caption).thenAccept(res -> {
							if (res.getMedia().getCaption().getText().length() == 0) {
								new MediaEditRequest(res.getMedia().getId(), caption).execute(client);
							}
							System.out.println("Published a new post: " + res.getMedia().getId());
							System.out.println("Caption length in this post: " + res.getMedia().getCaption().getText().length());
							System.out.println(" ");
						}).exceptionally(tr -> {
						    tr.printStackTrace();
						    return null;
						}).join();
						temp.delete();
						image.flush();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(" ");
		System.out.println("Instagram client has been disconnected: " + client.getSelfProfile().getUsername());
		System.out.println(" ");
	}
}