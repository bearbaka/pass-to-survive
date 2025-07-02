package com.company.passtosurvive.view;

import com.badlogic.gdx.Game;
import com.company.passtosurvive.levels.Level1ScreenPart1;
import com.company.passtosurvive.levels.Level1ScreenPart2;
import com.company.passtosurvive.levels.Level2ScreenFloor1;
import com.company.passtosurvive.levels.Level2ScreenFloor2;
import com.company.passtosurvive.tools.MusicalAtmosphere;

public class Main extends Game { // the very first class at startup
  private static float width, height; // dimensions of the screen on which we launch

  public static float getHeight() {
    return height;
  }

  public static void setHeight(float height) {
    Main.height = height;
  }

  public static float getWidth() {
    return width;
  }

  public static void setWidth(float width) {
    Main.width = width;
  }

  public static final float PPM = 100; // for conversions, pixels per meter

  public static boolean touchedBouncer = false;

  public static float playerX; // to save the player's coordinates after a pause
  public static float playerY;
  public static float playerTransitY;
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

  public void setPreviousLevel() {
    if (Main.screen == 1) {
      setScreen(new Level1ScreenPart1(this));
    } else if (Main.screen == 2) {
      setScreen(new Level1ScreenPart2(this));
    } else if (Main.screen == 3) {
      setScreen(new Level2ScreenFloor1(this));
    } else if (Main.screen == 4) {
      setScreen(new Level2ScreenFloor2(this));
    }
  }

  @Override
  public void create() {
    music = new MusicalAtmosphere();
    setScreen(new MainMenuScreen(this));
  }
}
