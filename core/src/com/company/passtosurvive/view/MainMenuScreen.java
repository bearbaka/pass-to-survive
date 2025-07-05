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

public class MainMenuScreen implements Screen { // the main menu starts at the beginning of the game
                                                // through the pause menu you can return here
  final Main game;
  private SpriteBatch batch;
  private Texture background, information;
  private float stateTime;
  private Stage stage;
  private ImageButton exit, sound, info, play, level1, level2, soundIsOff;
  private Skin skin;
  private TextureAtlas atlas;
  private Animation<TextureRegion> animation;
  private boolean infoIsPressed;

  public MainMenuScreen(final Main game) {
    this.game = game;
    infoIsPressed = false;
    // Main.playerX = 0;
    // Main.playerY = 0;
    // Main.playerCheckpointX = 0;
    // Main.playerCheckpointY = 0;
    batch = new SpriteBatch();
    Array<TextureRegion> logoFrames = new Array<TextureRegion>();
    atlas = new TextureAtlas("Logo.pack");
    information = new Texture("DevInfo.png");
    for (int i = 1; i <= 175; i += 3) {
      logoFrames.add(atlas.findRegion("Logo" + i)); // I increase i here by 3 because there are
                                                    // too many textures in the atlas (177)
    }
    logoFrames.add(atlas.findRegion("Logo177"));
    animation = new Animation(0.05f, logoFrames);
    logoFrames.clear();
    background = new Texture(Gdx.files.internal("backgroundMainMenu.png"));
    stage = new Stage();
    atlas = new TextureAtlas("AllComponents.pack");
    skin = new Skin(Gdx.files.internal("Buttons.json"), atlas);
    play = new ImageButton(skin, "default");
    play.addListener(new ClickListener() { // create a listener for it that will
                                           // read keystrokes
      @Override
      public void clicked(InputEvent event, float x, float y) {
        level1.setVisible(true);
        level2.setVisible(true);
        exit.setVisible(false);
        play.setVisible(false);
        info.setVisible(false);
        sound.setVisible(false);
        soundIsOff.setVisible(false);
      }
    });
    exit = new ImageButton(skin, "default1");
    exit.addListener(new ClickListener() { // create a listener for it that will
                                           // read keystrokes
      @Override
      public void clicked(InputEvent event, float x, float y) {
        dispose();
        Gdx.app.exit();
      }
    });
    sound = new ImageButton(skin, "default9");
    soundIsOff = new ImageButton(skin, "default13");
    soundIsOff.addListener(new ClickListener() { // create a listener for it
                                                 // that will read keystrokes
      @Override
      public void clicked(InputEvent event, float x, float y) {
        soundIsOff.setVisible(false);
        sound.setVisible(true);
        Main.getMusic().mainMenuMusicPlay();
        Main.musicIsOn = true;
      }
    });
    sound.addListener(new ClickListener() { // create a listener for it that
                                            // will read keystrokes
      @Override
      public void clicked(InputEvent event, float x, float y) {
        soundIsOff.setVisible(true);
        sound.setVisible(false);
        Main.getMusic().allPause();
        Main.musicIsOn = false;
      }
    });
    info = new ImageButton(skin, "default10");
    info.addListener(new ClickListener() { // create a listener for it that will
                                           // read keystrokes
      @Override
      public void clicked(InputEvent event, float x, float y) {
        infoIsPressed = !infoIsPressed;
        exit.setVisible(!exit.isVisible());
        play.setVisible(!play.isVisible());
      }
    });
    level1 = new ImageButton(skin, "default11");
    level1.addListener(new ClickListener() { // create a listener for it that
                                             // will read keystrokes
      @Override
      public void clicked(InputEvent event, float x, float y) {
        dispose();
        game.setScreen(new Level1ScreenPart1(game));
      }
    });
    level2 = new ImageButton(skin, "default12");
    level2.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        dispose();
        game.setScreen(new Level2ScreenFloor1(game));
      }
    });
    level1.setVisible(false);
    level2.setVisible(false);
    soundIsOff.setVisible(false);
    stage.addActor(exit);
    stage.addActor(play);
    stage.addActor(info);
    stage.addActor(sound);
    stage.addActor(soundIsOff);
    stage.addActor(level1);
    stage.addActor(level2);
    Gdx.input.setInputProcessor(stage); // so that clicks are processed only by stage
  }

  public void titleAnimationRender() { // in the presentation I explained why I divide 1920
                                       // / by the screen size of the device on which I run
    stateTime += Gdx.graphics.getDeltaTime();
    batch.draw(animation.getKeyFrame(stateTime, true),
        Main.getScreenWidth() / 2 - (936 / (1920 / Main.getScreenWidth())) / 2,
        Main.getScreenHeight() / 2 + 250 / (1080 / Main.getScreenHeight()), 936 / (1920 / Main.getScreenWidth()),
        144 / (1920 / Main.getScreenWidth()));
  }

  @Override
  public void show() {
    if (Main.musicIsOn)
      Main.getMusic().mainMenuMusicPlay();
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1, true); // clean up
    batch.begin();
    batch.draw(background, 0, 0, Main.getScreenWidth(), Main.getScreenHeight());
    if (!infoIsPressed) {
      titleAnimationRender();
    } else {
      batch.draw(information, Main.getScreenWidth() / 2 - ((1500 / (1794 / Main.getScreenWidth())) / 2),
          Main.getScreenHeight() / 2 - ((225 / (1794 / Main.getScreenWidth())) / 2),
          1500 / (1794 / Main.getScreenWidth()), 225 / (1794 / Main.getScreenWidth()));
    }
    batch.end();
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) { // in the presentation I explained why I optimize the
                                              // sizes of buttons for the screen in my own way
    sound.setSize(150 / (1794 / Main.getScreenWidth()), 117 / (1794 / Main.getScreenWidth()));
    sound.setPosition(0, Main.getScreenHeight() / 2 + 100 / (1080 / Main.getScreenHeight()));
    soundIsOff.setSize(150 / (1794 / Main.getScreenWidth()), 117 / (1794 / Main.getScreenWidth()));
    soundIsOff.setPosition(0, Main.getScreenHeight() / 2 + 100 / (1080 / Main.getScreenHeight()));
    info.setSize(150 / (1794 / Main.getScreenWidth()), 117 / (1794 / Main.getScreenWidth()));
    info.setPosition(0, Main.getScreenHeight() / 2 - 50 / (1080 / Main.getScreenHeight()));
    play.setSize((875 / (2880 / Main.getScreenWidth())), (350 / (2880 / Main.getScreenWidth())));
    play.setPosition((Main.getScreenWidth() / 2) - ((play.getWidth()) / 2),
        ((Main.getScreenHeight() / 2) - 200 / (2880 / Main.getScreenWidth())));
    exit.setSize((875 / (2880 / Main.getScreenWidth())), (350 / (2880 / Main.getScreenWidth())));
    exit.setPosition((Main.getScreenWidth() / 2) - ((exit.getWidth()) / 2),
        ((Main.getScreenHeight() / 2) - 600 / (2880 / Main.getScreenWidth())));
    level1.setSize(525 / (1794 / Main.getScreenWidth()), 525 / (1794 / Main.getScreenWidth()));
    level2.setSize(525 / (1794 / Main.getScreenWidth()), 525 / (1794 / Main.getScreenWidth()));
    level1.setPosition(
        Main.getScreenWidth() / 2 - (525 / (1794 / Main.getScreenWidth())) / 2 - 350 / (1794 / Main.getScreenWidth()),
        Main.getScreenHeight() / 2 - (525 / (1794 / Main.getScreenWidth())) / 2
            - 100 / (1080 / Main.getScreenHeight()));
    level2.setPosition(
        Main.getScreenWidth() / 2 - (525 / (1794 / Main.getScreenWidth())) / 2 + 350 / (1794 / Main.getScreenWidth()),
        Main.getScreenHeight() / 2 - (525 / (1794 / Main.getScreenWidth())) / 2
            - 100 / (1080 / Main.getScreenHeight()));
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {
    Main.getMusic().allPause();
  }

  @Override
  public void dispose() {
    Main.getMusic().allPause();
    stage.dispose();
    skin.dispose();
    atlas.dispose();
    game.dispose();
    batch.dispose();
    background.dispose();
    information.dispose();
  }
}
