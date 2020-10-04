package com.Jonas.SJGE.sound;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	public static Sound CHOOSE = load("/snd/Select.wav");
	public static Sound SELECT = load("/snd/Menu.wav");
	public static Sound HIT = load("/snd/Hit.wav");
	public static Sound HIT_BIG = load("/snd/Hit Big.wav");
	public static Sound HIT_SOFT = load("/snd/Hit Soft.wav");
	public static Sound HIT_GHOST = load("/snd/Hit Ghost 2.wav");
	public static Sound JUMP = load("/snd/Slime Jump.wav");
	public static Sound SWING = load("/snd/Swing.wav");
	public static Sound GHOST_DIE = load("/snd/Ghost Disapear Reapear.wav");
	public static Sound GHOST_VANISH = load("/snd/Ghost Disapear Reapear 3.wav");
	public static Sound ITEM = load("/snd/Get Item 2.wav");
	public static Sound STEP = load("/snd/Step.wav");
	public static Sound PLAYER_DAMAGE = load("/snd/Player Damage.wav");
	public static Sound SKELETON_STEP = load("/snd/Walk Skeleton.wav");
	public static Sound SPIDER_STEP = load("/snd/Walk Spider 2.wav");
	public static Sound BAT_FLAPPING = load("/snd/Bat Flapping.wav");
	public static Sound WIN = load("/snd/Win.wav");
	
	protected Clip clip;
	private static Sound load(String string) {
		Sound sound = new Sound();
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(string));
			sound.clip = AudioSystem.getClip();
			sound.clip.open(ais);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.out.println(string);
			e.printStackTrace();
		}
		
		return sound;
	}
	
	public void play() {
		if (clip == null) {
			System.out.println("Clip is null");
			return;
		}
		
		new Thread() {
			public void run() {
				clip.stop();
				clip.setFramePosition(0);
				clip.start();
			}
		}.start();
	}

	public void stop() {
		if (clip == null) {
			System.out.println("Clip is null");
			return;
		}
		
		new Thread() {
			public void run() {
				clip.stop();
			}
		}.start();
	}
}
