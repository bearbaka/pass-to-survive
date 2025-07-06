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
    implements Screen { // the pause menu is launched when the pause button is
                        // pressed in the game
  final Main game;
  private TextureAtlas atlas;
  private Skin skin;
  private Stage stage;
  private ImageButton resume, restart, mainMenu;

  public PauseScreen(final Main game) {
    this.game = game;
    stage = new Stage();
    atlas = new TextureAtlas("AllComponents.pack");
    skin = new Skin(Gdx.files.internal("Buttons.json"), atlas);
    resume = new ImageButton(skin, "default5");
    restart = new ImageButton(skin, "default6");
    mainMenu = new ImageButton(skin, "default3");
    Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(Color.BLACK);
    pixmap.fillRectangle(0, 0, 1, 1);
    Texture blackBackground = new Texture(pixmap);
    pixmap.dispose();
    Image transparentBlackBackground = new Image(blackBackground);
    transparentBlackBackground.setSize(Main.getScreenWidth(), Main.getScreenHeight());
    transparentBlackBackground.getColor().a = .1f;
    stage.addActor(transparentBlackBackground);
    stage.addActor(restart);
    stage.addActor(resume);
    stage.addActor(mainMenu);
    Gdx.input.setInputProcessor(stage);
    resume.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        dispose();
        // game.setPreviousLevel();
        game.setScreen(PlayGameScreen.getLastScreen());
      }
    });
    restart.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        dispose();
        PlayGameScreen.getLastScreen().restart();
        game.setScreen(PlayGameScreen.getLastScreen());
      }
    });
    mainMenu.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        dispose();
        game.setScreen(new MainMenuScreen(game));
      }
    });
  }

  @Override
  public void show() {}

  @Override
  public void render(float delta) {
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width,
                     int height) { // in the presentation I explained
                                   // why I divide 1794 by Main.getScreenWidth()
    resume.setSize(700 / (1794 / Main.getScreenWidth()),
                   234f / (1794 / Main.getScreenWidth()));
    restart.setSize(700 / (1794 / Main.getScreenWidth()),
                    234f / (1794 / Main.getScreenWidth()));
    mainMenu.setSize(700 / (1794 / Main.getScreenWidth()),
                     234f / (1794 / Main.getScreenWidth()));
    resume.setPosition((Main.getScreenWidth() / 2) - ((resume.getWidth()) / 2),
                       (Main.getScreenHeight() / 2) +
                           150f / (1794 / Main.getScreenWidth()));
    restart.setPosition((Main.getScreenWidth() / 2) - ((resume.getWidth()) / 2),
                        (Main.getScreenHeight() / 2) -
                            100f / (1794 / Main.getScreenWidth()));
    mainMenu.setPosition((Main.getScreenWidth() / 2) - ((resume.getWidth()) / 2),
                         (Main.getScreenHeight() / 2) -
                             350f / (1794 / Main.getScreenWidth()));
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}

  @Override
  public void dispose() {
    stage.dispose();
    atlas.dispose();
    skin.dispose();
    game.dispose();
  }
}
