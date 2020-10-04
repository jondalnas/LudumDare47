package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;

public class Number extends Entity {
	private final static double SPEED = 40;
	private final static double ZSPEED = 6;
	private final static double AMPLITUDE = 8;
	
	private int color;
	private int number;
	private int z;
	private double time;
	private int digits;
	private double vx, vy;
	
	public Number(Game game, int x, int y, int number, double dx, double dy, int color) {
		super(game, x, y);
		
		this.vx = dx * SPEED;
		this.vy = dy * SPEED;
		
		this.color = color;
		this.number = number;
		
		digits = (int) Math.log10(number);
	}

	public void update() {
		dx += vx * Main.getDeltaTime();
		dy += vy * Main.getDeltaTime();
		
		double dxx = (int) dx;
		double dyy = (int) dy;

		dx -= dxx;
		dy -= dyy;
		
		x += dxx;
		y += dyy;
		
		time += Main.getDeltaTime();
		z = (int) (Math.sin(ZSPEED * time) * AMPLITUDE);
		
		if (z < 0) game.removeEntity(this);
	}
	
	public void render(Screen screen) {
		int number = this.number;
		for (int i = digits; i >= 0; i--) {
			byte digit = (byte) (number % 10);
			screen.screen.drawWithColor(ImageLoader.tilemap, 0, x - game.cam.x + i * 8, y - game.cam.y, (54 + digit) * 4, 61 * 4, 4, 4);
			screen.screen.drawWithColor(ImageLoader.tilemap, color, x - game.cam.x + i * 8, y - game.cam.y - z, (54 + digit) * 4, 61 * 4, 4, 4);
			number /= 10;
		}
	}

	public boolean collide(Entity e) {
		return false;
	}
}
