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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.company.passtosurvive.levels.Level1Part1Screen;
import com.company.passtosurvive.levels.Level1Part2Screen;
import com.company.passtosurvive.levels.Level2Part1Screen;
import com.company.passtosurvive.levels.Level2Part2Screen;

public class WinScreen
    implements Screen { // the victory screen is launched after completing the level completely
  private final Main game;
  private Texture label, thanks;
  private final SpriteBatch batch;
  private TextureAtlas animationAtlas;
  private Stage stage;
  private ImageButton Yes, No;
  private float animationStateTime, timer;
  private Animation<TextureRegion> animation;
  private boolean played;

  public WinScreen(final Main game) {
    this.game = game;
    batch = new SpriteBatch();
    if (Level1Part2Screen.isFinished()
        && Level2Part2Screen.isFinished()) { // when player have completely completed the game
      label = new Texture("GameOverThanks.png");
      thanks = new Texture("ThanksForPlaying.png");
    } else if (Level1Part2Screen.isFinished()
        ^ Level2Part2Screen.isFinished()) { // when one of the levels has passed
      stage = new Stage();
      label = new Texture("ContinueWhite.png");
      animationAtlas = new TextureAtlas("YouWin.pack");
      final Array<TextureRegion> frames = new Array<TextureRegion>();
      for (int i = 1; i <= 15; i++) frames.add(animationAtlas.findRegion("youWin" + i));
      animation = new Animation(0.2f, frames);
      frames.clear();
      Yes = new ImageButton(Main.getButtonSkin(), "yesWhiteButton");
      Yes.addListener(
          new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
              dispose();
              if (Level1Part2Screen.isFinished()) {
                game.setScreen(new Level2Part1Screen(game));
              } else if (Level2Part2Screen.isFinished()) {
                game.setScreen(new Level1Part1Screen(game));
              }
            }
          });
      No = new ImageButton(Main.getButtonSkin(), "noWhiteButton");
      No.addListener(
          new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
              dispose();
              game.setScreen(new MainMenuScreen(game));
            }
          });
      stage.addActor(Yes);
      stage.addActor(No);
      Gdx.input.setInputProcessor(stage); // so that clicks are processed only by stage
    }
    if (Level1Part2Screen.isFinished() ^ Level2Part2Screen.isFinished()) {
      Main.getMusic().winSoundPlay();
    }
  }

  public void winRender() {
    animationStateTime += Gdx.graphics.getDeltaTime();
    batch.draw(
        animation.getKeyFrame(animationStateTime, true),
        0,
        Main.getScreenHeight() / 2,
        Main.getScreenWidth(),
        Main.getScreenHeight() / 3f);
  }

  @Override
  public void render(final float delta) {
    ScreenUtils.clear(0, 0, 0, 1, true); // clean up
    batch.begin();
    timer += delta;
    if (Level1Part2Screen.isFinished() ^ Level2Part2Screen.isFinished()) {
      winRender();
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
    } else if (Level1Part2Screen.isFinished() && Level2Part2Screen.isFinished()) {
      batch.draw(
          label,
          0,
          Main.getScreenHeight() / 2,
          Main.getScreenWidth(),
          203 / (1794 / Main.getScreenWidth()));
      if (timer >= 7) {
        if (!played) {
          Main.getMusic().endSoundPlay();
          played=true;
        }
        batch.draw(
            thanks,
            Main.getScreenWidth() / 2 - ((1266.7f / (1794 / Main.getScreenWidth())) / 2),
            Main.getScreenHeight() / 2
                - 80 / (1080 / Main.getScreenHeight())
                - 66.6f / (1794 / Main.getScreenWidth())
                - 50 / (1080 / Main.getScreenHeight()),
            1266.7f / (1794 / Main.getScreenWidth()),
            66.6f / (1794 / Main.getScreenWidth()));
      }
    }
    batch.end();
    if (Level1Part2Screen.isFinished() ^ Level2Part2Screen.isFinished()) {
      if (timer >= 3) {
        Yes.setVisible(true);
        No.setVisible(true);
      } else {
        Yes.setVisible(false);
        No.setVisible(false);
      }
      stage.act(delta);
      stage.draw();
    }
  }

  @Override
  public void resize(final int width, final int height) { // I explained this in slides (.pptx file)
    if (Level1Part2Screen.isFinished() ^ Level2Part2Screen.isFinished()) {
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
    label.dispose();
    if (thanks != null) thanks.dispose();
    if (animationAtlas != null) animationAtlas.dispose();
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
