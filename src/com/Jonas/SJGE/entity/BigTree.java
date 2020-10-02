package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;

public class BigTree extends Entity {
	public BigTree(Game game, int x, int y) {
		super(game);
		this.x = x;
		this.y = y;
		sizeD = 32;
	}
	
	public boolean collide(Entity e) {
		return true;
	}

	public void update() {
		
	}

	public void render(Screen screen) {
		screen.screen.draw(ImageLoader.tilemap, x - game.cam.x, y - game.cam.y, 32, 32, 16, 16, 16, 16);
	}
}