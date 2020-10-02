package com.Jonas.SJGE;

import java.util.ArrayList;
import java.util.List;

import com.Jonas.SJGE.entity.Camera;
import com.Jonas.SJGE.entity.Entity;
import com.Jonas.SJGE.tilemap.Tilemap;

public class Game {
	public Tilemap tilemap;
	public Camera cam;
	public List<Entity> entities = new ArrayList<Entity>();
	public Input input;
	
	public Game() {
		input = new Input();
		cam = new Camera(this);
		tilemap = new Tilemap(this);
	}
	
	public void update() {
		cam.update();
		
		for (Entity e : entities) {
			e.update();
		}
	}
	
	public List<Entity> getEntitiesInside(int x0, int y0, int x1, int y1) {
		List<Entity> entities = new ArrayList<Entity>();
		
		for (Entity e : this.entities) {
			if (e.x + e.xColOffs > x1 ||
				e.y + e.yColOffs > y1 ||
				e.x + e.xColOffs + e.sizeD < x0 ||
				e.y + e.yColOffs + e.sizeD < y0) continue;
			
			entities.add(e);
		}
		
		return entities;
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void removeEntity(Entity e) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) == e) entities.remove(i);
		}
	}
}
