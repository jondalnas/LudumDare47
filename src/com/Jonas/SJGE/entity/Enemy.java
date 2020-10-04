package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.Screen;

public abstract class Enemy extends Entity {
	protected final static int WAKEUP_RANGE = 160;
	
	private int hp;
	private boolean dead;
	
	public Enemy(Game game, int x, int y, int hp) {
		super(game, x, y);
		
		this.hp = hp;
	}
	
	public void damage(int damage) {
		hp -= damage;
		
		if (hp <= 0) dead = true;
	}
	
	public boolean isDead() {
		return dead;
	}

	public abstract void update();

	public abstract void render(Screen screen);

	public abstract boolean collide(Entity e);

	public abstract int getDamage();
}
