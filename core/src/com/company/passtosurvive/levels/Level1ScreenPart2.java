package com.company.passtosurvive.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.company.passtosurvive.control.PlayButtons;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.tools.WorldContactListener;
import com.company.passtosurvive.tools.b2WorldCreator;
import com.company.passtosurvive.view.Main;

public class Level1ScreenPart2
    extends PlayGameScreen { // level 1 part 2 is triggered when the
  // player collides with a certain object in
  // part 1

  public Level1ScreenPart2(final Main game) {
    super(
        new Builder(game, 2).setXMaxSpeed(2.5f).setXMaxAccel(0.3f).setYMaxAccel(
            5f).setGravity(-11));
    cam = new OrthographicCamera();
    if (Main.getWidth() == 1794 &&
        Main.getHeight() == 1080) { // I explained this in slides
                                    // (.pptx file)
      Main.worldHeight = 544f;
      Main.worldWidth = 904f;
    } else {
      Main.worldHeight = 544f;
      Main.worldWidth = 904f / (1.66f / (Main.getWidth() / Main.getHeight()));
    }
    mapPort = new FitViewport(Main.worldWidth / Main.PPM,
                              Main.worldHeight / Main.PPM, cam);
    mapLoader = new TmxMapLoader();
    map = mapLoader.load("map2.tmx");
    renderer = new OrthogonalTiledMapRenderer(
        map, 1 / Main.PPM); // I divide almost all values
    // associated with the map by
    // PPM so that there are no
    // problems with physics
    cam.position.set(mapPort.getWorldWidth() / 2, mapPort.getWorldHeight() / 2,
                     0);
    world = new World(new Vector2(0, gravity), true);
    if (Main.playerX == 0 && Main.playerY == 0 && Main.playerCheckpointY == 0 &&
        Main.playerCheckpointX == 0 && Main.playerTransitY != 0) {
      player = new Player(world, 0 / Main.PPM,
                          Main.playerTransitY); // save the height of the
                                                // previous person to make
                                                // it more realistic
    } else if (Main.playerX != 0 && Main.playerY != 0) {
      player = new Player(world, Main.playerX, Main.playerY); // increase due to
                                                              // the fact that
                                                              // the player does
                                                              // not spawn
                                                              // exactly in the
                                                              // center
    } else {
      player = new Player(world, Main.playerCheckpointX,
                          Main.playerCheckpointY + 0.3f); // increase
                                                          // Y by
                                                          // 0.3f
                                                          // so
                                                          // that
                                                          // the
                                                          // player
                                                          // spawns
                                                          // slightly
                                                          // higher
                                                          // than
                                                          // the
                                                          // checkpoint
                                                          // itself
    }
    buttons = new PlayButtons.Builder(game, player)
                  .setXMaxAccel(xMaxAccel)
                  .setXMaxSpeed(xMaxSpeed)
                  .setYMaxAccel(yMaxAccel)
                  .build();
    b2dr = new Box2DDebugRenderer();
    new b2WorldCreator(world, map, this);
    world.setContactListener(new WorldContactListener(this));
  }

  public void transitionToPart2() {
    if (player.getPosX() < 280 / Main.PPM && Main.playerTransitY != 0 &&
        Main.playerX == 0 && Main.playerY == 0 && Main.playerCheckpointX == 0 &&
        Main.playerCheckpointY == 0) { // This
                                       // script
                                       // will
                                       // not
                                       // work
      // after death because There
      // is a checkpoint at
      // position 280 so as not to
      // repeat
      buttons.joyStick.setVisible(false);
      buttons.jump.setVisible(false);
      player.jump(2f);
      player.playerBody.applyLinearImpulse(
          new Vector2(2f, 0), player.playerBody.getWorldCenter(), true);
    } else if (player.getPosX() >= 280 / Main.PPM) {
      buttons.joyStick.setVisible(true);
      buttons.jump.setVisible(true);
    }
  }

  @Override
  public void render(float delta) {
    buttons.handling();
    transitionToPart2();
    update(delta);
    super.render(delta);
  }
}
