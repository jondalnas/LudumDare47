package com.Jonas.SJGE.tilemap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;

import com.Jonas.SJGE.Game;
import com.Jonas.SJGE.entity.Entity;
import com.Jonas.SJGE.tilemap.tiles.Tile;

public class TilemapLoader {
	public class LoadedMap {
		private final int width, height;
		private Tile[] tilemap;
		
		public LoadedMap(Tile[] tilemap, int width, int height) {
			this.tilemap = tilemap;
			this.width = width;
			this.height = height;
		}
		
		public Tile[] getTilemap() {
			return tilemap;
		}
		
		public int getWidth() {
			return width;
		}
		
		public int getHeight() {
			return height;
		}
	}
	
	public static LoadedMap load(BufferedImage bi, Game game) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		
		int[] pixels = new int[width * height];
		
		bi.getRGB(0, 0, width, height, pixels, 0, width);

		Tile[] tm = new Tile[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				int col = pixels[x+y*width] & 0xffffff;
				int entity = (pixels[x+y*width] >> 24) & 0xff;
				
				try {
					tm[x+y*width] = (Tile) Class.forName(Tile.class.getPackage().getName() + ".VoidTile").getDeclaredConstructor(Game.class).newInstance(game);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				for (TileColor tc : TileColor.values()) {
					if (tc.color == col) {
						try {
							tm[x+y*width] = (Tile) Class.forName(Tile.class.getPackage().getName() + "." + (tc.name().charAt(0) + "").toUpperCase() + tc.name().substring(1)).getDeclaredConstructor(Game.class).newInstance(game);
							break;
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
							throw new RuntimeException(e);
						} catch (ClassNotFoundException e) {
							System.out.println("Tile associated with color does not exist!");
							throw new RuntimeException(e);
						}
					}
				}

				if (entity == 0xff) continue;
				
				for (EntityColor ec : EntityColor.values()) {
					if (ec.color == entity) {
						try {
							game.addEntity((Entity) Class.forName(Entity.class.getPackage().getName() + "." + (ec.name().charAt(0) + "").toUpperCase() + ec.name().substring(1)).getDeclaredConstructor(Game.class, int.class, int.class).newInstance(game, x * Tilemap.TILE_SIZE, y * Tilemap.TILE_SIZE));
							break;
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
							throw new RuntimeException(e);
						} catch (ClassNotFoundException e) {
							System.out.println("Entity associated with color does not exist!");
							throw new RuntimeException(e);
						}
					}
				}
			}
		}
		
		return new TilemapLoader().new LoadedMap(tm, bi.getWidth(), bi.getHeight());
	}
	
	public static LoadedMap load(String map, Game game) {
		BufferedImage bi;
		
		try {
			bi = ImageIO.read(new File(map));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return load(bi, game);
	}

	public enum EntityColor {
		player(0x88),
		healthChest(0x00),
		healthUpChest(0x01),
		damageUpChest(0x02),
		slime(0x70, 3),
		bat(0x74, 3),
		skeleton(0x78, 10),
		spider(0x7a, 1),
		ghost(0x7c, 20);
	
		public int color;
		public int difficulty = -1;
		EntityColor(int color) {
			this.color = color;
		}
		
		EntityColor(int color, int difficulty) {
			this.color = color;
			this.difficulty = difficulty;
		}
	}

	public enum TileColor {
		voidTile(0),
		wall(0xaaaaaa),
		floor(0x888888),
		stairwell(0xff0000);
		
		public int color;
		TileColor(int color) {
			this.color = color;
		}
	}
}