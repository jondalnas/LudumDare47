package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.sound.Sound;

public abstract class Chest extends Entity {
	private boolean opened;
	private static final double OPEN_TIMER = 0.5;
	private static final int ICON_SPEED = 14;
	private double openTime = 0;
	private int icon;
	
	public Chest(Game game, int x, int y, int icon) {
		super(game, x, y);
		this.icon = icon;
	}

	public void update() {
		openTime -= Main.getDeltaTime();
	}

	public void render(Screen screen) {
		screen.screen.draw(ImageLoader.tilemap, x - game.cam.x, y - game.cam.y, (1 + (opened ? 1 : 0)) * 16, 2 * 16, 16, 16);
		
		if (openTime > 0) {
			double location = (OPEN_TIMER - (openTime * 2 - OPEN_TIMER)) / OPEN_TIMER;
			
			if (location > 1) location = 1;
			
			screen.screen.draw(ImageLoader.tilemap, x - game.cam.x + 4, y - game.cam.y - (int) (location * ICON_SPEED), (icon % 32) * 8, (icon / 32) * 8, 8, 8);
		}
	}

	public boolean collide(Entity e) {
		if (!opened && e instanceof Player) {
			chestContent();
			opened = true;
			openTime = OPEN_TIMER;
			
			Sound.ITEM.play();
		}
		
		return true;
	}
	
	protected abstract void chestContent();
}
