package com.Jonas.SJGE.entity;

import java.util.List;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.Screen;

public class Player extends Entity {
	private final int speed = 60;

	public Player(Game game, int x, int y) {
		super(game);
		
		sizeD = 16;
		
		this.x = x;
		this.y = y;
	}

	public void update() {
		dx = game.input.getKeyDown('d') ? 1 : (game.input.getKeyDown('a') ? -1 : 0);
		dy = game.input.getKeyDown('s') ? 1 : (game.input.getKeyDown('w') ? -1 : 0);
		
		dx *= Math.round(speed * Main.getDeltaTime());
		dy *= Math.round(speed * Main.getDeltaTime());
		
		move();
		
		if (game.input.getKeyDown('n')) {
			List<Entity> entitiesInside = game.getEntitiesInside(x+xColOffs-16, y+yColOffs-16, x+xColOffs+sizeD+16, y+yColOffs+sizeD+16);
			
			for (Entity e : entitiesInside) {
				if (e == this) continue;
				
				game.removeEntity(e);
			}
		}
		
		game.cam.x = x - game.cam.xColOffs;
		game.cam.y = y - game.cam.yColOffs;
	}

	public void render(Screen screen) {
		screen.screen.draw(0xff00ff, Screen.WIDTH / 2 - sizeD / 2, Screen.HEIGHT / 2 - sizeD / 2, sizeD, sizeD);
	}

	public boolean collide(Entity e) {
		return true;
	}
}
