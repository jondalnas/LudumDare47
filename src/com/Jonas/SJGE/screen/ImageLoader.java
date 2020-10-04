package com.Jonas.SJGE.screen;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	public static Bitmap pattern = loadImage("/img/Pattern.png");
	public static Bitmap tilemap = loadImage("/img/Tilemap.png");
	
	private static Bitmap loadImage(String location) {
		BufferedImage bi;
		
		try {
			bi = ImageIO.read(ImageLoader.class.getResource(location));
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
