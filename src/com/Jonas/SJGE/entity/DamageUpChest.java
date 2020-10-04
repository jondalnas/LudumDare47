package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;

public class DamageUpChest extends Chest {
	public DamageUpChest(Game game, int x, int y) {
		super(game, x, y, 0 + 23 * 32);
	}

	protected void chestContent() {
		game.player.damageModefier++;
	}
}
