package com.Jonas.SJGE.entity;

import java.util.List;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.Screen;

public class Camera extends Entity {
	private final int CAM_SPEED = 60;

	public Camera(Game game) {
		super(game);

		xColOffs = (int) (Screen.WIDTH  / 2 - sizeD / 2.0);
		yColOffs = (int) (Screen.HEIGHT / 2 - sizeD / 2.0);

		x = -xColOffs;
		y = -yColOffs;
	}

	public void update() {
		dx = game.input.getKeyDown('d') ? 1 : (game.input.getKeyDown('a') ? -1 : 0);
		dy = game.input.getKeyDown('s') ? 1 : (game.input.getKeyDown('w') ? -1 : 0);
		
		dx *= Math.round(CAM_SPEED * Main.getDeltaTime());
		dy *= Math.round(CAM_SPEED * Main.getDeltaTime());
		
		move();
		
		if (game.input.getKeyDown('n')) {
			List<Entity> entitiesInside = game.getEntitiesInside(x+xColOffs-16, y+yColOffs-16, x+xColOffs+sizeD+16, y+yColOffs+sizeD+16);
			
			for (Entity e : entitiesInside) {
				if (e == this) continue;
				
				game.removeEntity(e);
			}
		}
	}
	
	public boolean collide(Entity e) {
		return true;
	}

	public void render(Screen screen) {
		screen.screen.draw(0xff00ff, xColOffs, yColOffs, sizeD, sizeD);
	}
}
