package com.company.passtosurvive.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.company.passtosurvive.tools.MusicalAtmosphere;

public class Main extends Game { // the very first class at startup
  
  private static float screenWidth, screenHeight; // dimensions of the screen on which we launch
  public static final float PPM = 100; // for conversions, pixels per meter
  private static MusicalAtmosphere music;
  private static TextureAtlas buttonAtlas;
  private static Skin buttonSkin;

  public static Skin getButtonSkin() {
    return buttonSkin;
  }

  @Override
  public void create() {
    screenWidth = Gdx.graphics.getWidth();
    screenHeight = Gdx.graphics.getHeight();
    buttonAtlas = new TextureAtlas("Buttons.pack");
    buttonSkin = new Skin(Gdx.files.internal("Buttons.json"), buttonAtlas);
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
