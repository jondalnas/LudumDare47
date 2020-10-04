package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;

public class Slime extends Enemy {
	private final double jumpTime = 1.5;
	private double jumpTimer = jumpTime;
	private final double jumpSpeed = 120;
	private final int jumpHeight = 3;
	private byte direction;
	
	private double velocityX, velocityY;
	
	public Slime(Game game, int x, int y) {
		super(game, x, y, 2);
		
		sizeD = 8;
		
		dx = dy = 0;
	}

	public void update() {
		double xx = game.player.x - x;
		double yy = game.player.y - y;
		
		double distance = xx*xx + yy*yy;
		
		if (distance > WAKEUP_RANGE * WAKEUP_RANGE) return;
		
		if ((jumpTimer -= Main.getDeltaTime()) > 0) return;

		if (dx == 0 && dy == 0) {
			double d = Math.sqrt(distance);
			
			xx /= d;
			yy /= d;
			
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
			
			velocityX = (xx * jumpSpeed * Main.getDeltaTime());
			velocityY = (yy * jumpSpeed * Main.getDeltaTime());
		}
		
		dx += velocityX;
		dy += velocityY;
		
		move();
		
		if (jumpTimer > -jumpTime / 6.0) return;
		jumpTimer = jumpTime;
		
		dx = dy = 0;
	}

	public boolean isJumping() {
		return jumpTimer < 0;
	}

	public void render(Screen screen) {
		//renderEntity(screen, location);
		//screen.screen.draw(0x00ff00, x - game.cam.x, y - game.cam.y - (jumpTimer < 0 ? jumpHeight : 0), sizeD, sizeD);
		screen.screen.draw(ImageLoader.tilemap, x - game.cam.x, y - game.cam.y - (jumpTimer < 0 ? jumpHeight : 0), direction * 8, 7 * 16 + (jumpTimer < 0 ? 8 : 0), 8, 8);
	}

 	public boolean collide(Entity e) {
		return true;
	}

	public int getDamage() {
		return 1;
	}
}