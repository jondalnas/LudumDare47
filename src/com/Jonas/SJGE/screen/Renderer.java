package com.Jonas.SJGE.screen;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.entity.Entity;

public class Renderer extends Screen {
	private static final long serialVersionUID = 1L;
	
	private Game game;
	public Renderer(Game game) {
		this.game = game;
		
		addKeyListener(game.input);
	}
	
	public void renderGame() {
		game.tilemap.render(this);
		
		for (Entity e : game.entities) {
			e.render(this);
		}
		
		game.cam.render(this);
	}
}
