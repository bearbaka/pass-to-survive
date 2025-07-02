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
import com.company.passtosurvive.levels.Level1ScreenPart1;
import com.company.passtosurvive.levels.Level2ScreenFloor1;

public class WinScreen
    implements Screen { // the victory screen is launched after completing the
                        // level completely
  final Main game;
  private Texture label, Thanks;
  private SpriteBatch batch;
  private TextureAtlas atlas;
  private Stage stage;
  private Skin skin;
  private ImageButton Yes, No;
  private float stateTime, stateTimer; // stateTimer is needed to count time
   private Animation<TextureRegion> animation;

  public WinScreen(final Main game) {
    this.game = game;
    batch = new SpriteBatch();
    if (Main.level1IsFinished &&
        Main.level2IsFinished) { // when player have completely completed
                                 // the game
      label = new Texture("GameOverThanks.png");
      Thanks = new Texture("thforpl.png");
    } else if (Main.level1IsFinished && !Main.level2IsFinished ||
               Main.level2IsFinished &&
                   !Main.level1IsFinished) { // when one of the levels has
                                             // passed
      stage = new Stage();
      label = new Texture("continueWhite.png");
      atlas = new TextureAtlas("AllComponents.pack");
      skin = new Skin(Gdx.files.internal("Buttons.json"), atlas);
      atlas = new TextureAtlas("YOUWIN.pack");
      Array<TextureRegion> Frames = new Array<TextureRegion>();
      for (int i = 1; i <= 15; i++)
        Frames.add(atlas.findRegion("YOUWIN" + i));
      animation = new Animation(0.2f, Frames);
      Frames.clear();
      Yes = new ImageButton(skin, "default14");
      Yes.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          Main.playerY = 0;
          Main.playerX = 0;
          Main.playerCheckpointX = 0;
          Main.playerCheckpointY = 0;
          dispose();
          if (Main.level1IsFinished) {
            game.setScreen(new Level2ScreenFloor1(game));
          } else if (Main.level2IsFinished) {
            game.setScreen(new Level1ScreenPart1(game));
          }
        }
      });
      No = new ImageButton(skin, "default15");
      No.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          dispose();
          game.setScreen(new MainMenuScreen(game));
        }
      });
      stage.addActor(Yes);
      stage.addActor(No);
      Gdx.input.setInputProcessor(
          stage); // so that clicks are processed only by stage
    }
    if (Main.level1IsFinished && !Main.level2IsFinished ||
        Main.level2IsFinished && !Main.level1IsFinished) {
      Main.getMusic().winSoundPlay();
    }
  }

  public void winRender() {
    stateTime += Gdx.graphics.getDeltaTime();
    batch.draw(animation.getKeyFrame(stateTime, true), 0,
               Main.getHeight() / 2, Main.getWidth(), Main.getHeight() / 3f);
  }

  @Override
  public void show() {}

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1, true); // clean up
    batch.begin();
    stateTimer += delta;
    if (Main.level1IsFinished && !Main.level2IsFinished ||
        Main.level2IsFinished && !Main.level1IsFinished) {
      winRender();
      if (stateTimer >= 3) {
        batch.draw(label,
                   Main.getWidth() / 2 - ((1266.7f / (1794 / Main.getWidth())) / 2),
                   Main.getHeight() / 2 - 80 / (1080 / Main.getHeight()) -
                       66.6f / (1794 / Main.getWidth()) - 50 / (1080 / Main.getHeight()),
                   1266.7f / (1794 / Main.getWidth()), 66.6f / (1794 / Main.getWidth()));
      }
    } else if (Main.level1IsFinished && Main.level2IsFinished) {
      batch.draw(label, 0, Main.getHeight() / 2, Main.getWidth(),
                 203 / (1794 / Main.getWidth()));
      if (stateTimer >= 7) {
        if (stateTimer==7) Main.getMusic().endSoundPlay();
        batch.draw(Thanks,
                   Main.getWidth() / 2 - ((1266.7f / (1794 / Main.getWidth())) / 2),
                   Main.getHeight() / 2 - 80 / (1080 / Main.getHeight()) -
                       66.6f / (1794 / Main.getWidth()) - 50 / (1080 / Main.getHeight()),
                   1266.7f / (1794 / Main.getWidth()), 66.6f / (1794 / Main.getWidth()));
      }
    }
    batch.end();
    if (Main.level1IsFinished && !Main.level2IsFinished ||
        Main.level2IsFinished && !Main.level1IsFinished) {
      if (Main.level1IsFinished && !Main.level2IsFinished && stateTimer >= 3 ||
          Main.level2IsFinished && !Main.level1IsFinished && stateTimer >= 3) {
        Yes.setVisible(true);
        No.setVisible(true);
      } else if (Main.level1IsFinished && Main.level2IsFinished ||
                 stateTimer < 3) {
        Yes.setVisible(false);
        No.setVisible(false);
      }
      stage.act(delta);
      stage.draw();
    }
  }

  @Override
  public void resize(int width,
                     int height) { // I explained this in slides (.pptx file)
    Yes.setSize(141.5f / (1794 / Main.getWidth()), 50 / (1794 / Main.getWidth()));
    No.setSize(141.5f / (1794 / Main.getWidth()), 50 / (1794 / Main.getWidth()));
    Yes.setPosition((Main.getWidth() / 2) - (341.5f / (1794 / Main.getWidth())),
                    (Main.getHeight() / 2) - (66.6f / (1794 / Main.getWidth())) -
                        (180 / (1080 / Main.getHeight())) -
                        (100 / (1080 / Main.getHeight())));
    No.setPosition((Main.getWidth() / 2) + (200f / (1794 / Main.getWidth())),
                   (Main.getHeight() / 2) - (66.6f / (1794 / Main.getWidth())) -
                       (180 / (1080 / Main.getHeight())) -
                       (100 / (1080 / Main.getHeight())));
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}

  @Override
  public void dispose() {
    batch.dispose();
    game.dispose();
    label.dispose();
    if (Main.level1IsFinished && Main.level2IsFinished) {
      Thanks.dispose();
    } else if (Main.level1IsFinished && !Main.level2IsFinished ||
               Main.level2IsFinished && !Main.level1IsFinished) {
      atlas.dispose();
      stage.dispose();
      skin.dispose();
    }
  }
}
