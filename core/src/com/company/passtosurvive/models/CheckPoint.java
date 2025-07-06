package com.company.passtosurvive.models;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.view.Main;

public class CheckPoint extends TileObject {
  public CheckPoint(World world, TiledMap map, Rectangle bounds) {
    super(world, map, bounds);
    fixture.setUserData(this); // add so that WorldContactListener can recognize
  }

  @Override
  public void inContactAct(PlayGameScreen playGameScreen) {
    super.inContactAct(playGameScreen);
    playGameScreen.setCheckpoint(getX(), getY()+0.3f);
  }
}
