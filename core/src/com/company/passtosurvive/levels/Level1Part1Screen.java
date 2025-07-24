package com.company.passtosurvive.levels;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.company.passtosurvive.control.PlayButtons;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.tools.b2WorldCreator;
import com.company.passtosurvive.view.Main;

import lombok.Getter;

public class Level1Part1Screen extends PlayGameScreen { // level 1 part 1 starts in the main menu
  private static float playerCheckpointX, playerCheckpointY;
  @Getter private static boolean finished;

  static {
    // default
    playerCheckpointX = 96 / Main.PPM;
    playerCheckpointY = 96 / Main.PPM;
    // spawn below the lava to test stability for constant restarts
    // playerCheckpointX=400/Main.PPM;
    // playerCheckpointY=500/Main.PPM;
    // check the roof bug when jumping
    // playerCheckpointX=1500/Main.PPM;
    // playerCheckpointY=400/Main.PPM;
    // spawn at the end
    // playerCheckpointX=12800/Main.PPM;
    // playerCheckpointY=400/Main.PPM;
  }

  public static void setFinished() {
    finished = true;
  }

  public Level1Part1Screen(final Main game) {
    super(new Builder(game).xMaxSpeed(3f).yMaxAccel(5f).gravity(-11));
    mapPort = new FitViewport(level1WorldWidth, level1WorldHeight, cam);
    cam.position.set(mapPort.getWorldWidth() / 2, mapPort.getWorldHeight() / 2, 0);
    map = mapLoader.load("Map1.tmx");
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

  @Override
  public void render(float delta) {
    buttons.update();
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
