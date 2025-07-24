package com.company.passtosurvive.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.PlayGameScreen;

public class CheckPoint extends TileObject {
  public CheckPoint(World world, Rectangle rect) {
    super(world, rect, 0f);
  }

  @Override
  public void inContactAct(PlayGameScreen playGameScreen) {
    super.inContactAct(playGameScreen);
    playGameScreen.setCheckpoint(getBody().getPosition().x, getBody().getPosition().y + 0.3f);
  }
}
