package com.company.passtosurvive.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.models.Lava;
import com.company.passtosurvive.models.Player;
import com.company.passtosurvive.models.TileObject;

public class GameOverScreen
    implements Screen { 
  private final Main game;
  private Texture gameOver, label;
  private final SpriteBatch batch;
  private TextureAtlas animationAtlas, buttonAtlas;
  private Stage stage;
  private ImageButton Yes, No;
  private Skin buttonSkin;
  private float animationStateTime, timer;
  private boolean played; // needed to start sound at a certain moment
  private Animation<TextureRegion> animation;
  private final boolean lavaRestarted;

  public GameOverScreen(final Main game, final TileObject whoRestarted) {
    this.game = game;
    this.lavaRestarted = whoRestarted instanceof Lava;
    Player.incrementRestarts();
    batch = new SpriteBatch();
    final Array<TextureRegion> frames = new Array<TextureRegion>();
    if (Player.getRestarts() == 20) {
      label = new Texture("ClickOnScreen.png");
      animationAtlas = new TextureAtlas("Hands.pack");
      for (int i = 1; i <= 13; i++) frames.add(animationAtlas.findRegion("hand" + i));
      animation = new Animation(0.08f, frames);
      frames.clear();
    } else {
      if (lavaRestarted) {
        animationAtlas = new TextureAtlas("Fire.pack");
        for (int i = 1; i <= 6; i++) frames.add(animationAtlas.findRegion("fire" + i));
        animation = new Animation(0.15f, frames);
        frames.clear();
      } else {
        gameOver = new Texture("GameOver.png");
        animationAtlas = new TextureAtlas("Spikes.pack");
        for (int i = 1;
            i <= 37;
            i += 2) // skipped some frames on purpose because the drop would fall too slow
        frames.add(animationAtlas.findRegion("spike" + i));
        for (int i = 38; i <= 48; i++) frames.add(animationAtlas.findRegion("spike" + i));
        animation = new Animation(0.05f, frames);
        frames.clear();
      }
      stage = new Stage();
      label = new Texture("Continue.png");
    buttonAtlas = new TextureAtlas("Buttons.pack");
    buttonSkin = new Skin(Gdx.files.internal("Buttons.json"), buttonAtlas);
      Yes = new ImageButton(buttonSkin, "yesButton");
      No = new ImageButton(buttonSkin, "noButton");
      stage.addActor(Yes);
      stage.addActor(No);
      Gdx.input.setInputProcessor(stage);
      No.addListener(
          new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
              dispose();
              game.setScreen(new MainMenuScreen(game));
            }
          });
      Yes.addListener(
          new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
              dispose();
              game.setScreen(PlayGameScreen.getLastScreen());
            }
          });
      Main.getMusic().gameOverSoundPlay();
    }
  }

  public void fireRender() { // I explained this in slides (.pptx file)
    animationStateTime += Gdx.graphics.getDeltaTime();
    batch.draw(
        animation.getKeyFrame(animationStateTime, true),
        0,
        Main.getScreenHeight() / 2,
        Main.getScreenWidth(),
        350f / (1920f / Main.getScreenWidth()));
  }

  public void spikeRender() { // I explained this in slides (.pptx file)
    animationStateTime += Gdx.graphics.getDeltaTime();
    batch.draw(
        animation.getKeyFrame(animationStateTime, false),
        Main.getScreenWidth() / 2 - (168 / (1794 / Main.getScreenWidth())),
        Main.getScreenHeight() / 2
            - 80 / (1080 / Main.getScreenHeight())
            + 203f / (1920f /Main.getScreenWidth()),
        336 / (1794 / Main.getScreenWidth()),
        336 / (1794 / Main.getScreenWidth()));
  }

  public void handRender() { // I explained this in slides (.pptx file)
    animationStateTime += Gdx.graphics.getDeltaTime();
    batch.draw(
        animation.getKeyFrame(animationStateTime, false),
        -640 / (720 / Main.getScreenHeight()) + (Main.getScreenWidth() / 2),
        0,
        1280 / (720 / Main.getScreenHeight()),
        720 / (720 / Main.getScreenHeight()));
  }

  @Override
  public void render(final float delta) {
    ScreenUtils.clear(0, 0, 0, 1, true); // clean up
    batch.begin();
    timer += delta;
    if (Player.getRestarts() == 20) {
      if (timer >= 3) {
        handRender();
      }
      if (timer >= 7) {
        batch.draw(
            label,
            Main.getScreenWidth() / 2 - (467.5f / (1794 / Main.getScreenWidth())),
            Main.getScreenHeight() / 2 + 200 / (1080 / Main.getScreenHeight()),
            935 / (1794 / Main.getScreenWidth()),
            50.5f / (1794 / Main.getScreenWidth()));
      } else if (timer >= 3.9f && !played) {
        Main.getMusic().handSoundPlay();
        played = true;
      }
      if (Gdx.input.justTouched() && timer >= 7) {
        dispose();
        game.setScreen(PlayGameScreen.getLastScreen());
      }
    } else {
      if (lavaRestarted) {
        fireRender();
        if (timer >= 3) {
          batch.draw(
              label,
              Main.getScreenWidth() / 2 - ((1266.7f / (1794 / Main.getScreenWidth())) / 2),
              Main.getScreenHeight() / 2
                  - 66.6f / (1794 / Main.getScreenWidth())
                  - 50 / (1080 / Main.getScreenHeight()),
              1266.7f / (1794 / Main.getScreenWidth()),
              66.6f / (1794 / Main.getScreenWidth()));
        }
      } else if (!lavaRestarted) {
        batch.draw(
            gameOver,
            0,
            Main.getScreenHeight() / 2 - 80 / (1080 / Main.getScreenHeight()),
            Main.getScreenWidth(),
            203f / (1920f /Main.getScreenWidth()));
        spikeRender();
        if (timer >= 3) {
          batch.draw(
              label,
              Main.getScreenWidth() / 2 - ((1266.7f / (1794 / Main.getScreenWidth())) / 2),
              Main.getScreenHeight() / 2
                  - 80 / (1080 / Main.getScreenHeight())
                  - 66.6f / (1794 / Main.getScreenWidth())
                  - 50 / (1080 / Main.getScreenHeight()),
              1266.7f / (1794 / Main.getScreenWidth()),
              66.6f / (1794 / Main.getScreenWidth()));
        }
      }
    }
    batch.end();
    if (Player.getRestarts() != 20) {
      if (!lavaRestarted && !animation.isAnimationFinished(animationStateTime) || timer < 3) {
        Yes.setVisible(false);
        No.setVisible(false);
      } else if (timer >= 3) {
        // to auto-click `yes` button
        // dispose();
        // game.setScreen(PlayGameScreen.getLastScreen());
        Yes.setVisible(true);
        No.setVisible(true);
      }
      stage.act(delta);
      stage.draw();
    }
  }

  @Override
  public void resize(final int width, final int height) { // I explained this in slides (.pptx file)
    if (Player.getRestarts() != 20) {
      Yes.setSize(141.5f / (1794 / Main.getScreenWidth()), 50 / (1794 / Main.getScreenWidth()));
      No.setSize(141.5f / (1794 / Main.getScreenWidth()), 50 / (1794 / Main.getScreenWidth()));
      Yes.setPosition(
          (Main.getScreenWidth() / 2) - (341.5f / (1794 / Main.getScreenWidth())),
          (Main.getScreenHeight() / 2)
              - (66.6f / (1794 / Main.getScreenWidth()))
              - (180 / (1080 / Main.getScreenHeight()))
              - (100 / (1080 / Main.getScreenHeight())));
      No.setPosition(
          (Main.getScreenWidth() / 2) + (200f / (1794 / Main.getScreenWidth())),
          (Main.getScreenHeight() / 2)
              - (66.6f / (1794 / Main.getScreenWidth()))
              - (180 / (1080 / Main.getScreenHeight()))
              - (100 / (1080 / Main.getScreenHeight())));
    }
  }

  @Override
  public void dispose() {
    if(buttonSkin!=null) buttonSkin.dispose();
    if(buttonAtlas!=null) buttonAtlas.dispose();
    label.dispose();
    if (gameOver != null) gameOver.dispose();
    animationAtlas.dispose();
    batch.dispose();
    if (stage != null) stage.dispose();
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
