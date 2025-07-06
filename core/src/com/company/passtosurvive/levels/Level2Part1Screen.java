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

public class Level2Part1Screen extends PlayGameScreen { // level 2 part 1 starts in the main menu
  private static float playerCheckpointX;
  private static float playerCheckpointY;
  private static boolean finished;

  static {
    // default spawn
    playerCheckpointX = 128 / Main.PPM;
    playerCheckpointY = 224 / Main.PPM;
    // test bouncers
    // playerCheckpointX=2100/Main.PPM;
    // playerCheckpointY=400/Main.PPM;
    // test pipes at head
    // playerCheckpointX=12800/Main.PPM;
    // playerCheckpointY=400/Main.PPM;
    // test transition to part 2
    // playerCheckpointX=13800/Main.PPM;
    // playerCheckpointY=400/Main.PPM;
  }

  @Override
  public void setCheckpoint(float x, float y) {
    playerCheckpointX = x;
    playerCheckpointY = y;
  }

  @Override
  public void restart() {
    player.reset(world, playerCheckpointX, playerCheckpointY);
  }

  public Level2Part1Screen(final Main game) {
    super(
        new Builder(game)
            .setXMaxSpeed(3f)
            .setYMaxAccel(8f)
            .setBouncerYMaxAccel(10f)
            .setGravity(-21));
    cam = new OrthographicCamera();
    mapPort = new FitViewport(level2WorldWidth, level2WorldHeight, cam);
    mapLoader = new TmxMapLoader();
    map = mapLoader.load("map3.tmx");
    renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM); // I divide almost all values
    // associated with the map by
    // PPM so that there are no
    // problems with physics
    cam.position.set(mapPort.getWorldWidth() / 2, mapPort.getWorldHeight() / 2, 0);
    world = new World(new Vector2(0, gravity), true);
    player = new Player(world, playerCheckpointX, playerCheckpointY);
    buttons =
        new PlayButtons.Builder(game, player)
            .setXMaxSpeed(xMaxSpeed)
            .setYMaxAccel(yMaxAccel)
            .build();
    b2dr = new Box2DDebugRenderer();
    new b2WorldCreator(world, map, this);
    worldContactListener = new WorldContactListener(this);
    world.setContactListener(worldContactListener);
  }

  @Override
  public void render(float delta) {
    buttons.update();
    update(delta);
    super.render(delta);
    if (finished) dispose();
  }

  public static boolean isFinished() {
    return finished;
  }

  public static void setFinished() {
    finished = true;
  }
}
