package com.company.passtosurvive.models;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.view.Main;
import com.company.passtosurvive.view.WinScreen;

public class Finish extends TileObject {
  public Finish(World world, TiledMap map, Rectangle bounds) {
    super(world, map, bounds);
    fixture.setUserData(this); // add so that WorldContactListener can recognize
  }

  @Override
  public void inContactAct(PlayGameScreen playGameScreen) {
    super.inContactAct(playGameScreen);
    final Main game = playGameScreen.getGame();
    if (Main.screen <= 2) {
      Main.level1IsFinished = true;
    } else {
      Main.level2IsFinished = true;
    }
    playGameScreen.getPlayer().setDead(true);
    game.setScreen(new WinScreen(game));
  }
}
