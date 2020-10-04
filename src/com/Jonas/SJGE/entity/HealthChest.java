package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;

public class HealthChest extends Chest {
	public HealthChest(Game game, int x, int y) {
		super(game, x, y, 0 + 22 * 32);
	}

	protected void chestContent() {
		game.player.hp += 2;
		
		if (game.player.hp > game.player.maxHp) game.player.hp = game.player.maxHp;
	}
}
