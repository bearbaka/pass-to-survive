package com.company.passtosurvive.levels;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.company.passtosurvive.control.PlayButtons;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.tools.b2WorldCreator;
import com.company.passtosurvive.view.Main;

import lombok.Getter;

public class Level2Part2Screen
    extends PlayGameScreen {  // level 2 part 2 is triggered when the
                              // player collides with a certain object in
                              // part 1
  private static float playerCheckpointX, playerCheckpointY;
  @Getter private static boolean finished;

  static {
    playerCheckpointY=704/Main.PPM;
  }

  public static void setFinished(){
    finished=true;
  }

  public static void setPlayerCheckpointX(float playerCheckpointX) {
    Level2Part2Screen.playerCheckpointX = playerCheckpointX;
  }

  public Level2Part2Screen(final Main game) {
    super(new Builder(game)
              .xMaxSpeed(4f)
              .yMaxAccel(7f)
              .bouncerYMaxAccel(8f).gravity(-21));
    mapPort = new FitViewport(level2WorldWidth,
                              level2WorldHeight, cam);
    cam.position.set(mapPort.getWorldWidth() / 2, mapPort.getWorldHeight() / 2, 0);
    map = mapLoader.load("Map4.tmx");
    renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM); // I divide almost all values
                                                                  // associated with the map by
                                                                  // PPM so that there are no
                                                                  // problems with units of vector2, force, velocity, etc
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
    if(finished) dispose();
  }

  @Override
  public void setCheckpoint(float x, float y){
    playerCheckpointX=x;
    playerCheckpointY=y;
  }

  @Override
  public void restart() {
    super.restart();
    player.reset(playerCheckpointX, playerCheckpointY);
  }
}
