package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;

public class Smoke extends Entity {
	private final static double SPEED = 12;
	private final static double SPREAD = 12;
	private final static double MAX_Z = 8;
	
	private double z = 0;
	private int effectIndex;
	private int color;
	
	private double vx;
	
	public Smoke(Game game, int x, int y, int color) {
		super(game, x, y);
		
		this.color = color;
		
		effectIndex = (int) (Math.random() * 4);
		vx = (Math.random() - 0.5) * SPREAD;
	}

	public void update() {
		dx += vx * Main.getDeltaTime();
		
		double dxx = (int) dx;
		dx -= dxx;
		x += dxx;
		
		z += SPEED * Main.getDeltaTime();
		
		if (z > MAX_Z) game.removeEntity(this);
	}

	public void render(Screen screen) {
		screen.screen.drawWithColor(ImageLoader.tilemap, color, x - game.cam.x, y - game.cam.y - (int) z, (4 + effectIndex) * 8, 23 * 8, 8, 8);
	}

	public boolean collide(Entity e) {
		return false;
	}

}
