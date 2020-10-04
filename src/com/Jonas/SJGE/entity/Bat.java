package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;

public class Bat extends Enemy {
	private final double flapTime = 0.125;
	private double flapTimer = flapTime;
	private byte anim;
	
	private double speed = 45;

	private final double attackTime = 2;
	private double attackTimer = attackTime;
	private final int attackRadius = 24;
	private byte attackDirection = 1;
	
	public Bat(Game game, int x, int y) {
		super(game, x, y, 2);
		
		sizeD = 8;
	}

	public void update() {
		double xx = game.player.x - x;
		double yy = game.player.y - y;
		
		double distance = xx*xx + yy*yy;
		
		if (distance > WAKEUP_RANGE * WAKEUP_RANGE) return;
		
		if ((flapTimer -= Main.getDeltaTime()) < 0) {
			flapTimer = flapTime;
			anim++;
			anim %= 2;
		}
		
		if (distance > attackRadius * attackRadius || (attackTimer -= Main.getDeltaTime()) < 0) {
			double d = Math.sqrt(distance);
			
			xx /= d;
			yy /= d;
	
			dx += xx * Main.getDeltaTime() * speed;
			dy += yy * Main.getDeltaTime() * speed;
		} else {
			double d = Math.sqrt(distance);
			
			xx /= d;
			yy /= d;
	
			dx += yy * Main.getDeltaTime() * speed * attackDirection;
			dy += xx * Main.getDeltaTime() * speed * -attackDirection;
			
			if (Math.random() < 1e-2)
				attackDirection = (byte) -attackDirection;
		}
		
		move();
	}

	public void render(Screen screen) {
		screen.screen.draw(ImageLoader.tilemap, x - game.cam.x, y - game.cam.y, 4 * 8, (14 + anim) * 8, 8, 8);
	}

	public boolean collide(Entity e) {
		if (e instanceof Slime && !((Slime) e).isJumping())
			return false;
		
		if (e instanceof Spider)
			return false;
		
		if (e instanceof Player)
			attackTimer = attackTime;
		else
			attackDirection = (byte) -attackDirection;
		
		return true;
	}

	public int getDamage() {
		return 1;
	}

}
