package com.Jonas.SJGE.tilemap.tiles;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.entity.Entity;
import com.Jonas.SJGE.entity.Player;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.sound.Sound;

public class Stairwell extends Tile {
	public Stairwell(Game game) {
		super(game);
		
		tilemapLocation = 0+2*16;
	}
	
	public boolean isSolid(Entity e) {
		if (e instanceof Player) {
			game.nextLevel();
			Sound.WIN.play();
		}
		
		return super.isSolid(e);
	}

	public void tick() {
	}
	
	public void render(Screen screen, int x, int y) {
		renderTile(screen, x, y);
	}
}