package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;

public class Spider extends Enemy {
	private final double walkTime = 0.064;
	private double walkTimer = walkTime;
	private byte anim;
	
	private final double speed = 40;
	private byte direction;
	
	private final int attackRange = 48;
	private final double nextWanderingTimer = 2;
	private final double nextWanderingTimerRange = 0.5;
	private double nextWanderingTime;
	private double velocityX, velocityY;
	private final double wanderingTimer = 0.5;
	private double wanderingTime;

	public Spider(Game game, int x, int y) {
		super(game, x, y, 1);
		
		sizeD = 8;
	}

	public void update() {
		double xx = game.player.x - x;
		double yy = game.player.y - y;
		
		double distance = xx*xx + yy*yy;
		
		if (distance < attackRange * attackRange) {
			double d = Math.sqrt(distance);
			
			xx /= d;
			yy /= d;
		} else {
			xx = yy = 0;
			
			if ((nextWanderingTime -= Main.getDeltaTime()) < 0) {
				velocityX = (Math.random() * 2.0 - 1.0);
				velocityY = (Math.random() * 2.0 - 1.0);
				
				double d = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
				
				velocityX /= d;
				velocityY /= d;
				
				nextWanderingTime = nextWanderingTimer + (Math.random() * 2.0 - 1.0) * nextWanderingTimerRange;
				
				wanderingTime = wanderingTimer;
			}
			
			if ((wanderingTime -= Main.getDeltaTime()) > 0) {
				xx = velocityX;
				yy = velocityY;
			}
		}
		
		if (xx != 0 || yy != 0) {
			if ((walkTimer -= Main.getDeltaTime()) < 0) {
				walkTimer = walkTime;
				anim++;
				anim %= 2;
			}
			
			if (Math.abs(xx) > Math.abs(yy))
				direction = 1;
			else
				direction = 0;
		}

		dx += xx * Main.getDeltaTime() * speed;
		dy += yy * Main.getDeltaTime() * speed;
		
		move();
	}

	public void render(Screen screen) {
		screen.screen.draw(ImageLoader.tilemap, x - game.cam.x, y - game.cam.y, (6 + direction) * 8, (14 + anim) * 8, 8, 8);
	}

	public boolean collide(Entity e) {
		return true;
	}

	public int getDamage() {
		return 1;
	}

}
