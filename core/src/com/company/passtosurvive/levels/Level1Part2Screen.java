package com.company.passtosurvive.levels;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.company.passtosurvive.control.PlayButtons;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.tools.b2WorldCreator;
import com.company.passtosurvive.view.Main;

import lombok.Getter;

public class Level1Part2Screen
    extends PlayGameScreen { // level 1 part 2 is triggered when the player collides with a certain
                             // object in part 1
  private static float playerCheckpointX, playerCheckpointY;
  @Getter private static boolean finished;

  public static void setFinished() {
    finished = true;
  }

  public static void setPlayerCheckpointY(float playerCheckpointY) {
    Level1Part2Screen.playerCheckpointY = playerCheckpointY;
  }

  public Level1Part2Screen(final Main game) {
    super(new Builder(game).xMaxSpeed(3f).yMaxAccel(5f).gravity(-11));
    mapPort = new FitViewport(level1WorldWidth, level1WorldHeight, cam);
    cam.position.set(mapPort.getWorldWidth() / 2, mapPort.getWorldHeight() / 2, 0);
    map = mapLoader.load("Map2.tmx");
    renderer =
        new OrthogonalTiledMapRenderer(
            map,
            1
                / Main
                    .PPM); // I divide almost all values associated with the map by PPM so that
                           // there are no problems with units of vector2, force, velocity, etc
    player = new Player(world, playerCheckpointX, playerCheckpointY);
    buttons =
        PlayButtons.builder()
            .game(game)
            .player(player)
            .xMaxSpeed(xMaxSpeed)
            .yMaxAccel(yMaxAccel)
            .build();
    new b2WorldCreator(world, map, this);
  }

  public void transitionToPart2() {
    if (player.getPosition().x < 280 / Main.PPM
        && playerCheckpointX
            == 0) { // This script will not work after restart because There is a checkpoint at
                    // position 280 so as not to repeat
      PlayButtons.getJoyStick().setVisible(false);
      PlayButtons.getJump().setVisible(false);
      player.jump(2f);
      player
          .applyLinearImpulse(new Vector2(2f, 0), player.getWorldCenter(), true);
    } else {
      PlayButtons.getJoyStick().setVisible(true);
      PlayButtons.getJump().setVisible(true);
    }
  }

  @Override
  public void render(float delta) {
    buttons.update();
    transitionToPart2();
    update(delta);
    super.render(delta);
    if (finished) dispose();
  }

  @Override
  public void setCheckpoint(float x, float y) {
    playerCheckpointX = x;
    playerCheckpointY = y;
  }

  @Override
  public void restart() {
    super.restart();
    player.reset(playerCheckpointX, playerCheckpointY);
  }
}
