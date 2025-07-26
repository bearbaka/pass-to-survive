package com.company.passtosurvive.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.company.passtosurvive.levels.PlayGameScreen;

public class PauseScreen
    implements Screen { // the pause menu is launched when the pause button is pressed in the game
  private final Main game;
  private final Stage stage;
  private TextureAtlas buttonAtlas;
  private Skin buttonSkin;
  private final ImageButton resume, restart, mainMenu;

  public PauseScreen(final Main game) {
    this.game = game;
    buttonAtlas = new TextureAtlas("Buttons.pack");
    buttonSkin = new Skin(Gdx.files.internal("Buttons.json"), buttonAtlas);
    stage = new Stage();
    resume = new ImageButton(buttonSkin, "resumeButton");
    restart = new ImageButton(buttonSkin, "restartButton");
    mainMenu = new ImageButton(buttonSkin, "mainMenuButton");
    final Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(Color.BLACK);
    pixmap.fillRectangle(0, 0, 1, 1);
    final Texture blackBackground = new Texture(pixmap);
    pixmap.dispose();
    final Image transparentBlackBackground = new Image(blackBackground);
    blackBackground.dispose();
    transparentBlackBackground.setSize(Main.getScreenWidth(), Main.getScreenHeight());
    transparentBlackBackground.getColor().a = .1f;
    stage.addActor(transparentBlackBackground);
    stage.addActor(restart);
    stage.addActor(resume);
    stage.addActor(mainMenu);
    Gdx.input.setInputProcessor(stage);
    resume.addListener(
        new ClickListener() {
          @Override
          public void clicked(final InputEvent event, final float x, final float y) {
            dispose();
            game.setScreen(PlayGameScreen.getLastScreen());
          }
        });
    restart.addListener(
        new ClickListener() {
          @Override
          public void clicked(final InputEvent event, final float x, final float y) {
            dispose();
            PlayGameScreen.getLastScreen().restart();
            game.setScreen(PlayGameScreen.getLastScreen());
          }
        });
    mainMenu.addListener(
        new ClickListener() {
          @Override
          public void clicked(final InputEvent event, final float x, final float y) {
            dispose();
            game.setScreen(new MainMenuScreen(game));
          }
        });
  }

  @Override
  public void render(final float delta) {
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(
      final int width,
      final int height) { // Explained in Github repo
    resume.setSize(700 / (1794 / Main.getScreenWidth()), 234f / (1794 / Main.getScreenWidth()));
    restart.setSize(700 / (1794 / Main.getScreenWidth()), 234f / (1794 / Main.getScreenWidth()));
    mainMenu.setSize(700 / (1794 / Main.getScreenWidth()), 234f / (1794 / Main.getScreenWidth()));
    resume.setPosition(
        (Main.getScreenWidth() / 2) - ((resume.getWidth()) / 2),
        (Main.getScreenHeight() / 2) + 150f / (1794 / Main.getScreenWidth()));
    restart.setPosition(
        (Main.getScreenWidth() / 2) - ((resume.getWidth()) / 2),
        (Main.getScreenHeight() / 2) - 100f / (1794 / Main.getScreenWidth()));
    mainMenu.setPosition(
        (Main.getScreenWidth() / 2) - ((resume.getWidth()) / 2),
        (Main.getScreenHeight() / 2) - 350f / (1794 / Main.getScreenWidth()));
  }

  @Override
  public void dispose() {
    buttonSkin.dispose();
    buttonAtlas.dispose();
    stage.dispose();
    game.dispose();
  }

  @Override
  public void show() {}

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}
}
