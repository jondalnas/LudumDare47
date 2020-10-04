package com.Jonas.SJGE.screen.gui;

import java.awt.event.KeyEvent;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.sound.Sound;

public class StartScreen extends GUI {
	private Game game;
	private boolean cursor;
	
	public StartScreen(Game game) {
		this.game = game;
	}
	
	public void update() {
		if (game.input.getButtonPressed(KeyEvent.VK_W) || game.input.getButtonPressed(KeyEvent.VK_UP) || 
			game.input.getButtonPressed(KeyEvent.VK_S) || game.input.getButtonPressed(KeyEvent.VK_DOWN)) {
			Sound.SELECT.play();
			cursor = !cursor;
		}
		
		
		if (game.input.getButtonPressed(KeyEvent.VK_SPACE)) {
			Sound.CHOOSE.play();
			if (!cursor) {
				game.startGame();
				game.removeGUI(this);
			} else {
				System.exit(0);
			}
		}
	}

	public void render(Screen screen) {
		screen.screen.fill(0x444444, 0, 0, Screen.WIDTH, Screen.HEIGHT);
		
		screen.screen.draw(ImageLoader.tilemap, Screen.WIDTH / 2 - 80 / 2, Screen.HEIGHT / 4 - 48 / 2, 11 * 16, 10 * 16, 80, 48);
		
		screen.screen.drawWithColor(ImageLoader.tilemap, cursor ? 0xffffff : 0xff0000, Screen.WIDTH / 2 - 5 * 8 / 2, Screen.HEIGHT / 2 + 8 / 2, 22 * 8, 29 * 8, 5 * 8, 8); //START
		screen.screen.drawWithColor(ImageLoader.tilemap, cursor ? 0xff0000 : 0xffffff, Screen.WIDTH / 2 - 4 * 8 / 2, Screen.HEIGHT / 2 + 8 / 2 + 10, 22 * 8, 28 * 8, 4 * 8, 8); //EXIT
	}
}