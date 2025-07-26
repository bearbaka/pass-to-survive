package com.company.passtosurvive.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.Level1Part1Screen;
import com.company.passtosurvive.levels.Level1Part2Screen;
import com.company.passtosurvive.levels.Level2Part1Screen;
import com.company.passtosurvive.levels.Level2Part2Screen;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.view.Main;

public class NextPart extends TileObject {
  public NextPart(World world, Rectangle rect) {
    super(world, rect);
  }

  @Override
  public void inContactAct(PlayGameScreen playGameScreen) {
    super.inContactAct(playGameScreen);
    if (playGameScreen == playGameScreen.getGame().getScreen()) {
      Player player = playGameScreen.getPlayer();
      Main game = playGameScreen.getGame();
      if (playGameScreen instanceof Level1Part1Screen) {
        Level1Part1Screen.setFinished();
        Level1Part2Screen.setPlayerCheckpointY(player.getPosition().y);
        game.setScreen(new Level1Part2Screen(game));
      } else {
        Level2Part1Screen.setFinished();
        Level2Part2Screen.setPlayerCheckpointX(player.getPosition().x);
        game.setScreen(new Level2Part2Screen(game));
      }
    }
  }
}
