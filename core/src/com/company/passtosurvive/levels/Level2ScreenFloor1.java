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

public class Level2ScreenFloor1
    extends PlayGameScreen { // level 2 part 1 starts in the main menu

  public Level2ScreenFloor1(final Main game) {
    super(new Builder(game, 3)
              .setXMaxSpeed(3f)
              .setXMaxAccel(0.3f)
              .setYMaxAccel(8f)
              .setBouncerYMaxAccel(10f).setGravity(-21));
    cam = new OrthographicCamera();
    if (Main.getScreenWidth() == 1794 &&
        Main.getScreenHeight() == 1080) { // I explained this in slides
                                    // (.pptx file)
      Main.worldHeight = 672f;
      Main.worldWidth = 1116f;
    } else {
      Main.worldHeight = 672f;
      Main.worldWidth = 1116f / (1.66f / (Main.getScreenWidth() / Main.getScreenHeight()));
    }
    mapPort = new FitViewport(Main.worldWidth / Main.PPM,
                              Main.worldHeight / Main.PPM, cam);
    mapLoader = new TmxMapLoader();
    map = mapLoader.load("map3.tmx");
    renderer = new OrthogonalTiledMapRenderer(
        map, 1 / Main.PPM); // I divide almost all values
                            // associated with the map by
                            // PPM so that there are no
                            // problems with physics
    cam.position.set(mapPort.getWorldWidth() / 2, mapPort.getWorldHeight() / 2,
                     0);
    world = new World(new Vector2(0, gravity), true);
    if (Main.playerX != 0 && Main.playerY != 0) {
      player = new Player(world, Main.playerX, Main.playerY); // increase due to
                                                              // the fact that
                                                              // the player does
                                                              // not spawn
                                                              // exactly in the
                                                              // center
    } else if (Main.playerX == 0 && Main.playerY == 0 &&
               Main.playerCheckpointY == 0 && Main.playerCheckpointX == 0) {
      // default spawn
      player = new Player(world, 128 / Main.PPM, 224 / Main.PPM);
      // test bouncers
      // player = new Player(world, 2100 / Main.PPM, 400 / Main.PPM);
      // test pipes at head
      // player = new Player(world, 12800 / Main.PPM, 400 / Main.PPM);
      // test transition to part 2
      // player = new Player(world, 13800 / Main.PPM, 400 / Main.PPM);
    } else if (Main.playerX == 0 && Main.playerY == 0) {
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
                  .setXMaxSpeed(xMaxSpeed)
                  .setYMaxAccel(yMaxAccel)
                  .build();
    b2dr = new Box2DDebugRenderer();
    new b2WorldCreator(world, map, this);
    worldContactListener=new WorldContactListener(this);
    world.setContactListener(worldContactListener);
  }

  @Override
  public void render(float delta) {
    buttons.update();
    update(delta);
    super.render(delta);
  }
}
