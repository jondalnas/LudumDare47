package com.Jonas.SJGE.tilemap.tiles;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.Screen;

public class Tree extends Tile {
	public Tree(Game game) {
		super(game);
		
		tilemapLocation = 1+1*16;
	}

	public void tick() {
	}

	public void render(Screen screen, int x, int y) {
		super.renderTile(screen, 0, x, y);
		super.renderTile(screen, x, y);
	}
}
