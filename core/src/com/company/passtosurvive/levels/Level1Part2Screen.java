package com.company.passtosurvive.levels;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.company.passtosurvive.control.PlayButtons;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.tools.b2WorldCreator;
import com.company.passtosurvive.view.Main;

public class Level1Part2Screen extends PlayGameScreen { // level 1 part 2 is triggered when the
  // player collides with a certain object in
  // part 1
  private static float playerCheckpointX;
  private static float playerCheckpointY;
  private static boolean finished;

  public Level1Part2Screen(final Main game) {
    super(
        new Builder(game)
            .setXMaxSpeed(2.5f)
            .setYMaxAccel(5f)
            .setGravity(-11));
    mapPort = new FitViewport(level1WorldWidth, level1WorldHeight, cam);
    cam.position.set(mapPort.getWorldWidth() / 2, mapPort.getWorldHeight() / 2, 0);
    map = mapLoader.load("Map2.tmx");
    renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM); // I divide almost all values
                                                                  // associated with the map by
                                                                  // PPM so that there are no
                                                                  // problems with units of vector2, force, velocity, etc
      player = new Player(world, playerCheckpointX, playerCheckpointY);
    buttons =
        new PlayButtons.Builder(game, player)
            .setXMaxSpeed(xMaxSpeed)
            .setYMaxAccel(yMaxAccel)
            .build();
    new b2WorldCreator(world, map, this);
  }

  public void transitionToPart2() {
    if (player.getBodyPositionX() < 280 / Main.PPM
        && playerCheckpointX == 0) { // This script will not work after death because There is a checkpoint at position 280 so as not to repeat
      buttons.getJoyStick().setVisible(false);
      buttons.getJump().setVisible(false);
      player.jump(2f);
      player
          .getPlayerBody()
          .applyLinearImpulse(new Vector2(2f, 0), player.getPlayerBody().getWorldCenter(), true);
    } else {
      buttons.getJoyStick().setVisible(true);
      buttons.getJump().setVisible(true);
    }
  }

  @Override
  public void render(float delta) {
    buttons.update();
    transitionToPart2();
    update(delta);
    super.render(delta);
    if(finished) dispose();
  }

  public static boolean isFinished() {
    return finished;
  }

  public static void setFinished(){
    finished=true;
  }

  public static void setPlayerCheckpointY(float playerCheckpointY) {
    Level1Part2Screen.playerCheckpointY = playerCheckpointY;
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
