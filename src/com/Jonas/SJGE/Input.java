package com.Jonas.SJGE;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
	private boolean[] keys = new boolean[65535];
	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}

	public void keyTyped(KeyEvent e) {
	}
	
	public boolean getKeyDown(char ch) {
		return keys[ch-97+KeyEvent.VK_A];
	}
}
