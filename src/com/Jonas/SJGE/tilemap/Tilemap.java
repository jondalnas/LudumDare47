package com.Jonas.SJGE.tilemap;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.tilemap.tiles.Grass;
import com.Jonas.SJGE.tilemap.tiles.Tile;
import com.Jonas.SJGE.tilemap.tiles.Tree;

public class Tilemap {
	private final Tile[] tilemap;
	private final int width, height;
	public static final int TILE_SIZE = 16;
	
	private Game game;
	
	public static Tile OOB_TILE;
	
	public Tilemap(Game game) {
		OOB_TILE = new Tree(game);
		
		this.game = game;
		
		TilemapLoader.LoadedMap loadedMap = TilemapLoader.load("res/lvl/level 1.png", game);
		tilemap = loadedMap.getTilemap();
		width = loadedMap.getWidth();
		height = loadedMap.getHeight();
	}
	
	public void tick() {
		for (int i = 0; i < width * height; i++) {
			tilemap[i].tick();
		}
	}
	
	public void render(Screen screen) {
		int x0 = game.cam.x / TILE_SIZE - 1;
		int y0 = game.cam.y / TILE_SIZE - 1;
		int x1 = (game.cam.x + Screen.WIDTH) / TILE_SIZE + 1;
		int y1 = (game.cam.y + Screen.HEIGHT) / TILE_SIZE + 1;
		
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(screen, x, y);
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) return OOB_TILE;
		return tilemap[x + y * width];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
