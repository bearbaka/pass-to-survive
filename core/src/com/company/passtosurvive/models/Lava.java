package com.company.passtosurvive.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.view.GameOverScreen;
import com.company.passtosurvive.view.Main;

public class Lava extends TileObject {
  public Lava(World world, Rectangle rect) {
    super(world, rect, 0f);
  }

  @Override
  public void inContactAct(PlayGameScreen playGameScreen) {
    super.inContactAct(playGameScreen);
    if (playGameScreen == playGameScreen.getGame().getScreen()) {
      Main game = playGameScreen.getGame();
      game.setScreen(new GameOverScreen(game, this));
    }
  }
}
