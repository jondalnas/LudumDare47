package com.Jonas.SJGE.tilemap.tiles;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.Screen;

public class Gravel extends Tile {

	public Gravel(Game game) {
		super(game);
		
		tilemapLocation = 1+0*16;
	}

	public void tick() {
	}

	public void render(Screen screen, int x, int y) {
		renderTile(screen, x, y);
	}
}
