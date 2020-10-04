package com.Jonas.SJGE.screen.gui;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;
import com.sun.glass.events.KeyEvent;

public class HUD extends GUI {

	private Game game;
	public HUD(Game game) {
		this.game = game;
	}
	
	public void update() {
		if (game.player.hp <= 0 && game.input.getButtonPressed(KeyEvent.VK_SPACE)) {
			game.addGUI(new StartScreen(game));
			game.removeGUI(this);
			game.player = null;
		}
	}

	public void render(Screen screen) {
		//Health
		int hp = game.player.hp;
		for (int i = 0; i < game.player.maxHp >> 1; i++) {
			if (hp >= 2) {
				screen.screen.draw(ImageLoader.tilemap, 2 + i * 10, 2, 0, 22 * 8, 8, 8);
				hp -= 2;
			} else if (hp == 1) {
				screen.screen.draw(ImageLoader.tilemap, 2 + i * 10, 2, 8, 22 * 8, 8, 8);
				hp--;
			} else {
				screen.screen.draw(ImageLoader.tilemap, 2 + i * 10, 2, 16, 22 * 8, 8, 8);
			}
		}
		
		//Floor
		screen.screen.draw(ImageLoader.tilemap, Screen.WIDTH - 8 * 8 - 2 - 4, 2, 22 * 8, 30 * 8, 5 * 8, 8);
		
		int floor = game.currentFloor;
		for (int i = 2; i >= 0; i--) {
			byte digit = (byte) (floor % 10);
			screen.screen.draw(ImageLoader.tilemap, Screen.WIDTH - 3 * 8 - 2 + i * 8, 2, (22 + digit) * 8, 31 * 8, 8, 8);
			floor /= 10;
		}
		
		//Dead
		if (game.player.hp <= 0) {
			for (int i = 0; i < screen.screen.pixels.length; i++) {
				int r = (screen.screen.pixels[i] >> 16) & 0xff;
				int g = (screen.screen.pixels[i] >> 8 ) & 0xff;
				int b = (screen.screen.pixels[i]	  ) & 0xff;

				r *= .3;
				g *= .3;
				b *= .3;
				
				screen.screen.pixels[i] = (r << 16) | (g << 8) | b;
			}
			
			screen.screen.draw(ImageLoader.tilemap, Screen.WIDTH / 2 - 7 * 8 / 2, Screen.HEIGHT / 2 - 8 / 2, 22 * 8, 27 * 8, 8 * 8, 8);
		}
	}
}
