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
import com.company.passtosurvive.levels.Level1Part1Screen;
import com.company.passtosurvive.levels.Level1Part2Screen;
import com.company.passtosurvive.levels.Level2Part1Screen;
import com.company.passtosurvive.levels.Level2Part2Screen;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.tools.MusicalAtmosphere;

public class MainMenuScreen
    implements Screen { // the main menu starts at the beginning of the game through the pause menu
                        // you can return here
  private final Main game;
  private final SpriteBatch batch;
  private final Texture background, information;
  private float logoAnimationStateTime;
  private final Stage stage;
  private ImageButton exit, soundIsOn, info;
  private final ImageButton play;
  private ImageButton level1;
  private ImageButton level2;
  private ImageButton soundIsOff;
  private final TextureAtlas animationLogoAtlas, buttonAtlas;
  private Skin buttonSkin;
  private final Animation<TextureRegion> animation;
  private boolean infoIsPressed;

  public MainMenuScreen(final Main game) {
    this.game = game;
    buttonAtlas = new TextureAtlas("Buttons.pack");
    buttonSkin = new Skin(Gdx.files.internal("Buttons.json"), buttonAtlas);
    infoIsPressed = false;
    batch = new SpriteBatch();
    final Array<TextureRegion> frames = new Array<TextureRegion>();
    animationLogoAtlas = new TextureAtlas("Logo.pack");
    information = new Texture("GameInfo.png");
    for (int i = 1; i <= 175; i += 3) {
      frames.add(
          animationLogoAtlas.findRegion(
              "logo"
                  + i)); // I increase i here by 3 because there are too many textures in the atlas
                         // (177)
    }
    frames.add(animationLogoAtlas.findRegion("logo177"));
    animation = new Animation(0.05f, frames);
    frames.clear();
    background = new Texture(Gdx.files.internal("BackgroundMainMenu.png"));
    stage = new Stage();
    play = new ImageButton(buttonSkin, "playButton");
    play.addListener(
        new ClickListener() { // create a listener for it that will read keystrokes
          @Override
          public void clicked(final InputEvent event, final float x, final float y) {
            level1.setVisible(true);
            level2.setVisible(true);
            exit.setVisible(false);
            play.setVisible(false);
            info.setVisible(false);
          }
        });
    exit = new ImageButton(buttonSkin, "exitButton");
    exit.addListener(
        new ClickListener() { // create a listener for it that will read keystrokes
          @Override
          public void clicked(final InputEvent event, final float x, final float y) {
            dispose();
            PlayGameScreen.resetLastScreen();
            Gdx.app.exit();
          }
        });
    soundIsOn = new ImageButton(buttonSkin, "soundButton");
    soundIsOff = new ImageButton(buttonSkin, "soundOffButton");
    soundIsOff.addListener(
        new ClickListener() { // create a listener for it that will read keystrokes
          @Override
          public void clicked(final InputEvent event, final float x, final float y) {
            MusicalAtmosphere.setMusicOn(true);
          }
        });
    soundIsOn.addListener(
        new ClickListener() { // create a listener for it that will read keystrokes
          @Override
          public void clicked(final InputEvent event, final float x, final float y) {
            MusicalAtmosphere.setMusicOn(false);
          }
        });
    info = new ImageButton(buttonSkin, "infoButton");
    info.addListener(
        new ClickListener() { // create a listener for it that will read keystrokes
          @Override
          public void clicked(final InputEvent event, final float x, final float y) {
            infoIsPressed = !infoIsPressed;
            exit.setVisible(!exit.isVisible());
            play.setVisible(!play.isVisible());
          }
        });
    level1 = new ImageButton(buttonSkin, "level1Button");
    level1.addListener(
        new ClickListener() { // create a listener for it that will read keystrokes
          @Override
          public void clicked(final InputEvent event, final float x, final float y) {
          if(!Level1Part1Screen.isFinished() || !Level1Part2Screen.isFinished()){
            dispose();
            if (!Level1Part1Screen.isFinished()) {
              if (PlayGameScreen.getLastScreen() instanceof Level1Part1Screen)
                game.setScreen(PlayGameScreen.getLastScreen());
              else {
              PlayGameScreen.resetLastScreen();
                game.setScreen(new Level1Part1Screen(game));
              }
            } else {
              if (PlayGameScreen.getLastScreen() instanceof Level1Part2Screen)
                game.setScreen(PlayGameScreen.getLastScreen());
              else {
              PlayGameScreen.resetLastScreen();
                game.setScreen(new Level1Part2Screen(game));
              }
            }
          }}
        });
    level2 = new ImageButton(buttonSkin, "level2Button");
    level2.addListener(
        new ClickListener() {
          @Override
          public void clicked(final InputEvent event, final float x, final float y) {
          if(!Level2Part1Screen.isFinished() || !Level2Part2Screen.isFinished()){
            dispose();
            if (!Level2Part1Screen.isFinished()) {
              if (PlayGameScreen.getLastScreen() instanceof Level2Part1Screen)
                game.setScreen(PlayGameScreen.getLastScreen());
              else {
              PlayGameScreen.resetLastScreen();
                game.setScreen(new Level2Part1Screen(game));
              }
            } else {
              if (PlayGameScreen.getLastScreen() instanceof Level2Part2Screen)
                game.setScreen(PlayGameScreen.getLastScreen());
              else {
              PlayGameScreen.resetLastScreen();
                game.setScreen(new Level2Part2Screen(game));
              }
            }
          }}
        });
    level1.setVisible(false);
    level2.setVisible(false);
    soundIsOff.setVisible(false);
    stage.addActor(exit);
    stage.addActor(play);
    stage.addActor(info);
    stage.addActor(soundIsOn);
    stage.addActor(soundIsOff);
    stage.addActor(level1);
    stage.addActor(level2);
    Gdx.input.setInputProcessor(stage); // so that clicks are processed only by stage
  }

  public void
      titleAnimationRender() { // Explained in Github repo
    logoAnimationStateTime += Gdx.graphics.getDeltaTime();
    batch.draw(
        animation.getKeyFrame(logoAnimationStateTime, true),
        Main.getScreenWidth() / 2 - (936 / (1920 / Main.getScreenWidth())) / 2,
        Main.getScreenHeight() / 2 + 250 / (1080 / Main.getScreenHeight()),
        936 / (1920 / Main.getScreenWidth()),
        144 / (1920 / Main.getScreenWidth()));
  }

  @Override
  public void render(final float delta) {
    if (MusicalAtmosphere.isMusicOn()){
      soundIsOff.setVisible(false);
      soundIsOn.setVisible(true);
      Main.getMusic().mainMenuMusicPlay();
    }
    else {
      soundIsOff.setVisible(true);
      soundIsOn.setVisible(false);
      Main.getMusic().allPause();
    }
    ScreenUtils.clear(0, 0, 0, 1, true); // clean up
    batch.begin();
    batch.draw(background, 0, 0, Main.getScreenWidth(), Main.getScreenHeight());
    if (!infoIsPressed) {
      titleAnimationRender();
    } else {
      batch.draw(
          information,
          Main.getScreenWidth() / 2 - ((1500 / (1794 / Main.getScreenWidth())) / 2),
          Main.getScreenHeight() / 2 - ((225 / (1794 / Main.getScreenWidth())) / 2),
          1500 / (1794 / Main.getScreenWidth()),
          225 / (1794 / Main.getScreenWidth()));
    }
    batch.end();
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(
      final int width,
      final int
          height) { // Explained in Github repo
    soundIsOn.setSize(150 / (1794 / Main.getScreenWidth()), 117 / (1794 / Main.getScreenWidth()));
    soundIsOn.setPosition(0, Main.getScreenHeight() / 2 + 100 / (1080 / Main.getScreenHeight()));
    soundIsOff.setSize(150 / (1794 / Main.getScreenWidth()), 117 / (1794 / Main.getScreenWidth()));
    soundIsOff.setPosition(0, Main.getScreenHeight() / 2 + 100 / (1080 / Main.getScreenHeight()));
    info.setSize(150 / (1794 / Main.getScreenWidth()), 117 / (1794 / Main.getScreenWidth()));
    info.setPosition(0, Main.getScreenHeight() / 2 - 50 / (1080 / Main.getScreenHeight()));
    play.setSize((875 / (2880 / Main.getScreenWidth())), (350 / (2880 / Main.getScreenWidth())));
    play.setPosition(
        (Main.getScreenWidth() / 2) - ((play.getWidth()) / 2),
        ((Main.getScreenHeight() / 2) - 200 / (2880 / Main.getScreenWidth())));
    exit.setSize((875 / (2880 / Main.getScreenWidth())), (350 / (2880 / Main.getScreenWidth())));
    exit.setPosition(
        (Main.getScreenWidth() / 2) - ((exit.getWidth()) / 2),
        ((Main.getScreenHeight() / 2) - 600 / (2880 / Main.getScreenWidth())));
    level1.setSize(525 / (1794 / Main.getScreenWidth()), 525 / (1794 / Main.getScreenWidth()));
    level2.setSize(525 / (1794 / Main.getScreenWidth()), 525 / (1794 / Main.getScreenWidth()));
    level1.setPosition(
        Main.getScreenWidth() / 2
            - (525 / (1794 / Main.getScreenWidth())) / 2
            - 350 / (1794 / Main.getScreenWidth()),
        Main.getScreenHeight() / 2
            - (525 / (1794 / Main.getScreenWidth())) / 2
            - 100 / (1080 / Main.getScreenHeight()));
    level2.setPosition(
        Main.getScreenWidth() / 2
            - (525 / (1794 / Main.getScreenWidth())) / 2
            + 350 / (1794 / Main.getScreenWidth()),
        Main.getScreenHeight() / 2
            - (525 / (1794 / Main.getScreenWidth())) / 2
            - 100 / (1080 / Main.getScreenHeight()));
  }

  @Override
  public void dispose() {
    Main.getMusic().allPause();
    buttonSkin.dispose();
    buttonAtlas.dispose();
    background.dispose();
    information.dispose();
    animationLogoAtlas.dispose();
    batch.dispose();
    stage.dispose();
    game.dispose();
  }

  @Override
  public void show() {
  }

  @Override
  public void hide() {
    Main.getMusic().allPause();
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}
}
