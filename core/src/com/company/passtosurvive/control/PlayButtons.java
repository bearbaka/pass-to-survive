package com.company.passtosurvive.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.view.Main;
import com.company.passtosurvive.view.PauseScreen;

import lombok.Getter;
import lombok.Builder;

public class PlayButtons
    implements Disposable { // has all the buttons and sticks for level screens, only they have this
                            // class
  @Getter private Stage stage;
  @Getter private Table stats;
  private Table table;
  private Label restarts, position, speed;
  private TextureAtlas buttonAtlas;
  private Skin buttonSkin;
  @Getter private ImageButton jump;
  private ImageButton pause;
  @Getter private Joystick joystick;
  private BitmapFont font;
  public void createStats() {
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
  private Main game;

  private final float xMaxSpeed, yMaxAccel;

  private Player player;

  @Builder
  public PlayButtons(Main game, float xMaxSpeed, float yMaxAccel, Player player) {
    this.game = game;
    this.player = player;
    this.xMaxSpeed = xMaxSpeed;
    this.yMaxAccel = yMaxAccel;
    buttonAtlas = new TextureAtlas("Buttons.pack");
    buttonSkin = new Skin(Gdx.files.internal("Buttons.json"), buttonAtlas);
    stage = new Stage();
    pause = new ImageButton(buttonSkin, "pauseButton");
    jump = new ImageButton(buttonSkin, "jumpButton");
    joystick = new Joystick();
    pause.setSize(158 * Main.getScreenWidth() / 1794, 140 * Main.getScreenHeight() / 1080);
    pause.setPosition(0, 940 * Main.getScreenHeight() / 1080);
    jump.setSize(235 * Main.getScreenWidth() / 1794, 320 * Main.getScreenHeight() / 1080);
    jump.setPosition(1270 * Main.getScreenWidth() / 1794, 40 * Main.getScreenHeight() / 1080);
    stage.addActor(pause);
    stage.addActor(jump);
    stage.addActor(joystick);
    font = new BitmapFont();
    Label label = new Label("RESTARTS", new Label.LabelStyle(font, new Color(0, 191, 0, 1)));
    label.setFontScale(4f * Main.getScreenHeight() / 1080);
    restarts =
        new Label(
            Integer.toString(Player.getRestarts()),
            new Label.LabelStyle(font, new Color(253, 238, 0, 1)));
    restarts.setFontScale(4f * Main.getScreenHeight() / 1080);
    table = new Table();
    table.right().top();
    table.setFillParent(true);
    table.add(label).right().padRight(10f * Main.getScreenWidth() / 1794);
    table.row();
    table.add(restarts).right().padRight(10f * Main.getScreenWidth() / 1794);
    stage.addActor(table);
    joystick.setUnTouched();
    pause.removeListener(pause.getClickListener());
    pause.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            game.setScreen(new PauseScreen(game));
          }
        });
  }

  public void reset() {
    joystick.remove();
    joystick.dispose();
    joystick=new Joystick();
    jump.remove();
    jump = new ImageButton(buttonSkin, "jumpButton");
    jump.setSize(235 * Main.getScreenWidth() / 1794, 320 * Main.getScreenHeight() / 1080);
    jump.setPosition(1270 * Main.getScreenWidth() / 1794, 40 * Main.getScreenHeight() / 1080);
    stage.addActor(joystick);
    stage.addActor(jump);
    restarts.setText(Integer.toString(Player.getRestarts()));
  }

  public void updateStats() {
    if (position == null & speed == null) createStats();
    position.setText(
        String.format(
            "Position:(%.3f,%.3f)", player.getPosition().x, player.getPosition().y));
    speed.setText(
        String.format(
            "Speed:[%.3f,%.3f]",
            player.getLinearVelocity().x,
            player.getLinearVelocity().y));
  }

  // method for controlling the player model & updating stats
  public void update() {
    if (!PlayGameScreen.isCheatsEnabled()) {
      player
          .applyLinearImpulse(
              new Vector2(-player.getLinearVelocity().x, 0),
              player.getWorldCenter(),
              true); // we need the player to stop immediately after releasing the joystick
      player
          .applyLinearImpulse(
              new Vector2(joystick.getValueX() * xMaxSpeed, 0),
              player.getWorldCenter(),
              true);
      if (jump.isPressed()) player.jump(yMaxAccel);
    } else { // cheats
      player
          .setLinearVelocity(joystick.getValueX() * 18f, joystick.getValueY() * 9f);
    }
    if (PlayGameScreen.isStatsEnabled()) updateStats();
  }
  @Override
  public void dispose() {
    buttonSkin.dispose();
    buttonAtlas.dispose();
    joystick.dispose();
    font.dispose();
    stage.dispose();
    game.dispose();
  }
}
