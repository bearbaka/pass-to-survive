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
import com.company.passtosurvive.models.Player.State;
import com.company.passtosurvive.view.Main;

public abstract class PlayGameScreen implements Screen { // children of this class are only level
                                                         // screens
  private static PlayGameScreen lastScreen;

  private static boolean cheatsEnabled;
  public static boolean isCheatsEnabled() {
    return cheatsEnabled;
  }

  static {
    cheatsEnabled = false;
  }

  public static PlayGameScreen getLastScreen() {
    return lastScreen;
  }

  Player player; // create Player here for WorldContactListener

  public Player getPlayer() {
    return player;
  }

  final Main game;

  public Main getGame() {
    return game;
  }

  Box2DDebugRenderer b2dr;
  World world;
  SpriteBatch batch;
  TiledMap map;
  TmxMapLoader mapLoader;
  PlayButtons buttons;
  OrthographicCamera cam;
  OrthogonalTiledMapRenderer renderer;
  Viewport mapPort;
  final float xMaxSpeed, xMaxAccel, yMaxAccel, bouncerYMaxAccel, gravity;

  static class Builder {
    private Main game;
    private float xMaxSpeed, xMaxAccel, yMaxAccel, bouncerYMaxAccel, gravity;

    Builder(Main game, int screen) {
      this.game = game;
      Main.screen = screen;
    }

    Builder setXMaxSpeed(float xMaxSpeed) {
      this.xMaxSpeed = xMaxSpeed;
      return this;
    }

    Builder setXMaxAccel(float xMaxAccel) {
      this.xMaxAccel = xMaxAccel;
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
    this.xMaxAccel = builder.xMaxAccel;
    this.yMaxAccel = builder.yMaxAccel;
    this.bouncerYMaxAccel = builder.bouncerYMaxAccel;
    this.gravity=builder.gravity;
    batch = new SpriteBatch();
  }

  // needed to update the textures of the
  // Player sprite, camera, buttons, physics, collision, music, etc.
  public void update(float delta) {
    if(Gdx.input.isKeyJustPressed(Keys.C)){
      if(cheatsEnabled) world.setGravity(new Vector2(0, gravity));
      else world.setGravity(new Vector2(0,0));
      cheatsEnabled=!cheatsEnabled;
    }
    if (Main.musicIsOn && !Main.getMusic().isAnyMusicPlaying()) {
      if (Main.screen <= 2)
        Main.getMusic().level1MusicPlay();
      else
        Main.getMusic().level2MusicPlay();
    }
    world.step(1 / 60f, 6, 2);
    if (Main.nextFloor) { // on this map the last bouncers at the end of this
                          // part of the level are marked as nextfloor and when
                          // we fall on them Main.nextFloor is set to true
      if (player.playerBody.getLinearVelocity().y == 0)
        player.performJump(10f);
      cam.position.y = player.playerBody.getPosition().y;
    } else { // after we fall on something else this will start
      cam.position.y = mapPort.getWorldHeight() / 2; // camera returns to original position Y
    }
    cam.position.x = player.playerBody.getPosition().x; // camera moves with player
    player.update(delta);
    cam.update();
    renderer.setView(cam);
    if (Main.touchedBouncer && player.getCurrentState() != State.JUMPING) {
      player.setCurrentState(State.JUMPING);
      player.performJump(bouncerYMaxAccel);
    }
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
    buttons.stage.act(delta);
    buttons.stage.draw();
    if (player.isDead())
      dispose();
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
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(buttons.stage); // so that clicks are processed only by stage
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {
    Main.getMusic().allPause();
  }
}
