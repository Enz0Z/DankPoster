package me.enz0z.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Utils {
	
	public static BufferedImage resizeImage(BufferedImage image, int width, int height) throws IOException {
	    BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();

	    graphics2D.drawImage(image, 0, 0, width, height, null);
	    graphics2D.dispose();
	    return resizedImage;
	}
}