package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.sound.Sound;

public class Skeleton extends Enemy {
	private final double speed = 60;
	
	private byte direction = 0;
	private byte walkingAnimCount = 0;
	private byte walkingAnim = 0;
	private final double walkingAnimTimer = 0.125;
	private double walkingAnimTime = 0;

	public Skeleton(Game game, int x, int y) {
		super(game, x, y, 4, Sound.HIT_BIG, Sound.HIT_BIG);
	}

	public void update() {
		if ((knockbackTime -= Main.getDeltaTime()) > 0) {
			dx += hurtVelocityX;
			dy += hurtVelocityY;
			
			move();
			
			return;
		}
		
		double xx = game.player.x - x;
		double yy = game.player.y - y;
		
		double distance = xx*xx + yy*yy;
		
		if (distance > WAKEUP_RANGE * WAKEUP_RANGE) return;
		
		double d = Math.sqrt(distance);
		
		xx /= d;
		yy /= d;

		dx += xx * Main.getDeltaTime() * speed;
		dy += yy * Main.getDeltaTime() * speed;
		
		if (dx != 0 || dy != 0) {
			if ((walkingAnimTime -= Main.getDeltaTime()) < 0) {
				walkingAnimTime = walkingAnimTimer;
				
				walkingAnimCount = (byte) ((++walkingAnimCount) % 4);
				
				walkingAnim = (byte) (walkingAnimCount == 0 ? 0 : (walkingAnimCount == 1 ? 1 : (walkingAnimCount == 2 ? 0 : (walkingAnimCount == 3 ? 2 : 0))));
				
				if (walkingAnim == 0) Sound.SKELETON_STEP.play();
			}
		} else {
			walkingAnim = 0;
			walkingAnimTime = 0;
		}
		
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
		
		move();
	}

	public void render(Screen screen) {
		screen.screen.drawWithColor(ImageLoader.tilemap, 0x212121, x - game.cam.x, y - game.cam.y + 2, 4 * 16, 3 * 16, 16, 16);
		screen.screen.draw(ImageLoader.tilemap, x - game.cam.x, y - game.cam.y, direction * 16, (8 + walkingAnim) * 16, 16, 16);
	}

	public boolean collide(Entity e) {
		return true;
	}

	public int getDamage() {
		return 2;
	}

}
