package com.Jonas.SJGE.tilemap.tiles;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.Screen;

public class Grass extends Tile {
	public Grass(Game game) {
		super(game);
		tilemapLocation = 0;
	}

	public void tick() {
	}

	public void render(Screen screen, int x, int y) {
		renderTile(screen, x, y);
	}
}
