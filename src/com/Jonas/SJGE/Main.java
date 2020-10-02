package com.Jonas.SJGE;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.Jonas.SJGE.screen.ImageLoader;
import com.Jonas.SJGE.screen.Renderer;

public class Main implements Runnable {
	private static Renderer renderer;
	private static Game game;
	private Thread thread;
	private static int FRAME_CAP = 60;
	private static double DELTA_TIME;
	
	public Main() {
		new ImageLoader();
	}
	
	public static void main(String[] args) {
		game = new Game();
		renderer = new Renderer(game);
		
		JFrame frame = new JFrame("Engine");
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(renderer, 0);
		
		frame.setContentPane(panel);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		new Main().start();
	}
	
	private void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		long last = System.nanoTime();
		while(true) {
			if ((System.nanoTime() - last) * 1.0e-9 < 1.0 / FRAME_CAP) {
				long delay = (long) ((1.0 / FRAME_CAP) * 1.0e3 - (System.nanoTime() - last) * 1.0e-6);
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			DELTA_TIME = (System.nanoTime() - last) * 1.0e-9;
			
			game.update();
			renderer.render();
			
			last = System.nanoTime();
		}
	}
	
	public static double getDeltaTime() {
		return DELTA_TIME;
	}
}
