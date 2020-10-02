package com.Jonas.SJGE.entity;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.screen.Screen;
import com.Jonas.SJGE.tilemap.Tilemap;

public abstract class Entity {
	public int x, y;
	public int dx, dy;
	protected Game game;
	public int xColOffs, yColOffs;
	public int sizeD = 16;
	
	public Entity(Game game) {
		this.game = game;
	}
	
	public abstract void update();
	public abstract void render(Screen screen);
	
	public void move() {
		if (dx != 0) {
			dx:
			for (int xx = dx * (dx < 0 ? -1 : 1); xx > 0; xx--) {
				int x2 = x + xx * (dx < 0 ? -1 : 1);
				
				if (x2+xColOffs < 0 || x2+xColOffs+sizeD >= Tilemap.TILE_SIZE * game.tilemap.getWidth()) continue;

				int x0 = (x2+xColOffs)/Tilemap.TILE_SIZE;
				int x1 = (x2+xColOffs+sizeD-1)/Tilemap.TILE_SIZE;
				int y0 = (y+yColOffs)/Tilemap.TILE_SIZE;
				int y1 = (y+yColOffs+sizeD-1)/Tilemap.TILE_SIZE;

				for (int xt = x0; xt <= x1; xt++) {
					for (int yt = y0; yt <= y1; yt++) {
						if (game.tilemap.getTile(xt, yt).solid) continue dx;
					}
				}

				for (Entity e : game.entities) {
					if (e.x + e.xColOffs > x2 + xColOffs + sizeD - 1 ||
						e.y + e.yColOffs > y + yColOffs + sizeD - 1 ||
						e.x + e.xColOffs + e.sizeD < x2 + xColOffs + 1 ||
						e.y + e.yColOffs + e.sizeD < y + yColOffs + 1) continue;
					
					if (e.collide(this))
						continue dx;
				}
				
				x = x2;
				dx = 0;
				break;
			}
		}
		
		if (dy != 0) {
			dy:
			for (int yy = dy * (dy < 0 ? -1 : 1); yy > 0; yy--) {
				int y2 = y + yy * (dy < 0 ? -1 : 1);
				
				if (y2+yColOffs < 0 || y2+yColOffs+sizeD >= Tilemap.TILE_SIZE * game.tilemap.getHeight()) continue;

				int x0 = (x+xColOffs)/Tilemap.TILE_SIZE;
				int x1 = (x+xColOffs+sizeD-1)/Tilemap.TILE_SIZE;
				int y0 = (y2+yColOffs)/Tilemap.TILE_SIZE;
				int y1 = (y2+yColOffs+sizeD-1)/Tilemap.TILE_SIZE;
				
				for (int xt = x0; xt <= x1; xt++) {
					for (int yt = y0; yt <= y1; yt++) {
						if (game.tilemap.getTile(xt, yt).solid) continue dy;
					}
				}
				
				for (Entity e : game.entities) {
					if (e.x + e.xColOffs > x + xColOffs + sizeD - 1 ||
						e.y + e.yColOffs > y2 + yColOffs + sizeD - 1 ||
						e.x + e.xColOffs + e.sizeD < x + xColOffs + 1 ||
						e.y + e.yColOffs + e.sizeD < y2 + yColOffs + 1) continue;
					
					if (e.collide(this))
						continue dy;
				}
				
				y = y2;
				dy = 0;
				break;
			}
		}
	}
	
	public abstract boolean collide(Entity e);
}
