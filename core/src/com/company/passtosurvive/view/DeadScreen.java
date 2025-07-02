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

  public DeadScreen(final Main game, boolean lavaKilled) {
    this.game = game;
    this.lavaKilled = lavaKilled;
    Main.playerX = 0;
    Main.playerY = 0;
    Main.deaths++;
    batch = new SpriteBatch();
    Array<TextureRegion> Frames = new Array<TextureRegion>();
    if (Main.deaths == 20) {
      label = new Texture("ClickOScr.png");
      atlas = new TextureAtlas("Ghoul.pack");
      for (int i = 1; i <= 13; i++)
        Frames.add(atlas.findRegion("Ghoul" + i));
      animation = new Animation(0.08f, Frames);
      Frames.clear();
    } else if (Main.deaths != 20) {
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
          game.setPreviousLevel();
        }
      });
      Main.getMusic().gameOverSoundPlay();
    }
  }

  public void fireRender() { // I explained this in slides (.pptx file)
    stateTime += Gdx.graphics.getDeltaTime();
    batch.draw(animation.getKeyFrame(stateTime, true), 0, Main.getHeight() / 2,
               Main.getWidth(), Main.getHeight() / 3f);
  }

  public void bloodySpikeRender() { // I explained this in slides (.pptx file)
    stateTime += Gdx.graphics.getDeltaTime();
    batch.draw(animation.getKeyFrame(stateTime, false),
               Main.getWidth() / 2 - (168 / (1794 / Main.getWidth())),
               Main.getHeight() / 2 - 80 / (1080 / Main.getHeight()) +
                   Main.getHeight() / 5.1f,
               336 / (1794 / Main.getWidth()), 336 / (1794 / Main.getWidth()));
  }

  public void handRender() { // I explained this in slides (.pptx file)
    stateTime += Gdx.graphics.getDeltaTime();
    batch.draw(animation.getKeyFrame(stateTime, false),
               -640 / (720 / Main.getHeight()) + (Main.getWidth() / 2), 0,
               1280 / (720 / Main.getHeight()), 720 / (720 / Main.getHeight()));
  }

  @Override
  public void show() {}

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1, true); // clean up
    batch.begin();
    stateTimer += delta;
    if (Main.deaths != 20) {
      if (lavaKilled) {
        fireRender();
        if (stateTimer >= 3) {
          batch.draw(label,
                     Main.getWidth() / 2 -
                         ((1266.7f / (1794 / Main.getWidth())) / 2),
                     Main.getHeight() / 2 - 66.6f / (1794 / Main.getWidth()) -
                         50 / (1080 / Main.getHeight()),
                     1266.7f / (1794 / Main.getWidth()),
                     66.6f / (1794 / Main.getWidth()));
        }
      } else if (!lavaKilled) {
        batch.draw(gameOver, 0,
                   Main.getHeight() / 2 - 80 / (1080 / Main.getHeight()),
                   Main.getWidth(), Main.getHeight() / 3f);
        bloodySpikeRender();
        if (stateTimer >= 3) {
          batch.draw(label,
                     Main.getWidth() / 2 -
                         ((1266.7f / (1794 / Main.getWidth())) / 2),
                     Main.getHeight() / 2 - 80 / (1080 / Main.getHeight()) -
                         66.6f / (1794 / Main.getWidth()) -
                         50 / (1080 / Main.getHeight()),
                     1266.7f / (1794 / Main.getWidth()),
                     66.6f / (1794 / Main.getWidth()));
        }
      }
    } else if (Main.deaths == 20) {
      if (stateTimer >= 3) {
        handRender();
      }
      if (stateTimer >= 7) {
        batch.draw(
            label, Main.getWidth() / 2 - (467.5f / (1794 / Main.getWidth())),
            Main.getHeight() / 2 + 200 / (1080 / Main.getHeight()),
            935 / (1794 / Main.getWidth()), 50.5f / (1794 / Main.getWidth()));
      } else if (stateTimer >= 4f && !played) {
        Main.getMusic().ghoulSoundPlay();
        played = true;
      }
      if (Gdx.input.justTouched() && stateTimer >= 7) {
        Main.playerY = 0;
        Main.playerX = 0;
        dispose();
        game.setPreviousLevel();
      }
    }
    batch.end();
    if (Main.deaths != 20) {
      if (!lavaKilled && !animation.isAnimationFinished(stateTime) ||
          stateTimer < 3) {
        Yes.setVisible(false);
        No.setVisible(false);
      } else if (stateTimer >= 3 && Main.deaths != 20 ||
                 stateTimer >= 3 && Main.deaths != 20) {
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
    if (Main.deaths != 20) {
      Yes.setSize(141.5f / (1794 / Main.getWidth()),
                  50 / (1794 / Main.getWidth()));
      No.setSize(141.5f / (1794 / Main.getWidth()),
                 50 / (1794 / Main.getWidth()));
      Yes.setPosition(
          (Main.getWidth() / 2) - (341.5f / (1794 / Main.getWidth())),
          (Main.getHeight() / 2) - (66.6f / (1794 / Main.getWidth())) -
              (180 / (1080 / Main.getHeight())) -
              (100 / (1080 / Main.getHeight())));
      No.setPosition((Main.getWidth() / 2) + (200f / (1794 / Main.getWidth())),
                     (Main.getHeight() / 2) -
                         (66.6f / (1794 / Main.getWidth())) -
                         (180 / (1080 / Main.getHeight())) -
                         (100 / (1080 / Main.getHeight())));
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
