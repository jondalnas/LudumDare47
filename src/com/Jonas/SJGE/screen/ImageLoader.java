package com.Jonas.SJGE.screen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	public static Bitmap pattern = loadImage("res/img/Pattern.png");
	public static Bitmap tilemap = loadImage("res/img/Tilemap.png");
	
	private static Bitmap loadImage(String location) {
		BufferedImage bi;
		
		try {
			bi = ImageIO.read(new File(location));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Bitmap img = new Bitmap(bi.getWidth(), bi.getHeight());
		
		bi.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
		
		for (int i = 0; i < img.pixels.length; i++) {
			img.pixels[i] = img.pixels[i] & 0xffffff;
		}
		
		return img;
	}
}
