package com.company.passtosurvive.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.company.passtosurvive.tools.MusicalAtmosphere;

import lombok.AccessLevel;
import lombok.Getter;

public class Main extends Game { // the very first class at startup
  
  @Getter private static float screenWidth, screenHeight; // dimensions of the screen on which we launch
  public static final float PPM = 100; // for conversions, pixels per meter
  @Getter(AccessLevel.PUBLIC) private static MusicalAtmosphere music;
  private static TextureAtlas buttonAtlas;
  @Getter private static Skin buttonSkin;

  @Override
  public void create() {
    screenWidth = Gdx.graphics.getWidth();
    screenHeight = Gdx.graphics.getHeight();
    buttonAtlas = new TextureAtlas("Buttons.pack");
    buttonSkin = new Skin(Gdx.files.internal("Buttons.json"), buttonAtlas);
    music = new MusicalAtmosphere();
    setScreen(new MainMenuScreen(this));
  }
}
