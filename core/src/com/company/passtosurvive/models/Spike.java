package com.company.passtosurvive.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.view.DeadScreen;
import com.company.passtosurvive.view.Main;

public class Spike extends TileObject {
  public Spike(World world, Rectangle rect) {
    super(world, rect);
  }

  @Override
  public void inContactAct(PlayGameScreen playGameScreen) {
    super.inContactAct(playGameScreen);
    if (playGameScreen == playGameScreen.getGame().getScreen()) {
      final Main game = playGameScreen.getGame();
      game.setScreen(new DeadScreen(game, this));
    }
  }
}
