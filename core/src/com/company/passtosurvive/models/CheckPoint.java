package com.company.passtosurvive.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.PlayGameScreen;

public class CheckPoint extends TileObject {
  public CheckPoint(World world, Rectangle rect) {
    super(world, rect);
  }

  @Override
  public void inContactAct(PlayGameScreen playGameScreen) {
    super.inContactAct(playGameScreen);
    playGameScreen.setCheckpoint(getX(), getY()+0.3f);
  }
}
