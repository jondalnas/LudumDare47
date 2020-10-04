package com.Jonas.SJGE.screen;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.entity.Enemy;
import com.Jonas.SJGE.entity.Entity;
import com.Jonas.SJGE.screen.gui.GUI;

public class Renderer extends Screen {
	private static final long serialVersionUID = 1L;
	
	private Game game;
	public Renderer(Game game) {
		this.game = game;
		
		addKeyListener(game.input);
	}
	
	public void renderGame() {
		if (game.player != null) {
			game.tilemap.render(this);
		
			for (Entity e : game.entities) {
				if (e instanceof Enemy && ((Enemy) e).isDead()) continue;
				
				e.render(this);
			}
		}
		
		for (GUI gui : game.activeGUI) {
			gui.render(this);
		}
	}
}
