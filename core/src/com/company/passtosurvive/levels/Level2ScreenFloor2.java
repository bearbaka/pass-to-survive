package com.company.passtosurvive.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

public class Level2ScreenFloor2
    extends PlayGameScreen {  // level 2 part 2 is triggered when the
                              // player collides with a certain object in
                              // part 1
  private Texture background; // needed so that the person is not visible when
                              // he goes to the finish line

  public Level2ScreenFloor2(final Main game, float playerTransitX) {
    super(new Builder(game, 4)
              .setXMaxSpeed(3f)
              .setXMaxAccel(0.3f)
              .setYMaxAccel(7f)
              .setBouncerYMaxAccel(8f).setGravity(-21));
    background = new Texture("mapBackground.png");
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
    map = mapLoader.load("map4.tmx");
    renderer = new OrthogonalTiledMapRenderer(
        map, 1 / Main.PPM); // I divide almost all values
                            // associated with the map by
                            // PPM so that there are no
                            // problems with physics
    cam.position.set(mapPort.getWorldWidth() / 2, mapPort.getWorldHeight() / 2,
                     0);
    world = new World(new Vector2(0, gravity), true);
    if (Main.playerCheckpointY == 0 && Main.playerCheckpointX == 0) {
      player = new Player(world, playerTransitX, 704/Main.PPM); // save the height of the
      // previous person to make
      // it more realistic
    } else {
      player = new Player(world, Main.playerCheckpointX, Main.playerCheckpointY);
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
    batch.draw(background, -127, -375); // this texture is near the finish
    super.render(delta);
  }

  @Override
  public void dispose() {
    background.dispose();
    super.dispose();
  }
}
