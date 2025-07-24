package com.company.passtosurvive.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.company.passtosurvive.control.PlayButtons;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.tools.MusicalAtmosphere;
import com.company.passtosurvive.tools.WorldContactListener;
import com.company.passtosurvive.view.GameOverScreen;
import com.company.passtosurvive.view.Main;

import lombok.Getter;

public abstract class PlayGameScreen
    implements Screen { // children of this class are only level screens
  static class Builder {
    private Main game;
    private float xMaxSpeed, yMaxAccel, bouncerYMaxAccel, gravity;

    Builder(Main game) {
      this.game = game;
    }

    Builder xMaxSpeed(float xMaxSpeed) {
      this.xMaxSpeed = xMaxSpeed;
      return this;
    }

    Builder yMaxAccel(float yMaxAccel) {
      this.yMaxAccel = yMaxAccel;
      return this;
    }

    Builder bouncerYMaxAccel(float bouncerYMaxAccel) {
      this.bouncerYMaxAccel = bouncerYMaxAccel;
      return this;
    }

    Builder gravity(float gravity) {
      this.gravity = gravity;
      return this;
    }
  }
  @Getter private static PlayGameScreen lastScreen;
  @Getter private static boolean cheatsEnabled, statsEnabled;
  static final float level2WorldWidth, level2WorldHeight, level1WorldWidth, level1WorldHeight;
  static {
    cheatsEnabled = false;
    statsEnabled = false;
    level1WorldHeight = 544f / Main.PPM;
    level1WorldWidth =
        904f
            / (1.661f / (Main.getScreenWidth() / Main.getScreenHeight()))
            / Main
                .PPM; // ratio of the FHD screen to the aspect ratio of the screen of the device on
                      // which I run
    level2WorldHeight = 672f / Main.PPM;
    level2WorldWidth =
        1116f / (1.661f / (Main.getScreenWidth() / Main.getScreenHeight())) / Main.PPM;
  }
  @Getter Player player; // create Player here for WorldContactListener
  @Getter private Main game;
  private Box2DDebugRenderer b2dr;
  World world;
  private SpriteBatch batch;
  TiledMap map;
  private WorldContactListener worldContactListener;
  TmxMapLoader mapLoader;
  PlayButtons buttons;
  OrthographicCamera cam;
  OrthogonalTiledMapRenderer renderer;

  Viewport mapPort; // Viewport is still required by tiled maps

  final float xMaxSpeed, yMaxAccel, bouncerYMaxAccel, gravity;

  PlayGameScreen(Builder builder) {
    lastScreen = this;
    this.game = builder.game;
    this.xMaxSpeed = builder.xMaxSpeed;
    this.yMaxAccel = builder.yMaxAccel;
    this.bouncerYMaxAccel = builder.bouncerYMaxAccel;
    this.gravity = builder.gravity;
    batch = new SpriteBatch();
    cam = new OrthographicCamera();
    mapLoader = new TmxMapLoader();
    world = new World(new Vector2(0, gravity), true);
    worldContactListener = new WorldContactListener(this);
    world.setContactListener(worldContactListener);
  }

  // needed to update the textures of the
  // Player sprite, camera, buttons, physics, collision, music, etc.
  public void update(float delta) {
    if (Gdx.input.isKeyJustPressed(Keys.S)) statsEnabled = !statsEnabled;
    if (Gdx.input.isKeyJustPressed(Keys.C)) {
      if (cheatsEnabled) {
        player.getFixtureList().forEach(fixture -> fixture.setSensor(false));
        world.setGravity(new Vector2(0, gravity));
        world.setContactListener(worldContactListener);
      } else {
        player.getFixtureList().forEach(fixture -> fixture.setSensor(true));
        world.setGravity(new Vector2(0, 0));
        world.setContactListener(null);
      }
      cheatsEnabled = !cheatsEnabled;
    }
    if (statsEnabled && PlayButtons.getStats() != null && PlayButtons.getStats().getStage() == null)
      PlayButtons.getStage().addActor(PlayButtons.getStats());
    else if (!statsEnabled
        && PlayButtons.getStats() != null
        && PlayButtons.getStats().getStage() != null) PlayButtons.getStats().remove();
    if (MusicalAtmosphere.isMusicOn() && !Main.getMusic().isAnyMusicPlaying()) {
      if (this instanceof Level1Part1Screen || this instanceof Level1Part2Screen)
        Main.getMusic().level1MusicPlay();
      else Main.getMusic().level2MusicPlay();
    }
    world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    if (player.isEndingBouncer()) { // invoked when last two bouncers are touched by player
      player.performJump(bouncerYMaxAccel);
      cam.position.y = player.getPosition().y;
    } else { // after we fall on something else this will start
      cam.position.y = mapPort.getWorldHeight() / 2; // camera returns to original position Y
    }
    cam.position.x = player.getPosition().x; // camera moves with player
    player.update(delta, PlayButtons.getJoystick().getValueX());
    cam.update();
    renderer.setView(cam);
    if (player.isTouchedBouncer()) player.performJump(bouncerYMaxAccel);
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1, true); // clean up
    renderer.render();
    if (statsEnabled) {
      if (b2dr == null) b2dr = new Box2DDebugRenderer();
      b2dr.render(
          world,
          cam.combined); // you can turn this on if you want to see a green outline around objects
      // of world
    }
    batch.setProjectionMatrix(cam.combined);
    batch.begin();
    player.draw(batch);
    batch.end();
    PlayButtons.getStage().act(delta);
    PlayButtons.getStage().draw();
    if (game.getScreen() instanceof GameOverScreen) restart();
  }

  @Override
  public void resize(int width, int height) {
    mapPort.update(width, height);
  }

  @Override
  public void dispose() {
    hide();
    buttons.dispose();
    if (b2dr != null) b2dr.dispose();
    batch.dispose();
    map.dispose();
    renderer.dispose();
    world.dispose();
    game.dispose();
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(
        PlayButtons.getStage()); // so that clicks are processed only by stage
  }

  @Override
  public void hide() {
    PlayButtons.getJoystick().setUnTouched();
    Main.getMusic().allPause();
  }

  public void restart() {
    buttons.updateRestarts();
  }

  public abstract void setCheckpoint(float x, float y);

  @Override
  public void pause() {}

  @Override
  public void resume() {}
}
