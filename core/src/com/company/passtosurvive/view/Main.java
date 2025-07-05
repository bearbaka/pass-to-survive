package com.company.passtosurvive.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.company.passtosurvive.levels.Level1ScreenPart1;
import com.company.passtosurvive.levels.Level1ScreenPart2;
import com.company.passtosurvive.levels.Level2ScreenFloor1;
import com.company.passtosurvive.levels.Level2ScreenFloor2;
import com.company.passtosurvive.tools.MusicalAtmosphere;

// TODO:
// Combine levels, make some objects reusable, get rid of useless code, fix stats so that their
// state is saved

public class Main extends Game { // the very first class at startup
  public static float screenWidth, screenHeight; // dimensions of the screen on which we launch

  public static float getScreenHeight() {
    return screenHeight;
  }

  public static float getScreenWidth() {
    return screenWidth;
  }

  public static final float PPM = 100; // for conversions, pixels per meter

  public static boolean touchedBouncer = false;

  public static float playerCheckpointX; // to save checkpoint coordinates
  public static float playerCheckpointY;
  public static float worldWidth, worldHeight; // to optimize the map for the screen
  public static boolean musicIsOn = true; // to check if music is enabled in settings
  public static boolean nextFloor = false; // to start animation in level 2 of part 1
  public static boolean level1IsFinished = false; // to check if the level is passed
  public static boolean level2IsFinished = false;
  public static int deaths; // to count the number of deaths
  public static int screen; // to determine what level screen was
  private static MusicalAtmosphere music;

  public static MusicalAtmosphere getMusic() {
    return music;
  }

  @Override
  public void create() {
    screenWidth = Gdx.graphics.getWidth();
    screenHeight = Gdx.graphics.getHeight();
    music = new MusicalAtmosphere();
    setScreen(new MainMenuScreen(this));
  }
}
