package com.Jonas.SJGE.tilemap.tiles;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.Screen;

public class Floor extends Tile {
	public Floor(Game game) {
		super(game);
		
		tilemapLocation = 0+1*16;
	}

	public void tick() {

	}

	public void render(Screen screen, int x, int y) {
		renderTile(screen, x, y);
	}
}