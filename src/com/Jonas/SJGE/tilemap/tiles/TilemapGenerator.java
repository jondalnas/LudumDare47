package com.Jonas.SJGE.tilemap.tiles;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.Jonas.SJGE.tilemap.TilemapLoader;

public class TilemapGenerator extends Canvas {
	private static final long serialVersionUID = 1L;
	private final static int MIN_ROOM_SIZE = 3, MAX_ROOM_SIZE = 5;
	private final static int MIN_ROOM_DISTANCE = 3;
	private final static int CORRIDOR_WIDTH = 2;
	private final static double STAIRWELL_CHANCE = 0.5;
	private final static double STAIRWELL_DIFFICULTY_BOOST = 2;
	private final static double CHEST_DIFFICULTY_BOOST = 2;
	private final static double CHEST_SPAWN_CHANCE = 0.2;

	public static void main(String[] args) {
		TilemapGenerator tmg = new TilemapGenerator();
		
		tmg.setSize(new Dimension(128 * 4, 128 * 4));
		
		JFrame frame = new JFrame("Engine");
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(tmg, 0);
		
		frame.setContentPane(panel);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		BufferedImage bi = generate(tmg, 256, 256, 20);
		
		while(true) {
			BufferStrategy bs = tmg.getBufferStrategy();
			if (bs == null) {
				tmg.createBufferStrategy(3);
				continue;
			}
			
			Graphics graphics = bs.getDrawGraphics();
			graphics.drawImage(bi, 0, 0, tmg.getWidth(), tmg.getHeight(), null);
			graphics.dispose();
			bs.show();
		}
	}
	
	public static BufferedImage generate(TilemapGenerator tmg, int width, int height, int difficulty) {
		width >>= 2;
		height >>= 2;
		
		int[] pixels = new int[width * height];
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xff000000;
		}
		
		List<Room> rooms = new ArrayList<Room>();
		
		rooms.add(tmg.new Room(width/2 - 2/2, height/2 - 2/2, 2, 2));
		loop:
		for (int i = 0; i < /*MAX_ROOM_COUNT*/Math.log10(difficulty)*18-10; i++) { //10 room at 10 difficulty and log growth
			int x, y, w, h;
			
			int j = 0;
			test:
			while (true) {
				if (j++ > 100) continue loop;
				x = (int) (Math.random() * (width - MAX_ROOM_SIZE - CORRIDOR_WIDTH * 2)) + CORRIDOR_WIDTH;
				y = (int) (Math.random() * (height - MAX_ROOM_SIZE - CORRIDOR_WIDTH * 2)) + CORRIDOR_WIDTH;
				w = (int) (Math.random() * (MAX_ROOM_SIZE - MIN_ROOM_SIZE)) + MIN_ROOM_SIZE;
				h = (int) (Math.random() * (MAX_ROOM_SIZE - MIN_ROOM_SIZE)) + MIN_ROOM_SIZE;
				
				for (Room r : rooms) {
					if (r.x - MIN_ROOM_DISTANCE < x + w && r.x + r.w + MIN_ROOM_DISTANCE > x && r.y - MIN_ROOM_DISTANCE < y + h && r.y + r.h + MIN_ROOM_DISTANCE > y) continue test;
				}
				
				break;
			}
			
			rooms.add(tmg.new Room(x, y, w, h));
		}
		
		List<Corridor> corridor = new ArrayList<Corridor>();
		
		List<Room> open = new ArrayList<Room>();
		open.addAll(rooms);
		open.remove(0);
		
		Room curr = rooms.get(0);
		while (!open.isEmpty()) {
			int minDist = Integer.MAX_VALUE;
			int room = -1;
			for (int i = 0; i < open.size(); i++) {
				Room r = open.get(i);

				int xd = r.x - curr.x;
				int yd = r.y - curr.y;
				
				if (xd * xd + yd * yd < minDist) {
					room = i;
					minDist = xd * xd + yd * yd;
				}
			}
			
			corridor.add(tmg.new Corridor(curr, open.get(room)));
			curr.exitPoints.add(corridor.get(corridor.size()-1).points[0]);
			open.get(room).exitPoints.add(corridor.get(corridor.size()-1).points[1]);
			curr = open.get(room);
			open.remove(room);
		}
		
		for (Room r : rooms) {
			drawRoom(r, pixels, width, height);
		}
		
		for (Corridor c : corridor) {
			drawCorridor(c, pixels, width, height);
		}
		
		//Scale level up
		BufferedImage bi = new BufferedImage(width << 2, height << 2, BufferedImage.TYPE_INT_ARGB);
		int[] scaledPixels = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
		
		for (int xx = 0; xx < width; xx++) {
			for (int yy = 0; yy < height; yy++) {
				for (int x = 0; x < 4; x++) {
					for (int y = 0; y < 4; y++) {
						scaledPixels[((xx<<2)+x) + ((yy<<2)+y) * (width << 2)] = pixels[xx + yy * width];
					}
				}
			}
		}

		for (int xx = 1; xx < (width << 2) - 1; xx++) {
			for (int yy = 1; yy < (height << 2) - 1; yy++) {
				if ((scaledPixels[xx + yy * (width << 2)] & 0xffffff) != 0x888888)
					continue;
				
				if ((scaledPixels[(xx+1) + yy * (width << 2)] & 0xffffff) == 0 ||
					(scaledPixels[(xx-1) + yy * (width << 2)] & 0xffffff) == 0 ||
					(scaledPixels[xx + (yy+1) * (width << 2)] & 0xffffff) == 0 ||
					(scaledPixels[xx + (yy-1) * (width << 2)] & 0xffffff) == 0 ||
					(scaledPixels[(xx+1) + (yy+1) * (width << 2)] & 0xffffff) == 0 ||
					(scaledPixels[(xx+1) + (yy-1) * (width << 2)] & 0xffffff) == 0 ||
					(scaledPixels[(xx-1) + (yy+1) * (width << 2)] & 0xffffff) == 0 ||
					(scaledPixels[(xx-1) + (yy-1) * (width << 2)] & 0xffffff) == 0)
					scaledPixels[xx + yy * (width << 2)] = 0xffaaaaaa;
			}
		}
		
		scaledPixels[bi.getWidth()/2 + bi.getHeight()/2 * bi.getWidth()] = (0x88 << 24) | (scaledPixels[bi.getWidth()/2 + bi.getHeight()/2 * bi.getWidth()] & 0xffffff);

		//Make sure the spawn room is untouched
		rooms.remove(0);
		
		//Generate stairwell
		for (int i = rooms.size() - 1; i >= 0; i--) {
			int bestDistance = Integer.MAX_VALUE;
			int bestIndex = -1;
			
			for (int o = i; o >= 0; o--) {
				int distanceX = rooms.get(o).getCenterX() - (width << 1);
				int distanceY = rooms.get(o).getCenterY() - (height << 1);
				
				int distance = distanceX * distanceX + distanceY * distanceY;
				
				if (distance < bestDistance) {
					bestDistance = distance;
					bestIndex = o;
				}
			}
			
			rooms.add(rooms.get(bestIndex));
			rooms.remove(bestIndex);
		}

		for (int i = rooms.size() - 1; i >= 0; i--) {
			if (Math.random() < STAIRWELL_CHANCE) {
				int x = (int) (Math.random() * ((rooms.get(i).w << 2) - 4)) + (rooms.get(i).x << 2) + 2;
				int y = (int) (Math.random() * ((rooms.get(i).h << 2) - 4)) + (rooms.get(i).y << 2) + 2;
				
				scaledPixels[x + y * (width << 2)] = (scaledPixels[x + y * (width << 2)] & 0xff000000) | (0xff0000);
				
				rooms.get(i).setStairwell();
				
				break;
			}
		}
		
		//TODO: Add enemies and treasure here
		for (Room r : rooms) {
			double difficultyModified = difficulty * (r.hasStairwell() ? STAIRWELL_DIFFICULTY_BOOST : 1);
			
			//Spawn chest
			if (!r.hasStairwell()) {
				if (Math.random() < CHEST_SPAWN_CHANCE) {
					if (scaledPixels[(r.getCenterX() << 2) + (r.getCenterY() << 2) * (width << 2)] == 0xff888888) {
						scaledPixels[(r.getCenterX() << 2) + (r.getCenterY() << 2) * (width << 2)] = (getChestLoot() << 24) | (scaledPixels[(r.getCenterX() << 2) + (r.getCenterY() << 2) * (width << 2)] & 0xffffff);
						
						difficultyModified *= CHEST_DIFFICULTY_BOOST;
					}
				}
			}
			
			//Spawn enemy
			int currDifficulty = 0;
			
			List<Integer> enemies = new ArrayList<Integer>();
			
			while (currDifficulty < difficultyModified) {
				int index = (int) (Math.random() * TilemapLoader.EntityColor.values().length);
				if (TilemapLoader.EntityColor.values()[index].difficulty > 0 && TilemapLoader.EntityColor.values()[index].difficulty + currDifficulty <= difficultyModified) {
					currDifficulty += TilemapLoader.EntityColor.values()[index].difficulty;
					
					enemies.add(TilemapLoader.EntityColor.values()[index].color);
				}
			}

			for (int e : enemies) {
				int x = (int) (Math.random() * ((r.w << 2) - 2)) + (r.x << 2) + 1;
				int y = (int) (Math.random() * ((r.h << 2) - 2)) + (r.y << 2) + 1;
				
				scaledPixels[x + y * (width << 2)] = (e << 24) | (scaledPixels[x + y * (width << 2)] & 0xffffff);
			}
		}
		
		return bi;
	}
	
	private static byte getChestLoot() {
		if (Math.random() < 0.10) return 0x01; //Life up 10%
		if (Math.random() < 0.15) return 0x01; //Life up 15%
		
		return 0x00; //Extra life 75%
	}
	
	private static void drawRoom(int[] pixels, int x, int y, int w, int h, int imageWidth, int imageHeight) {
		for (int yy = 0; yy < h; yy++) {
			int y0 = yy + y;
			if (y0 < 0 || y0 >= imageHeight) continue;
			
			for (int xx = 0; xx < w; xx++) {
				int x0 = xx + x;
				if (x0 < 0 || x0 >= imageWidth) continue;
				
				/*if (xx == 0 || yy == 0 || xx == w-1 || yy == h-1)
					pixels[x0 + y0 * imageWidth] = 0xffaaaaaa;
				else*/
				pixels[x0 + y0 * imageWidth] = 0xff888888;
			}
		}
	}
	
	private static void drawCorridorH(int[] pixels, int x0, int y0, int x1, int y1, int imageWidth, int imageHeight) {
		if (x0 > x1) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;

			tmp = y0;
			y0 = y1;
			y1 = tmp;
		}
		
		int xm = (x0+x1)/2;
		for (int x = x0; x <= xm; x++) {
			pixels[x + y0 * imageWidth] = 0xff888888;
		}
		
		for (int x = xm; x <= x1; x++) {
			pixels[x + y1 * imageWidth] = 0xff888888;
		}
		
		if (y0 > y1) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;

			tmp = y0;
			y0 = y1;
			y1 = tmp;
		}

		for (int y = y0; y < y1; y++) {
			pixels[xm + y * imageWidth] = 0xff888888;
		}
	}
	
	private static void drawCorridorV(int[] pixels, int x0, int y0, int x1, int y1, int imageWidth, int imageHeight) {
		if (y0 > y1) {
			int tmp = y0;
			y0 = y1;
			y1 = tmp;

			tmp = x0;
			x0 = x1;
			x1 = tmp;
		}
		
		int ym = (y0+y1)/2;
		for (int y = y0; y <= ym; y++) {
			pixels[x0 + y * imageWidth] = 0xff888888;
		}
		
		for (int y = ym; y <= y1; y++) {
			pixels[x1 + y * imageWidth] = 0xff888888;
		}
		
		if (x0 > x1) {
			int tmp = y0;
			y0 = y1;
			y1 = tmp;

			tmp = x0;
			x0 = x1;
			x1 = tmp;
		}

		for (int x = x0; x < x1; x++) {
			pixels[x + ym * imageWidth] = 0xff888888;
		}
	}
	
	private static void drawRoom(Room room, int[] pixels, int imageWidth, int imageHeight) {
		drawRoom(pixels, room.x, room.y, room.w, room.h, imageWidth, imageHeight);
		
		for (int[] points : room.exitPoints) {
			pixels[points[0] + points[1] * imageWidth] = 0xff888888;
		}
	}
	
	private static void drawCorridor(Corridor corridor, int[] pixels, int imageWidth, int imageHeight) {
		
		if ((corridor.type & 0b00000011) != 0)
			drawCorridorV(pixels, corridor.points[0][0] + ((corridor.type & 0b00000100) != 0 ? -1 : ((corridor.type & 0b00001000) != 0 ? 1 : 0)), 
								  corridor.points[0][1] + ((corridor.type & 0b00000001) != 0 ? -1 : ((corridor.type & 0b00000010) != 0 ? 1 : 0)), 
								  corridor.points[1][0] + ((corridor.type & 0b01000000) != 0 ? -1 : ((corridor.type & 0b10000000) != 0 ? 1 : 0)), 
								  corridor.points[1][1] + ((corridor.type & 0b00010000) != 0 ? -1 : ((corridor.type & 0b00100000) != 0 ? 1 : 0)), imageWidth, imageHeight);
		else
			drawCorridorH(pixels, corridor.points[0][0] + ((corridor.type & 0b00000100) != 0 ? -1 : ((corridor.type & 0b00001000) != 0 ? 1 : 0)), 
								  corridor.points[0][1] + ((corridor.type & 0b00000001) != 0 ? -1 : ((corridor.type & 0b00000010) != 0 ? 1 : 0)), 
								  corridor.points[1][0] + ((corridor.type & 0b01000000) != 0 ? -1 : ((corridor.type & 0b10000000) != 0 ? 1 : 0)), 
								  corridor.points[1][1] + ((corridor.type & 0b00010000) != 0 ? -1 : ((corridor.type & 0b00100000) != 0 ? 1 : 0)), imageWidth, imageHeight);
	}
	
	private class Room {
		public int x, y, w, h;
		public List<int[]> exitPoints = new ArrayList<int[]>();
		
		private boolean hasStairwell = false;
		
		public Room(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;

			/*exitPoints.add(new int[] {x+w/2, y});
			if (Math.random() > 0.5) exitPoints.add(new int[] {x+w/2, y+h-1});
			if (Math.random() > 0.8) exitPoints.add(new int[] {x, y+h/2});
			if (Math.random() > 0.95) exitPoints.add(new int[] {x+w-1, y+h/2});*/
		}
		
		public int getCenterX() {
			return x+w/2;
		}
		
		public int getCenterY() {
			return y+h/2;
		}
		
		public void setStairwell() {
			hasStairwell = true;
		}
		
		public boolean hasStairwell() {
			return hasStairwell;
		}
	}
	
	private class Corridor {
		int[][] points;
		byte type = 0;
		
		public Corridor(Room r0, Room r1) {
			int x0, y0, x1, y1;
			x0 = y0 = x1= y1 = 0;
			
			int minDist = Integer.MAX_VALUE;
			points = new int[2][2];
			
			for (int i = 0; i < 4; i++) {
				byte currTypeI = 0;
				switch(i) {
				case 0:
					x0 = r0.x+r0.w/2;
					y0 = r0.y;
					currTypeI |= 0b00000001;
					break;
				case 1:
					x0 = r0.x+r0.w/2;
					y0 = r0.y+r0.h-1;
					currTypeI |= 0b00000010;
					break;
				case 2:
					x0 = r0.x;
					y0 = r0.y+r0.h/2;
					currTypeI |= 0b00000100;
					break;
				case 3:
					x0 = r0.x+r0.w-1;
					y0 = r0.y+r0.h/2;
					currTypeI |= 0b00001000;
					break;
				}
				for (int o = 0; o < 4; o++) {
					byte currTypeO = 0;
					switch(o) {
					case 0:
						x1 = r1.x+r1.w/2;
						y1 = r1.y;
						currTypeO |= 0b00010000;
						break;
					case 1:
						x1 = r1.x+r1.w/2;
						y1 = r1.y+r1.h-1;
						currTypeO |= 0b00100000;
						break;
					case 2:
						x1 = r1.x;
						y1 = r1.y+r1.h/2;
						currTypeO |= 0b01000000;
						break;
					case 3:
						x1 = r1.x+r1.w-1;
						y1 = r1.y+r1.h/2;
						currTypeO |= 0b10000000;
						break;
					}

					int xd = x0 - x1;
					int yd = y0 - y1;
					
					if (minDist > xd*xd + yd*yd) {
						minDist = xd*xd + yd*yd;
						points[0][0] = x0;
						points[0][1] = y0;
						points[1][0] = x1;
						points[1][1] = y1;
						
						type = (byte) (currTypeI | currTypeO);
					}
				}
			}
		}
	}
}
