package com.Jonas.SJGE.tilemap.tiles;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.Screen;

public class Rock extends Tile {

	public Rock(Game game) {
		super(game);
		tilemapLocation = 0+1*16;
		solid = true;
	}

	public void tick() {
	}

	public void render(Screen screen, int x, int y) {
		renderTile(screen, 0, x, y);
		renderTile(screen, x, y);
	}
}
