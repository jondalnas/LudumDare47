package com.Jonas.SJGE;

import java.util.ArrayList;
import java.util.List;

import com.Jonas.SJGE.entity.Bat;
import com.Jonas.SJGE.entity.Camera;
import com.Jonas.SJGE.entity.Enemy;
import com.Jonas.SJGE.entity.Entity;
import com.Jonas.SJGE.entity.Ghost;
import com.Jonas.SJGE.entity.Player;
import com.Jonas.SJGE.screen.gui.GUI;
import com.Jonas.SJGE.screen.gui.HUD;
import com.Jonas.SJGE.screen.gui.StartScreen;
import com.Jonas.SJGE.tilemap.Tilemap;

public class Game {
	public Tilemap tilemap;
	public Camera cam;
	public Player player;
	public List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> removedEntities = new ArrayList<Entity>();
	public Input input;

	public List<GUI> activeGUI = new ArrayList<GUI>();
	private List<GUI> addedGUI = new ArrayList<GUI>();
	private List<GUI> removedGUI = new ArrayList<GUI>();
	
	private boolean startGame = false;
	
	public int currentFloor = 1;
	
	public Game() {
		input = new Input();

		activeGUI.add(new StartScreen(this));
	}
	
	public void update() {
		if (startGame) {
			startGame = false;
			
			entities.clear();
			
			cam = new Camera(this);
			tilemap = new Tilemap(this);
			
			activeGUI.add(new HUD(this));
		}
		
		for (GUI gui : activeGUI) {
			gui.update();
		}
		
		if (player != null) { //Is player active
			cam.update();
			
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.update();
				
				if (e instanceof Enemy && ((Enemy) e).isDead()) removeEntity(e);
			}
		}
		
		if (addedGUI.size() != 0) {
			activeGUI.addAll(addedGUI);
			addedGUI.clear();
		}

		//Remove entities
		for (Entity e : removedEntities) {
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i) == e) {
					entities.remove(i);
					i--;
				}
			}
		}
		
		removedEntities.clear();

		//Remove GUI
		for (GUI gui : removedGUI) {
			for (int i = 0; i < activeGUI.size(); i++) {
				if (activeGUI.get(i) == gui) {
					activeGUI.remove(i);
					i--;
				}
			}
		}
		
		removedGUI.clear();
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
		if (e instanceof Player) {
			player = (Player) e;
			entities.add(e);
		} else {
			if (e instanceof Bat || e instanceof Ghost) {
				if (player == null)
					entities.add(e);
				else
					entities.add(entities.size() - 1, e);
			} else
				entities.add(0, e);
		}
	}

	public void removeEntity(Entity e) {
		removedEntities.add(e);
	}
	
	public void nextLevel() {
		currentFloor++;
		Player oldplayer = player;
		entities.clear();
		player = null;
		
		tilemap = new Tilemap(this);

		player.damageModefier = oldplayer.damageModefier;
		player.maxHp = oldplayer.maxHp;
		player.hp = oldplayer.hp;
	}
	
	public void startGame() {
		startGame = true;
	}

	public void addGUI(GUI gui) {
		addedGUI.add(gui);
	}

	public void removeGUI(GUI gui) {
		removedGUI.add(gui);
	}
}
