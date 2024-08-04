package com.company.passtosurvive.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Main extends Game { // the very first class at startup
	public static float width, height; // dimensions of the screen on which we launch
	public static final float PPM=100; // for conversions, pixels per meter
	public static int hit; // to determine what the player encountered
	public static float HumanX=0; // to save the player's coordinates after a pause
	public static float HumanY=0;
	public static float HumanXCheckpoint=0; // to save checkpoint coordinates
	public static float HumanYCheckpoint=0;
	public static float Human2Y=0; // to save the Y position when moving to the 2nd part of the 1st level
	public static float worldWidth, worldHeight; // to optimize the map for the screen
	public static boolean soundIsOn=true; // to check if music is enabled in settings
	public static boolean infoIsPressed=false; // is the info button pressed
	public static boolean PreviousBouncers=false; // for bouncers in level 2
	public static boolean nextFloor=false; // to start animation in level 2 of part 1
	public static boolean chooseLevel=false; // to check if the play button is pressed
	public static boolean level1IsFinished=false; // to check if the level is passed
	public static boolean level2IsFinished=false;
	public static int deaths=0; // to count the number of deaths
	public static int screen; // to determine what level screen was
	public static Vector2 v; // to not create each time in the level screens
	public static Animation animation; // so as not to create every time
	@Override
	public void create() {
		v=new Vector2();
		setScreen(new MainMenuScreen(this));
	}
	@Override
	public void render() {
		super.render();
	}
}
