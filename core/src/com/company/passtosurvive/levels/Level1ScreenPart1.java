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

public class Level1ScreenPart1 extends PlayGameScreen { // level 1 part 1 starts in the main menu

  public Level1ScreenPart1(final Main game) {
    super(new Builder(game, 1).setXMaxSpeed(2.5f).setXMaxAccel(0.3f).setYMaxAccel(5f).setGravity(-11));
    cam = new OrthographicCamera();
    if (Main.getScreenWidth() == 1794 && Main.getScreenHeight() == 1080) { // I explained this in slides
                                                               // (.pptx file)
      Main.worldHeight = 544f;
      Main.worldWidth = 904f;
    } else {
      Main.worldHeight = 544f;
      Main.worldWidth = 904f / (1.66f / (Main.getScreenWidth() / Main.getScreenHeight())); // ratio of the FHD
                                                                               // screen to the
                                                                               // aspect ratio
                                                                               // of the screen of
                                                                               // the device on
                                                                               // which I run
    }
    mapPort = new FitViewport(Main.worldWidth / Main.PPM, Main.worldHeight / Main.PPM, cam);
    mapLoader = new TmxMapLoader();
    map = mapLoader.load("map1.tmx");
    renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM); // I divide almost all values
                                                                  // associated with the map by
                                                                  // PPM so that there are no
                                                                  // problems with physics
    cam.position.set(mapPort.getWorldWidth() / 2, mapPort.getWorldHeight() / 2, 0);
    world = new World(new Vector2(0, gravity), true);
    if (Main.playerX != 0 && Main.playerY != 0) {
      player = new Player(world, Main.playerX, Main.playerY); // increase due to
                                                              // the fact that
                                                              // the player does
                                                              // not spawn
                                                              // exactly in the
                                                              // center
    } else if (Main.playerX == 0 && Main.playerY == 0 && Main.playerCheckpointY == 0
        && Main.playerCheckpointX == 0) {
      // default spawn
      player = new Player(world, 96 / Main.PPM, 96 / Main.PPM);
      // spawn below the lava to test stability for constant deaths
      // player=new Player(world,400/Main.PPM, 96/Main.PPM);
      // check the roof bug when jumping
      // player = new Player(world, 1500 / Main.PPM, 400 / Main.PPM);
      // spawn at the end
      // player = new Player(world, 12800 / Main.PPM, 400 / Main.PPM);
    } else if (Main.playerX == 0 && Main.playerY == 0) {
      player = new Player(world, Main.playerCheckpointX, Main.playerCheckpointY + 0.3f); // increase
                                                                                         // Y by
                                                                                         // 0.3f so
                                                                                         // that the
                                                                                         // player
                                                                                         // spawns
                                                                                         // slightly
                                                                                         // higher
                                                                                         // than the
                                                                                         // checkpoint
                                                                                         // itself
    }
    buttons = new PlayButtons.Builder(game, player).setXMaxSpeed(xMaxSpeed)
        .setYMaxAccel(yMaxAccel).build();
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
