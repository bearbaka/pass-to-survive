package com.company.passtosurvive.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.view.Main;
import com.company.passtosurvive.view.PauseScreen;

public class PlayButtons implements Disposable { // has all the buttons and sticks for level
                                                 // screens, only they have this class
  private final Main game;
  public Stage stage;
  public ImageButton pause, jump;
  private Skin skin;
  private TextureAtlas atlas;
  public JoyStick joyStick;
  private Player player;
  private final float xMaxSpeed, xMaxAccel, yMaxAccel;

  public static class Builder {
    private final Main game;
    private Player player;
    private float xMaxSpeed, xMaxAccel, yMaxAccel;

    public Builder(final Main game, Player player) {
      this.game = game;
      this.player = player;
    }

    public Builder setXMaxSpeed(float xMaxSpeed) {
      this.xMaxSpeed = xMaxSpeed;
      return this;
    }

    public Builder setXMaxAccel(float xMaxAccel) {
      this.xMaxAccel = xMaxAccel;
      return this;
    }

    public Builder setYMaxAccel(float yMaxAccel) {
      this.yMaxAccel = yMaxAccel;
      return this;
    }

    public PlayButtons build() {
      return new PlayButtons(this);
    }
  }

  public PlayButtons(Builder builder) {
    game = builder.game;
    player = builder.player;
    xMaxSpeed = builder.xMaxSpeed;
    xMaxAccel = builder.xMaxAccel;
    yMaxAccel = builder.yMaxAccel;
    stage = new Stage();
    atlas = new TextureAtlas("AllComponents.pack");
    skin = new Skin(Gdx.files.internal("Buttons.json"), atlas);
    pause = new ImageButton(skin, "default4");
    jump = new ImageButton(skin, "default2");
    joyStick = new JoyStick();
    pause.setSize(158 * Main.getWidth() / 1794, 140 * Main.getHeight() / 1080);
    pause.setPosition(0, 940 * Main.getHeight() / 1080);
    pause.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new PauseScreen(game));
      }
    });
    jump.setSize(235 * Main.getWidth() / 1794, 320 * Main.getHeight() / 1080);
    jump.setPosition(1270 * Main.getWidth() / 1794, 40 * Main.getHeight() / 1080);
    stage.addActor(pause);
    stage.addActor(jump);
    stage.addActor(joyStick);
    Gdx.input.setInputProcessor(stage); // so that clicks are processed only by stage
  }

  // method for controlling the player model
  public void handling() {
    if (!PlayGameScreen.isCheatsEnabled()) {
      if (jump.isPressed())
        player.jump(yMaxAccel);
      if (joyStick.isJoyStickDown()) {
        if (joyStick.getValueX() > 0 && player.playerBody.getLinearVelocity().x <= xMaxSpeed) { // xMaxSpeed
                                                                                                // is
                                                                                                // the
                                                                                                // max.
                                                                                                // speed
                                                                                                // xMaxAccel
                                                                                                // as
                                                                                                // acceleration
          player.playerBody.applyLinearImpulse(new Vector2(xMaxAccel, 0),
              player.playerBody.getWorldCenter(), true);
        } else if (joyStick.getValueX() < 0
            && player.playerBody.getLinearVelocity().x >= -xMaxSpeed) {
          player.playerBody.applyLinearImpulse(new Vector2(-xMaxAccel, 0),
              player.playerBody.getWorldCenter(), true);
        }
      } else if (player.playerBody.getLinearVelocity().x != 0) { // we need the player to stop
                                                                 // immediately after releasing
                                                                 // the joystick
        player.playerBody.applyLinearImpulse(
            new Vector2(-player.playerBody.getLinearVelocity().x, 0),
            player.playerBody.getWorldCenter(), true);
      }
    } else { // cheats
      if (joyStick.isJoyStickDown()) {
        if (joyStick.getValueX() > 0 && player.playerBody.getLinearVelocity().x <= 3f) { // xMaxSpeed
                                                                                                // is
                                                                                                // the
                                                                                                // max.
                                                                                                // speed
                                                                                                // xMaxAccel
                                                                                                // as
                                                                                                // acceleration
          player.playerBody.applyLinearImpulse(new Vector2(0.3f, 0),
              player.playerBody.getWorldCenter(), true);
        } else if (joyStick.getValueX() < 0
            && player.playerBody.getLinearVelocity().x >= -3f) {
          player.playerBody.applyLinearImpulse(new Vector2(-0.3f, 0),
              player.playerBody.getWorldCenter(), true);
        }
        if (joyStick.getValueY() > 0 && player.playerBody.getLinearVelocity().y <= 3f) { // xMaxSpeed
                                                                                                // is
                                                                                                // the
                                                                                                // max.
                                                                                                // speed
                                                                                                // xMaxAccel
                                                                                                // as
                                                                                                // acceleration
          player.playerBody.applyLinearImpulse(new Vector2(0, 0.3f),
              player.playerBody.getWorldCenter(), true);
        } else if (joyStick.getValueY() < 0
            && player.playerBody.getLinearVelocity().y >= -3f) {
          player.playerBody.applyLinearImpulse(new Vector2(0, -0.3f),
              player.playerBody.getWorldCenter(), true);
        }
      } else {
        player.playerBody.applyLinearImpulse(
            new Vector2(-player.playerBody.getLinearVelocity().x, -player.playerBody.getLinearVelocity().y),
            player.playerBody.getWorldCenter(), true);
      }
    }
  }

  @Override
  public void dispose() {
    joyStick.dispose();
    stage.dispose();
    skin.dispose();
    atlas.dispose();
    game.dispose();
  }
}
