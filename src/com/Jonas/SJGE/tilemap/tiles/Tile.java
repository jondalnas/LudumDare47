package com.Jonas.SJGE.tilemap.tiles;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.tilemap.Tilemap;

public abstract class Tile {
	protected int tilemapLocation;
	protected Game game;
	
	public boolean solid = false;
	
	public Tile(Game game) {
		this.game = game;
	}
	
	public abstract void tick();
	public abstract void render(Screen screen, int x, int y);

	protected void renderTile(Screen screen, int x, int y) {
		renderTile(screen, tilemapLocation, x, y);
	}
	protected void renderTile(Screen screen, int tilemapLocation, int x, int y) {
		screen.screen.draw(ImageLoader.tilemap, x * Tilemap.TILE_SIZE - game.cam.x, y * Tilemap.TILE_SIZE - game.cam.y, (tilemapLocation % 16) * Tilemap.TILE_SIZE, (tilemapLocation / 16) * Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
	}
}
