package com.company.passtosurvive.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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

public class DeadScreen
    implements Screen { // GameOver screen starts when the player falls into
                        // lava or dies from spikes
  final Main game;
  private Texture gameOver, label;
  private SpriteBatch batch;
  private TextureAtlas atlas;
  private Stage stage;
  private Skin skin;
  private ImageButton Yes, No;
  private float stateTime, stateTimer; // stateTimer is needed to count time
  private boolean played; // needed to start sound at a certain moment
  private Animation<TextureRegion> animation;
  private boolean lavaKilled;

  public DeadScreen(final Main game, TileObject whoKilled) {
    this.game = game;
    this.lavaKilled = whoKilled instanceof Lava;
    Player.incrementDeaths();
    batch = new SpriteBatch();
    Array<TextureRegion> Frames = new Array<TextureRegion>();
    if (Player.getDeaths()==20) {
      label = new Texture("ClickOScr.png");
      atlas = new TextureAtlas("Ghoul.pack");
      for (int i = 1; i <= 13; i++)
        Frames.add(atlas.findRegion("Ghoul" + i));
      animation = new Animation(0.08f, Frames);
      Frames.clear();
    } else {
      if (lavaKilled) {
        atlas = new TextureAtlas("Fire.pack");
        for (int i = 1; i <= 6; i++)
          Frames.add(atlas.findRegion("fire" + i));
        animation = new Animation(0.15f, Frames);
        Frames.clear();
      } else {
        gameOver = new Texture("gameOver.png");
        atlas = new TextureAtlas("BloodySpikes.pack");
        for (int i = 1; i <= 37; i += 2) // skipped some frames on purpose
                                         // because the drop would fall too slow
          Frames.add(atlas.findRegion("spike" + i));
        for (int i = 38; i <= 48; i++)
          Frames.add(atlas.findRegion("spike" + i));
        animation = new Animation(0.05f, Frames);
        Frames.clear();
      }
      stage = new Stage();
      atlas = new TextureAtlas("AllComponents.pack");
      label = new Texture("continue.png");
      skin = new Skin(Gdx.files.internal("Buttons.json"), atlas);
      Yes = new ImageButton(skin, "default8");
      No = new ImageButton(skin, "default7");
      stage.addActor(Yes);
      stage.addActor(No);
      Gdx.input.setInputProcessor(stage);
      No.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          dispose();
          game.setScreen(new MainMenuScreen(game));
        }
      });
      Yes.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          dispose();
          game.setScreen(PlayGameScreen.getLastScreen());
        }
      });
      Main.getMusic().gameOverSoundPlay();
    }
  }

  public void fireRender() { // I explained this in slides (.pptx file)
    stateTime += Gdx.graphics.getDeltaTime();
    batch.draw(animation.getKeyFrame(stateTime, true), 0, Main.getScreenHeight() / 2,
               Main.getScreenWidth(), Main.getScreenHeight() / 3f);
  }

  public void bloodySpikeRender() { // I explained this in slides (.pptx file)
    stateTime += Gdx.graphics.getDeltaTime();
    batch.draw(animation.getKeyFrame(stateTime, false),
               Main.getScreenWidth() / 2 - (168 / (1794 / Main.getScreenWidth())),
               Main.getScreenHeight() / 2 - 80 / (1080 / Main.getScreenHeight()) +
                   Main.getScreenHeight() / 5.1f,
               336 / (1794 / Main.getScreenWidth()), 336 / (1794 / Main.getScreenWidth()));
  }

  public void handRender() { // I explained this in slides (.pptx file)
    stateTime += Gdx.graphics.getDeltaTime();
    batch.draw(animation.getKeyFrame(stateTime, false),
               -640 / (720 / Main.getScreenHeight()) + (Main.getScreenWidth() / 2), 0,
               1280 / (720 / Main.getScreenHeight()), 720 / (720 / Main.getScreenHeight()));
  }

  @Override
  public void show() {}

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1, true); // clean up
    batch.begin();
    stateTimer += delta;
    if (Player.getDeaths() == 20) {
      if (stateTimer >= 3) {
        handRender();
      }
      if (stateTimer >= 7) {
        batch.draw(
            label, Main.getScreenWidth() / 2 - (467.5f / (1794 / Main.getScreenWidth())),
            Main.getScreenHeight() / 2 + 200 / (1080 / Main.getScreenHeight()),
            935 / (1794 / Main.getScreenWidth()), 50.5f / (1794 / Main.getScreenWidth()));
      } else if (stateTimer >= 4f && !played) {
        Main.getMusic().ghoulSoundPlay();
        played = true;
      }
      if (Gdx.input.justTouched() && stateTimer >= 7) {
        dispose();
        game.setScreen(PlayGameScreen.getLastScreen());
      }
    }
    else {
      if (lavaKilled) {
        fireRender();
        if (stateTimer >= 3) {
          batch.draw(label,
                     Main.getScreenWidth() / 2 -
                         ((1266.7f / (1794 / Main.getScreenWidth())) / 2),
                     Main.getScreenHeight() / 2 - 66.6f / (1794 / Main.getScreenWidth()) -
                         50 / (1080 / Main.getScreenHeight()),
                     1266.7f / (1794 / Main.getScreenWidth()),
                     66.6f / (1794 / Main.getScreenWidth()));
        }
      } else if (!lavaKilled) {
        batch.draw(gameOver, 0,
                   Main.getScreenHeight() / 2 - 80 / (1080 / Main.getScreenHeight()),
                   Main.getScreenWidth(), Main.getScreenHeight() / 3f);
        bloodySpikeRender();
        if (stateTimer >= 3) {
          batch.draw(label,
                     Main.getScreenWidth() / 2 -
                         ((1266.7f / (1794 / Main.getScreenWidth())) / 2),
                     Main.getScreenHeight() / 2 - 80 / (1080 / Main.getScreenHeight()) -
                         66.6f / (1794 / Main.getScreenWidth()) -
                         50 / (1080 / Main.getScreenHeight()),
                     1266.7f / (1794 / Main.getScreenWidth()),
                     66.6f / (1794 / Main.getScreenWidth()));
        }
      }
    }
    batch.end();
    if (Player.getDeaths() != 20) {
      if (!lavaKilled && !animation.isAnimationFinished(stateTime) ||
          stateTimer < 3) {
        Yes.setVisible(false);
        No.setVisible(false);
      } else if (stateTimer >= 3) {
        Yes.setVisible(true);
        No.setVisible(true);
      }
      stage.act(delta);
      stage.draw();
    }
  }

  @Override
  public void resize(int width,
                     int height) { // I explained this in slides (.pptx file)
    if (Player.getDeaths() != 20) {
      Yes.setSize(141.5f / (1794 / Main.getScreenWidth()),
                  50 / (1794 / Main.getScreenWidth()));
      No.setSize(141.5f / (1794 / Main.getScreenWidth()),
                 50 / (1794 / Main.getScreenWidth()));
      Yes.setPosition(
          (Main.getScreenWidth() / 2) - (341.5f / (1794 / Main.getScreenWidth())),
          (Main.getScreenHeight() / 2) - (66.6f / (1794 / Main.getScreenWidth())) -
              (180 / (1080 / Main.getScreenHeight())) -
              (100 / (1080 / Main.getScreenHeight())));
      No.setPosition((Main.getScreenWidth() / 2) + (200f / (1794 / Main.getScreenWidth())),
                     (Main.getScreenHeight() / 2) -
                         (66.6f / (1794 / Main.getScreenWidth())) -
                         (180 / (1080 / Main.getScreenHeight())) -
                         (100 / (1080 / Main.getScreenHeight())));
    }
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}

  @Override
  public void dispose() {
    game.dispose();
    label.dispose();
    batch.dispose();
    atlas.dispose();
    if(gameOver!=null) gameOver.dispose();
    stage.dispose();
    skin.dispose();
  }
}
