package com.Jonas.SJGE.entity;

import java.util.ArrayList;
import java.util.List;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.Main;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.sound.Sound;
import com.sun.glass.events.KeyEvent;

public class Player extends Entity {
	private final int speed = 60;

	public int maxHp = 6;
	public int hp = maxHp;
	private final double invTime = 1;
	private double invTimer = 0;
	private final double knockback = 360;
	private double velocityX, velocityY;

	private final int swordSize = 16;
	private final double swordSwingTime = 0.25;
	private double swordSwingTimer;
	private final int swordDamage = 1;
	public int damageModefier = 1;
	
	private byte direction = 0;
	private byte walkingAnimCount = 0;
	private byte walkingAnim = 0;
	private final double walkingAnimTimer = 0.125;
	private double walkingAnimTime = 0;

	
	public Player(Game game, int x, int y) {
		super(game, x, y);
		
		sizeD = 16;
	}

	public void update() {
		
		invTimer -= Main.getDeltaTime();
		swordSwingTimer -= Main.getDeltaTime();

		if (hp <= 0) return;
		
		if (invTimer < invTime * (1.0 - 1e-1)) {
			//Move
			int xx = game.input.getKeyDown('d') ? 1 : (game.input.getKeyDown('a') ? -1 : 0);
			int yy = game.input.getKeyDown('s') ? 1 : (game.input.getKeyDown('w') ? -1 : 0);
			
			dx += xx * speed * Main.getDeltaTime();
			dy += yy * speed * Main.getDeltaTime();
			
			if (xx != 0 || yy != 0) {
				if ((walkingAnimTime -= Main.getDeltaTime()) < 0) {
					walkingAnimTime = walkingAnimTimer;
					
					walkingAnimCount = (byte) ((++walkingAnimCount) % 4);
					
					walkingAnim = (byte) (walkingAnimCount == 0 ? 0 : (walkingAnimCount == 1 ? 1 : (walkingAnimCount == 2 ? 0 : (walkingAnimCount == 3 ? 2 : 0))));
					
					if (walkingAnim == 0) Sound.STEP.play();
				}
			} else {
				walkingAnim = 0;
				walkingAnimTime = 0;
			}

			if (swordSwingTimer < 0) {
				direction = (byte) (game.input.getKeyDown('s') ? 0 : (game.input.getKeyDown('w') ? 1 : (game.input.getKeyDown('d') ? 2 : (game.input.getKeyDown('a') ? 3 : direction))));
			}
			
			//Attack
			if (swordSwingTimer < 0) {
				List<Entity> hitEntities = new ArrayList<Entity>();

				boolean down  = game.input.getButtonPressed(KeyEvent.VK_DOWN);
				boolean up    = game.input.getButtonPressed(KeyEvent.VK_UP);
				boolean left  = game.input.getButtonPressed(KeyEvent.VK_LEFT);
				boolean right = game.input.getButtonPressed(KeyEvent.VK_RIGHT);
				
				if (down) {
					hitEntities.addAll(game.getEntitiesInside(x, y+sizeD, x+sizeD, y+sizeD+swordSize));
					direction = 0;
				} else if (up) {
					hitEntities.addAll(game.getEntitiesInside(x, y-swordSize, x+sizeD, y));
					direction = 1;
				} else if (right) {
					hitEntities.addAll(game.getEntitiesInside(x+sizeD, y, x+sizeD+swordSize, y+sizeD));
					direction = 2;
				} else if (left) {
					hitEntities.addAll(game.getEntitiesInside(x-swordSize, y, x, y+sizeD));
					direction = 3;
				}
				
				if (down || up || left || right) {
					boolean playSound = true;
					for (Entity e : hitEntities) {
						if (e instanceof Enemy && !(e instanceof Ghost)) playSound = false;
					}
					
					if (playSound)
						Sound.SWING.play();
				}
				
				for (Entity e : hitEntities) {
					if (e instanceof Enemy)
						((Enemy) e).damage(swordDamage * damageModefier);
				}
				
				if (hitEntities.size() > 0) 
					swordSwingTimer = swordSwingTime;
			}
		} else {
			dx += velocityX;
			dy += velocityY;
		}
		
		move();
		
		game.cam.x = x - game.cam.xColOffs;
		game.cam.y = y - game.cam.yColOffs;
	}
	
	public void damage(int damage, Entity e) {
		if (hp <= 0) return;
		
		if (damage <= 0) return;
		
		if (invTimer > 0) return;
		invTimer = invTime;// * damage;
		
		hp -= damage;
		
		Sound.PLAYER_DAMAGE.play();

		double xx = x - e.x;
		double yy = y - e.y;
		
		double d = Math.sqrt(xx*xx + yy*yy);
		
		xx /= d;
		yy /= d;

		velocityX = xx * knockback * Main.getDeltaTime();
		velocityY = yy * knockback * Main.getDeltaTime();
	}

	public void render(Screen screen) {
		//screen.screen.draw(0xff00ff, Screen.WIDTH / 2 - sizeD / 2, Screen.HEIGHT / 2 - sizeD / 2, sizeD, sizeD);

		screen.screen.drawWithColor(ImageLoader.tilemap, 0x212121, Screen.WIDTH / 2 - sizeD / 2, Screen.HEIGHT / 2 - sizeD / 2 + 2, 4 * 16, 3 * 16, 16, 16);
		screen.screen.draw(ImageLoader.tilemap, Screen.WIDTH / 2 - sizeD / 2, Screen.HEIGHT / 2 - sizeD / 2, (hp <= 0 ? 4 : direction) * 16, (4 + (hp <= 0 ? 0 : walkingAnim)) * 16, 16, 16);
		
		if (swordSwingTimer > 0) {
			switch (direction) {
			case 0:
				screen.screen.draw(ImageLoader.tilemap, Screen.WIDTH / 2 - sizeD / 2, Screen.HEIGHT / 2 - sizeD / 2 + sizeD, direction * 16, 3 * 16, 16, 16);
				break;
			case 1:
				screen.screen.draw(ImageLoader.tilemap, Screen.WIDTH / 2 - sizeD / 2, Screen.HEIGHT / 2 - sizeD / 2 - swordSize, direction * 16, 3 * 16, 16, 16);
				break;
			case 2:
				screen.screen.draw(ImageLoader.tilemap, Screen.WIDTH / 2 - sizeD / 2 + sizeD, Screen.HEIGHT / 2 - sizeD / 2, direction * 16, 3 * 16, 16, 16);
				break;
			case 3:
				screen.screen.draw(ImageLoader.tilemap, Screen.WIDTH / 2 - sizeD / 2 - swordSize, Screen.HEIGHT / 2 - sizeD / 2, direction * 16, 3 * 16, 16, 16);
				break;
			}
		}
	}

	public boolean collide(Entity e) {
		if (hp <= 0) return false;
		
		if (e instanceof Enemy) {
			damage(((Enemy)e).getDamage(), e);
		}
		
		return true;
	}
}
