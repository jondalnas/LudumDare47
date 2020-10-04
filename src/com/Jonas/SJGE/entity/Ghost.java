package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.sound.Sound;

public class Ghost extends Enemy {
	private final static double BOBBING_SPEED = 1.5;
	private final static double BOBBING_HEIGHT = 2;
	private final static double SPEED = 30;
	private double bobbingTime;

	private final static double DISAPEAR_TIMER = 10;
	private double disapearTime = DISAPEAR_TIMER;
	private final static double ATTACK_RADIUS = 48;
	
	private boolean awoken;
	
	private int direction;
	
	private boolean disapeard;
	
	public Ghost(Game game, int x, int y) {
		super(game, x, y, 6, null, Sound.GHOST_DIE);
	}

	public void update() {
		bobbingTime += Main.getDeltaTime();
		
		double xx = game.player.x - x;
		double yy = game.player.y - y;
		
		double distance = xx*xx + yy*yy;
		
		if (distance < WAKEUP_RANGE * WAKEUP_RANGE) awoken = true;
		
		if (!awoken) return;
		
		if ((disapearTime -= Main.getDeltaTime()) > 0) {
			double d = Math.sqrt(distance);
			
			xx /= d;
			yy /= d;
	
			dx += xx * Main.getDeltaTime() * SPEED;
			dy += yy * Main.getDeltaTime() * SPEED;
			
			if (Math.abs(xx) > Math.abs(yy)) {
				if (xx > 0) {
					direction = 2;
				} else {
					direction = 3;
				}
			} else {
				if (yy > 0) {
					direction = 0;
				} else {
					direction = 1;
				}
			}
			
			if (disapearTime < 0.3 && !disapeard) {
				disapeard = true;
				if (hp > 0) Sound.GHOST_VANISH.play();
			}
			
			move();
		} else if (disapearTime < -DISAPEAR_TIMER / 2) {
			xx = Math.random();
			yy = Math.random();

			double d = Math.sqrt(xx*xx + yy*yy);
			
			xx /= d;
			yy /= d;

			xx *= ATTACK_RADIUS;
			yy *= ATTACK_RADIUS;

			xx += game.player.x;
			yy += game.player.y;
			
			x = (int) xx;
			y = (int) yy;
			
			disapearTime = DISAPEAR_TIMER;
			
			disapeard = false;
			
			if (hp > 0) Sound.GHOST_VANISH.play();
		}
	}

	public void render(Screen screen) {
		if (disapearTime < 0) return;
		
		int anim = 0;
		
		if (disapearTime < 0.1) {
			anim = 3;
		} else if (disapearTime < 0.2) {
			anim = 2;
		} else if (disapearTime < 0.3) {
			anim = 1;
		} else if (disapearTime > DISAPEAR_TIMER - 0.1) {
			anim = 3;
		} else if (disapearTime > DISAPEAR_TIMER - 0.2) {
			anim = 2;
		} else if (disapearTime > DISAPEAR_TIMER - 0.3) {
			anim = 1;
		}
		
		screen.screen.draw(ImageLoader.tilemap, x - game.cam.x, y - game.cam.y + (int) (Math.sin(bobbingTime * BOBBING_SPEED) * BOBBING_HEIGHT), (direction + 4) * 16, (8 + anim) * 16, 16, 16);
	}

	public boolean collide(Entity e) {
		if (e instanceof Player && disapearTime > 0.3) return true;
		
		return false;
	}

	public int getDamage() {
		if (disapearTime < 0.3) return 0;
		
		return 3;
	}
	
	public void damage(int damage) {
		if (disapearTime < 0.3) return;
		
		super.damage(damage);
		
		disapearTime = 0.3;
	}
	
	public boolean isDead() {
		if (disapearTime < 0)
			return dead;
		
		return false;
	}
}
