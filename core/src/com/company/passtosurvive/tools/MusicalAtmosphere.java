package com.company.passtosurvive.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicalAtmosphere
    extends AssetManager { // used for music and sounds
  private static boolean musicOn; // to check if music is enabled in settings
  public static boolean isMusicOn() {
    return musicOn;
  }
  public static void toggleMusicOn() {
    musicOn = !musicOn;
  }

  static {
    musicOn=true;
  }

  public MusicalAtmosphere() { // loading everything
    load("Level1Sound.ogg", Music.class);
    load("Level2Sound.ogg", Music.class);
    load("MainMenuSound.ogg", Music.class);
    load("GameOverSound.wav", Sound.class);
    load("PlayerJump.wav", Sound.class);
    load("WinSound.wav", Sound.class);
    load("GhoulSound.wav", Sound.class);
    load("End.wav", Sound.class);
    finishLoading();
  }

  public void mainMenuMusicPlay() {
    get("MainMenuSound.ogg", Music.class).setVolume(0.4f);
    get("MainMenuSound.ogg", Music.class).setLooping(true);
    get("MainMenuSound.ogg", Music.class).play();
  }

  public void level1MusicPlay() {
    get("Level1Sound.ogg", Music.class).setVolume(0.4f);
    get("Level1Sound.ogg", Music.class).setLooping(true);
    get("Level1Sound.ogg", Music.class).play();
  }

  public void level2MusicPlay() {
    get("Level2Sound.ogg", Music.class).setVolume(0.4f);
    get("Level2Sound.ogg", Music.class).setLooping(true);
    get("Level2Sound.ogg", Music.class).play();
  }

  public void jumpSoundPlay() { get("PlayerJump.wav", Sound.class).play(); }

  public void gameOverSoundPlay() {
    get("GameOverSound.wav", Sound.class).play();
  }

  public void winSoundPlay() { get("WinSound.wav", Sound.class).play(); }

  public void ghoulSoundPlay() { get("GhoulSound.wav", Sound.class).play(); }

  public void mainMenuMusicPause() {
    get("MainMenuSound.ogg", Music.class).pause();
  }

  public void level1MusicPause() { get("Level1Sound.ogg", Music.class).pause(); }

  public void level2MusicPause() { get("Level2Sound.ogg", Music.class).pause(); }

  public void endSoundPlay() { get("End.wav", Sound.class).play(); }

  public boolean isAnyMusicPlaying(){
    return get("MainMenuSound.ogg", Music.class).isPlaying() || get("Level2Sound.ogg", Music.class).isPlaying() || get("Level1Sound.ogg", Music.class).isPlaying();
  }

  public void allPause() {
    mainMenuMusicPause();
    level1MusicPause();
    level2MusicPause();
  }

  @Override
  public synchronized void dispose() {
    unload("Level1Sound.ogg");
    unload("Level2Sound.ogg");
    unload("MainMenuSound.ogg");
    unload("GameOverSound.wav");
    unload("PlayerJump.wav");
    unload("WinSound.wav");
    unload("GhoulSound.wav");
    unload("End.wav");
    super.dispose();
  }
}
