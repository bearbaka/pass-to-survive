package com.company.passtosurvive.models;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.Level1Part1Screen;
import com.company.passtosurvive.levels.Level1Part2Screen;
import com.company.passtosurvive.levels.Level2Part1Screen;
import com.company.passtosurvive.levels.Level2Part2Screen;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.view.Main;

public class NextPart extends TileObject {
  public NextPart(World world, TiledMap map, Rectangle bounds) {
    super(world, map, bounds);
    fixture.setUserData(this); // add so that WorldContactListener can recognize
  }

  @Override
  public void inContactAct(PlayGameScreen playGameScreen) {
    super.inContactAct(playGameScreen);
    Player player = playGameScreen.getPlayer();
    final Main game = playGameScreen.getGame();
    if (playGameScreen instanceof Level1Part1Screen) {
      Level1Part1Screen.setFinished();
      Level1Part2Screen.setPlayerCheckpointY(player.getPosY());
      game.setScreen(new Level1Part2Screen(game));
    } else {
      Level2Part1Screen.setFinished();
      Level2Part2Screen.setPlayerCheckpointX(player.getPosX());
      game.setScreen(new Level2Part2Screen(game));
    }
  }
}
