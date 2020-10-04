package com.Jonas.SJGE.tilemap.tiles;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.Screen;

public class Wall extends Tile {
	private byte type = -1;
	
	private byte decoration;
	
	public Wall(Game game) {
		super(game);
		
		solid = true;
		
		decoration = (byte) ((Math.random() * 4) % 3);
	}

	public void tick() {
	}

	public void render(Screen screen, int x, int y) {
		if (type == -1) {
			type = 0;
			if (game.tilemap.getTile(x, y+1) instanceof Floor) {
				type |= 0b00000001;
			}
			if (game.tilemap.getTile(x, y-1) instanceof Floor) {
				type |= 0b00000010;
			}
			if (game.tilemap.getTile(x+1, y) instanceof Floor) {
				type |= 0b00000100;
			}
			if (game.tilemap.getTile(x-1, y) instanceof Floor) {
				type |= 0b00001000;
			}
			
			if (game.tilemap.getTile(x, y+1) instanceof Wall) {
				if (game.tilemap.getTile(x+1, y) instanceof Wall && game.tilemap.getTile(x+1, y+1) instanceof Floor) {
					type |= 0b00000100;
				}
				if (game.tilemap.getTile(x-1, y) instanceof Wall && game.tilemap.getTile(x-1, y+1) instanceof Floor) {
					type |= 0b00001000;
				}
			}
			
			if (game.tilemap.getTile(x, y-1) instanceof Wall) {
				if (game.tilemap.getTile(x+1, y) instanceof Wall) {
					type |= 0b00010000;
				}
				if (game.tilemap.getTile(x-1, y) instanceof Wall) {
					type |= 0b00100000;
				}
			}
		}
		
		if ((type & 0b00000010) != 0) {
			renderTile(screen, 4, x, y);
		}
		
		if ((type & 0b00000100) != 0) {
			renderTile(screen, 3, x, y);
		}
		
		if ((type & 0b00001000) != 0) {
			renderTile(screen, 5, x, y);
		}
		
		if ((type & 0b00010000) != 0) {
			renderTile(screen, 7, x, y);
		}
		
		if ((type & 0b00100000) != 0) {
			renderTile(screen, 6, x, y);
		}
		
		if ((type & 0b00000001) != 0) {
			renderTile(screen, 0 + decoration, x, y);
		}
	}
}