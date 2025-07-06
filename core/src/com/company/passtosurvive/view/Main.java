package com.company.passtosurvive.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.company.passtosurvive.tools.MusicalAtmosphere;

// TODO:
// Combine levels, make some objects reusable, get rid of useless code, fix stats so that their
// state is saved

public class Main extends Game { // the very first class at startup
  
  private static float screenWidth, screenHeight; // dimensions of the screen on which we launch
  public static final float PPM = 100; // for conversions, pixels per meter
  private static MusicalAtmosphere music;

  @Override
  public void create() {
    screenWidth = Gdx.graphics.getWidth();
    screenHeight = Gdx.graphics.getHeight();
    music = new MusicalAtmosphere();
    setScreen(new MainMenuScreen(this));
  }

  public static float getScreenHeight() {
    return screenHeight;
  }

  public static float getScreenWidth() {
    return screenWidth;
  }

  public static MusicalAtmosphere getMusic() {
    return music;
  }
}
