package com.company.passtosurvive.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.Level1Part2Screen;
import com.company.passtosurvive.levels.Level2Part2Screen;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.view.Main;
import com.company.passtosurvive.view.WinScreen;

public class Finish extends TileObject {
  public Finish(World world, Rectangle rect) {
    super(world, rect);
  }

  @Override
  public void inContactAct(PlayGameScreen playGameScreen) {
    super.inContactAct(playGameScreen);
    if (playGameScreen == playGameScreen.getGame().getScreen()) {
      Main game = playGameScreen.getGame();
      if (playGameScreen instanceof Level1Part2Screen) Level1Part2Screen.setFinished();
      else Level2Part2Screen.setFinished();
      game.setScreen(new WinScreen(game));
    }
  }
}
