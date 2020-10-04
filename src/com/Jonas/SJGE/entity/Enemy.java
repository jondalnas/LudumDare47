package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.sound.Sound;

public abstract class Enemy extends Entity {
	protected final static int WAKEUP_RANGE = 160;
	
	private final static double KNOCKBACK = 220;
	private final static double KNOCKBACK_TIMER = 0.1;
	protected double knockbackTime;
	protected double hurtVelocityX, hurtVelocityY;
	
	private Sound damageSound, dieSound;
	
	protected int hp;
	protected boolean dead;
	
	public Enemy(Game game, int x, int y, int hp, Sound damageSound, Sound dieSound) {
		super(game, x, y);
		
		this.hp = hp;
		this.damageSound = damageSound;
		this.dieSound = dieSound;
	}
	
	public void damage(int damage) {
		if (hp <= 0) return;
		
		hp -= damage;
		
		if (hp <= 0)  {
			dead = true;
			dieSound.play();
			
			if (!(this instanceof Ghost)) game.addEntity(new Smoke(game, x, y, 0xdddddd));
		} else if (damageSound != null)
			damageSound.play();

		double xx = x - game.player.x;
		double yy = y - game.player.y;
		
		double d = Math.sqrt(xx*xx + yy*yy);
		
		xx /= d;
		yy /= d;

		hurtVelocityX = xx * KNOCKBACK * Main.getDeltaTime();
		hurtVelocityY = yy * KNOCKBACK * Main.getDeltaTime();
		
		knockbackTime = KNOCKBACK_TIMER;
		
		int color = (int) ((hp / 5.0) * 0xff);
		if (color > 0xff) color = 0xff;

		game.addEntity(new Number(game, x, y, damage, xx, yy, 0xff0000 | (color << 8) | (color)));
	}
	
	public boolean isDead() {
		return dead;
	}

	public abstract void update();

	public abstract void render(Screen screen);

	public abstract boolean collide(Entity e);

	public abstract int getDamage();
}
