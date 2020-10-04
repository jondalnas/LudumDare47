package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;

public class HealthUpChest extends Chest {
	public HealthUpChest(Game game, int x, int y) {
		super(game, x, y, 2 + 22 * 32);
	}

	protected void chestContent() {
		game.player.maxHp += 2;
		game.player.hp += 2;
	}
}
