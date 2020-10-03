package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.Screen;

public class Camera extends Entity {
	public Camera(Game game) {
		super(game);
		
		sizeD = 16;

		xColOffs = (int) (Screen.WIDTH  / 2 - sizeD / 2.0);
		yColOffs = (int) (Screen.HEIGHT / 2 - sizeD / 2.0);
	}

	public void update() {
	}
	
	public boolean collide(Entity e) {
		return false;
	}

	public void render(Screen screen) {
	}
}
