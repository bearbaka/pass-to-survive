package com.company.passtosurvive.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.company.passtosurvive.control.PlayButtons;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.tools.WorldContactListener;
import com.company.passtosurvive.tools.b2WorldCreator;
import com.company.passtosurvive.view.DeadScreen;
import com.company.passtosurvive.view.Main;

public class Level2Part2Screen
    extends PlayGameScreen {  // level 2 part 2 is triggered when the
                              // player collides with a certain object in
                              // part 1
  private Texture background; // needed so that the person is not visible when
                              // he goes to the finish line
  private static float playerCheckpointX;
  private static float playerCheckpointY;
  private static boolean finished;

  public static boolean isFinished() {
    return finished;
  }

  public static void setFinished(){
    finished=true;
  }

  static {
    playerCheckpointY=704/Main.PPM;
  }
  
  public static void setPlayerCheckpointX(float playerCheckpointX) {
    Level2Part2Screen.playerCheckpointX = playerCheckpointX;
  }

  @Override
  public void setCheckpoint(float x, float y){
    playerCheckpointX=x;
    playerCheckpointY=y;
  }

  @Override
  public void restart() {
    player.reset(world, playerCheckpointX, playerCheckpointY);
  }

  public Level2Part2Screen(final Main game) {
    super(new Builder(game)
              .setXMaxSpeed(3f)
              .setYMaxAccel(7f)
              .setBouncerYMaxAccel(8f).setGravity(-21));
    background = new Texture("mapBackground.png");
    cam = new OrthographicCamera();
    mapPort = new FitViewport(level2WorldWidth,
                              level2WorldHeight, cam);
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
      player = new Player(world, playerCheckpointX, playerCheckpointY);
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
    ScreenUtils.clear(0, 0, 0, 1, true); // clean up
    renderer.render();
    b2dr.render(world, cam.combined); // you can turn this on if you want to
    // see a green outline around objects of world
    batch.setProjectionMatrix(cam.combined);
    batch.begin();
    batch.draw(background, -127, -375); // this texture is near the finish
    player.draw(batch);
    batch.end();
    buttons.getStage().act(delta);
    buttons.getStage().draw();
    if (game.getScreen() instanceof DeadScreen) restart();
    if(finished) dispose();
  }

  @Override
  public void dispose() {
    background.dispose();
    super.dispose();
  }
}
