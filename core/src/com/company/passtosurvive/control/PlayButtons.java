package com.company.passtosurvive.control;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.view.Main;
import com.company.passtosurvive.view.PauseScreen;

public class PlayButtons
    implements Disposable { // has all the buttons and sticks for level screens, only they have this
  // class
  private static Stage stage;
  private static Table stats, table;
  private static Label deaths, position, speed;
  private static ImageButton pause, jump;
  private static JoyStick joyStick;
  private static BitmapFont font;
  private Main game;
  private final float xMaxSpeed, yMaxSpeed;
  private Player player;

  static {
    stage = new Stage();
    pause = new ImageButton(Main.getButtonSkin(), "pauseButton");
    jump = new ImageButton(Main.getButtonSkin(), "jumpButton");
    joyStick = new JoyStick();
    pause.setSize(158 * Main.getScreenWidth() / 1794, 140 * Main.getScreenHeight() / 1080);
    pause.setPosition(0, 940 * Main.getScreenHeight() / 1080);
    jump.setSize(235 * Main.getScreenWidth() / 1794, 320 * Main.getScreenHeight() / 1080);
    jump.setPosition(1270 * Main.getScreenWidth() / 1794, 40 * Main.getScreenHeight() / 1080);
    stage.addActor(pause);
    stage.addActor(jump);
    stage.addActor(joyStick);
    font = new BitmapFont();
    Label label =
        new Label("DEATHS", new Label.LabelStyle(font, new Color(0, 191, 0, 1)));
    label.setFontScale(4f * Main.getScreenHeight() / 1080);
    deaths =
        new Label(
            Integer.toString(Player.getDeaths()),
            new Label.LabelStyle(font, new Color(253, 238, 0, 1)));
    deaths.setFontScale(4f * Main.getScreenHeight() / 1080);
    table = new Table();
    table.right().top();
    table.setFillParent(true);
    table.add(label).right().padRight(10f * Main.getScreenWidth() / 1794);
    table.row();
    table.add(deaths).right().padRight(10f * Main.getScreenWidth() / 1794);
    stage.addActor(table);
  }

  public static class Builder {
    private final Main game;
    private Player player;
    private float xMaxSpeed, yMaxAccel;

    public Builder(final Main game, Player player) {
      this.game = game;
      this.player = player;
    }

    public Builder setXMaxSpeed(float xMaxSpeed) {
      this.xMaxSpeed = xMaxSpeed;
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
    yMaxSpeed = builder.yMaxAccel;
    joyStick.setUnTouched();
    pause.removeListener(pause.getClickListener());
    pause.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            game.setScreen(new PauseScreen(game));
          }
        });
  }

  public static void createStats() {
    stats = new Table();
    stats.top();
    stats.setFillParent(true);
    position = new Label("", new Label.LabelStyle(font, Color.WHITE));
    position.setFontScale(4f * Main.getScreenHeight() / 1080);
    speed = new Label("", new Label.LabelStyle(font, Color.WHITE));
    speed.setFontScale(4f * Main.getScreenHeight() / 1080);
    stats.add(position);
    stats.row();
    stats.add(speed);
  }

  public void updateDeaths() {
    deaths.setText(Integer.toString(Player.getDeaths()));
  }

  public void updateStats() {
    if (position == null & speed == null) createStats();
    position.setText(
        String.format(
            "Position:(%.3f,%.3f)", player.getBodyPositionX(), player.getBodyPositionY()));
    speed.setText(
        String.format(
            "Speed:[%.3f,%.3f]",
            player.getPlayerBody().getLinearVelocity().x,
            player.getPlayerBody().getLinearVelocity().y));
  }

  // method for controlling the player model & updating stats
  public void update() {
    if (!PlayGameScreen.isCheatsEnabled()) {
      player
          .getPlayerBody()
          .applyLinearImpulse(
              new Vector2(-player.getPlayerBody().getLinearVelocity().x, 0),
              player.getPlayerBody().getWorldCenter(),
              true); // we need the player to stop immediately after releasing the joystick
      player
          .getPlayerBody()
          .applyLinearImpulse(
              new Vector2(joyStick.getValueX() * xMaxSpeed, 0),
              player.getPlayerBody().getWorldCenter(),
              true);
      if (jump.isPressed()) player.jump(yMaxSpeed);
    } else { // cheats
      player
          .getPlayerBody()
          .applyLinearImpulse(
              new Vector2(
                  -player.getPlayerBody().getLinearVelocity().x,
                  -player.getPlayerBody().getLinearVelocity().y),
              player.getPlayerBody().getWorldCenter(),
              true);
      player
          .getPlayerBody()
          .applyLinearImpulse(
              new Vector2(joyStick.getValueX() * 18f, joyStick.getValueY() * 9f),
              player.getPlayerBody().getWorldCenter(),
              true);
    }
    if (PlayGameScreen.isStatsEnabled()) updateStats();
  }

  @Override
  public void dispose() {
    game.dispose();
  }

  public Table getStats() {
    return stats;
  }

  public Stage getStage() {
    return stage;
  }

  public ImageButton getJump() {
    return jump;
  }

  public JoyStick getJoyStick() {
    return joyStick;
  }
}
