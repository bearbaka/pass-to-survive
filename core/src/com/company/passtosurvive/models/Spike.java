package com.company.passtosurvive.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.view.DeadScreen;
import com.company.passtosurvive.view.Main;

public class Spike extends TileObject {
  public Spike(World world, TiledMap map, Rectangle bounds) {
    super(world, map, bounds);
    fixture.setUserData(this); // add so that WorldContactListener can recognize
  }

  @Override
  public void inContactAct(PlayGameScreen playGameScreen) {
    super.inContactAct(playGameScreen);
    if (!playGameScreen.getPlayer().isDead()) {
      final Main game = playGameScreen.getGame();
      playGameScreen.getPlayer().setDead(true);
      game.setScreen(new DeadScreen(game, false));
    }
  }
}
