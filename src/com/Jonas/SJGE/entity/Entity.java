package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.tilemap.Tilemap;

public abstract class Entity {
	public int x, y;
	public double dx, dy;
	private int dxx, dyy;
	protected Game game;
	public int xColOffs, yColOffs;
	public int sizeD = 16;
	
	public Entity(Game game, int x, int y) {
		this.game = game;
		this.x = x;
		this.y = y;
	}
	
	public void move() {
		dxx = (int) dx;
		dyy = (int) dy;

		dx -= dxx;
		dy -= dyy;
		
		if (dxx != 0) {
			dx:
			for (int xx = dxx * (dxx < 0 ? -1 : 1); xx > 0; xx--) {
				int x2 = x + xx * (dxx < 0 ? -1 : 1);
				
				if (x2+xColOffs < 0 || x2+xColOffs+sizeD >= Tilemap.TILE_SIZE * game.tilemap.getWidth()) continue;

				int x0 = (x2+xColOffs)/Tilemap.TILE_SIZE;
				int x1 = (x2+xColOffs+sizeD-1)/Tilemap.TILE_SIZE;
				int y0 = (y+yColOffs)/Tilemap.TILE_SIZE;
				int y1 = (y+yColOffs+sizeD-1)/Tilemap.TILE_SIZE;

				boolean hitSolid = false;
				for (int xt = x0; xt <= x1; xt++) {
					for (int yt = y0; yt <= y1; yt++) {
						if (game.tilemap.getTile(xt, yt).isSolid(this)) hitSolid = true;
					}
				}
				
				if (hitSolid) continue;

				for (Entity e : game.entities) {
					if (e == this) continue;
					
					if (e.x + e.xColOffs > x2 + xColOffs + sizeD - 1 ||
						e.y + e.yColOffs > y + yColOffs + sizeD - 1 ||
						e.x + e.xColOffs + e.sizeD < x2 + xColOffs + 1 ||
						e.y + e.yColOffs + e.sizeD < y + yColOffs + 1) continue;

					boolean col0 = this.collide(e);
					boolean col1 = e.collide(this);
					
					if (col0 && col1)
						continue dx;
				}
				
				x = x2;
				if (xx != dxx * (dxx < 0 ? -1 : 1)) dxx = 0;
				break;
			}
		}
		
		if (dyy != 0) {
			dy:
			for (int yy = dyy * (dyy < 0 ? -1 : 1); yy > 0; yy--) {
				int y2 = y + yy * (dyy < 0 ? -1 : 1);
				
				if (y2+yColOffs < 0 || y2+yColOffs+sizeD >= Tilemap.TILE_SIZE * game.tilemap.getHeight()) continue;

				int x0 = (x+xColOffs)/Tilemap.TILE_SIZE;
				int x1 = (x+xColOffs+sizeD-1)/Tilemap.TILE_SIZE;
				int y0 = (y2+yColOffs)/Tilemap.TILE_SIZE;
				int y1 = (y2+yColOffs+sizeD-1)/Tilemap.TILE_SIZE;

				boolean hitSolid = false;
				for (int xt = x0; xt <= x1; xt++) {
					for (int yt = y0; yt <= y1; yt++) {
						if (game.tilemap.getTile(xt, yt).isSolid(this)) hitSolid = true;
					}
				}
				
				if (hitSolid) continue;
				
				for (Entity e : game.entities) {
					if (e == this) continue;
					
					if (e.x + e.xColOffs > x + xColOffs + sizeD - 1 ||
						e.y + e.yColOffs > y2 + yColOffs + sizeD - 1 ||
						e.x + e.xColOffs + e.sizeD < x + xColOffs + 1 ||
						e.y + e.yColOffs + e.sizeD < y2 + yColOffs + 1) continue;

					boolean col0 = this.collide(e);
					boolean col1 = e.collide(this);
					
					if (col0 && col1)
						continue dy;
				}
				
				y = y2;
				if (yy != dyy * (dyy < 0 ? -1 : 1)) dyy = 0;
				break;
			}
		}
	}
	
	public void renderEntity(Screen screen, int location) {
		screen.screen.draw(ImageLoader.tilemap, x - game.cam.x, y - game.cam.y, (location % 16) * Tilemap.TILE_SIZE, (location / 16) * Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
	}
	
	public abstract void update();
	public abstract void render(Screen screen);
	public abstract boolean collide(Entity e);
}
