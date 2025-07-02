package com.company.passtosurvive.models;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.Level1ScreenPart2;
import com.company.passtosurvive.levels.Level2ScreenFloor2;
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
    Main.playerX = 0;
    Main.playerY = 0;
    Main.playerCheckpointX = 0;
    Main.playerCheckpointY = 0;
    if (Main.screen <= 2) {
      Main.playerTransitY = player.getPosY();
      playGameScreen.getPlayer().setDead(true);
      game.setScreen(new Level1ScreenPart2(game));
    } else {
      Main.playerX = player.getPosX();
      playGameScreen.getPlayer().setDead(true);
      game.setScreen(new Level2ScreenFloor2(game));
    }
  }
}
