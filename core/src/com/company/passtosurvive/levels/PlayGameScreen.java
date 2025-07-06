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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.company.passtosurvive.control.PlayButtons;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.models.Player.State;
import com.company.passtosurvive.tools.MusicalAtmosphere;
import com.company.passtosurvive.tools.WorldContactListener;
import com.company.passtosurvive.view.DeadScreen;
import com.company.passtosurvive.view.Main;

public abstract class PlayGameScreen implements Screen { // children of this class are only level
  // screens
  private static PlayGameScreen lastScreen;
  private static boolean cheatsEnabled, statsEnabled;
  static final float level2WorldWidth, level2WorldHeight, level1WorldWidth, level1WorldHeight;
  Player player; // create Player here for WorldContactListener
  final Main game;
  Box2DDebugRenderer b2dr;
  World world;
  SpriteBatch batch;
  TiledMap map;
  WorldContactListener worldContactListener;
  TmxMapLoader mapLoader;
  PlayButtons buttons;
  OrthographicCamera cam;
  OrthogonalTiledMapRenderer renderer;
  Viewport mapPort; // Viewport is still required by tiled maps
  final float xMaxSpeed, yMaxAccel, bouncerYMaxAccel, gravity;

  static {
    cheatsEnabled = false;
    statsEnabled = false;
    level1WorldHeight = 544f / Main.PPM;
    level1WorldWidth =
        904f
            / (1.661f / (Main.getScreenWidth() / Main.getScreenHeight()))
            / Main.PPM; // ratio of the FHD screen to the aspect ratio of the screen of the device on which I run
    level2WorldHeight = 672f / Main.PPM;
    level2WorldWidth =
        1116f / (1.661f / (Main.getScreenWidth() / Main.getScreenHeight())) / Main.PPM;
  }

  static class Builder {
    private Main game;
    private float xMaxSpeed, yMaxAccel, bouncerYMaxAccel, gravity;

    Builder(Main game) {
      this.game = game;
    }

    Builder setXMaxSpeed(float xMaxSpeed) {
      this.xMaxSpeed = xMaxSpeed;
      return this;
    }

    Builder setYMaxAccel(float yMaxAccel) {
      this.yMaxAccel = yMaxAccel;
      return this;
    }

    Builder setBouncerYMaxAccel(float bouncerYMaxAccel) {
      this.bouncerYMaxAccel = bouncerYMaxAccel;
      return this;
    }

    Builder setGravity(float gravity) {
      this.gravity = gravity;
      return this;
    }
  }

  PlayGameScreen(Builder builder) {
    lastScreen = this;
    this.game = builder.game;
    this.xMaxSpeed = builder.xMaxSpeed;
    this.yMaxAccel = builder.yMaxAccel;
    this.bouncerYMaxAccel = builder.bouncerYMaxAccel;
    this.gravity = builder.gravity;
    batch = new SpriteBatch();
  }

  // needed to update the textures of the
  // Player sprite, camera, buttons, physics, collision, music, etc.
  public void update(float delta) {
    if (Gdx.input.isKeyJustPressed(Keys.S)) statsEnabled = !statsEnabled;
    if (Gdx.input.isKeyJustPressed(Keys.C)) {
      if (cheatsEnabled) {
        for (Fixture fixture : player.getPlayerBody().getFixtureList()) fixture.setSensor(false);
        world.setGravity(new Vector2(0, gravity));
        world.setContactListener(worldContactListener);
      } else {
        for (Fixture fixture : player.getPlayerBody().getFixtureList()) fixture.setSensor(true);
        world.setGravity(new Vector2(0, 0));
        world.setContactListener(null);
      }
      cheatsEnabled = !cheatsEnabled;
    }
    if (statsEnabled && buttons.getStats().getStage() == null)
      buttons.getStage().addActor(buttons.getStats());
    else if (!statsEnabled && buttons.getStats().getStage() != null) buttons.getStats().remove();
    if (MusicalAtmosphere.isMusicOn() && !Main.getMusic().isAnyMusicPlaying()) {
      if (this instanceof Level1Part1Screen || this instanceof Level1Part2Screen)
        Main.getMusic().level1MusicPlay();
      else Main.getMusic().level2MusicPlay();
    }
    world.step(1 / 60f, 6, 2);
    if (Player.isNextFloor()) { // on this map the last bouncers at the end of this
      // part of the level are marked as nextfloor and when
      // we fall on them Main.nextFloor is set to true
      if (player.getCurrentState() != State.JUMPING) player.performJump(10f);
      cam.position.y = player.getPlayerBody().getPosition().y;
    } else { // after we fall on something else this will start
      cam.position.y = mapPort.getWorldHeight() / 2; // camera returns to original position Y
    }
    cam.position.x = player.getPlayerBody().getPosition().x; // camera moves with player
    player.update(delta, buttons.getJoyStick().getValueX());
    cam.update();
    renderer.setView(cam);
    if (Player.isTouchedBouncer() && player.getCurrentState() != State.JUMPING)
      player.performJump(bouncerYMaxAccel);
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1, true); // clean up
    renderer.render();
    b2dr.render(world, cam.combined); // you can turn this on if you want to
    // see a green outline around objects of world
    batch.setProjectionMatrix(cam.combined);
    batch.begin();
    player.draw(batch);
    batch.end();
    buttons.getStage().act(delta);
    buttons.getStage().draw();
    if (game.getScreen() instanceof DeadScreen) restart();
  }

  @Override
  public void resize(int width, int height) {
    mapPort.update(width, height);
  }

  @Override
  public void dispose() {
    hide();
    buttons.dispose();
    b2dr.dispose();
    batch.dispose();
    map.dispose();
    renderer.dispose();
    world.dispose();
    game.dispose();
    // player.dispose(); // WARN:
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(buttons.getStage()); // so that clicks are processed only by stage
  }

  @Override
  public void hide() {
    buttons.getJoyStick().setUnTouched();
    Main.getMusic().allPause();
  }

  public static boolean isStatsEnabled() {
    return statsEnabled;
  }

  public static boolean isCheatsEnabled() {
    return cheatsEnabled;
  }

  public static PlayGameScreen getLastScreen() {
    return lastScreen;
  }

  public Player getPlayer() {
    return player;
  }

  public Main getGame() {
    return game;
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  public abstract void restart();

  public abstract void setCheckpoint(float x, float y);
}
