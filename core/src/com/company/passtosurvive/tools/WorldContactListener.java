package com.company.passtosurvive.tools;

import java.util.regex.Pattern;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.models.TileObject;

public class WorldContactListener
    implements ContactListener { // Serves as a Collision Listener for two objects in the game
  private final PlayGameScreen
      playGameScreen; // I created Player in PlayGameScreen so that there would be one listener for
                      // all level screens

  public WorldContactListener(final PlayGameScreen playGameScreen) {
    this.playGameScreen = playGameScreen;
  }

  @Override
  public void beginContact(final Contact contact) { // the start of collision
    final Fixture fixA = contact.getFixtureA(); // first object
    final Fixture fixB = contact.getFixtureB(); // second object
    if (Pattern.matches("Player.*", fixA.getUserData().toString())
        || Pattern.matches(
            "Player.*", fixB.getUserData().toString())) { // check if one of the objects is player
      final Fixture player =
          Pattern.matches("Player.*", fixA.getUserData().toString())
              ? fixA
              : fixB; // which one is player
      final Fixture object = player == fixA ? fixB : fixA; // which one is not player
      if (TileObject.class.isAssignableFrom(
          object
              .getUserData()
              .getClass())) { // is not the object equal to nothing and does it inherit from
                              // TileObject
        ((TileObject) object.getUserData()).inContactAct(playGameScreen);
        if (player.getUserData() == "PlayerHead") {
          playGameScreen.getPlayer().setHeadInContact(true);
        }
      }
    }
  }

  @Override
  public void endContact(final Contact contact) { // end of collision
    final Fixture fixA = contact.getFixtureA();
    final Fixture fixB = contact.getFixtureB();
    if (Pattern.matches("Player.*", fixA.getUserData().toString())
        || Pattern.matches("Player.*", fixB.getUserData().toString())) {
      final Fixture player = Pattern.matches("Player.*", fixA.getUserData().toString()) ? fixA : fixB;
      final Fixture object = player == fixA ? fixB : fixA;
      if (TileObject.class.isAssignableFrom(object.getUserData().getClass())) {
        if (player.getUserData() == "PlayerHead"
            && playGameScreen
                .getPlayer()
                .isHeadInContact()) { // if the player's head was in contact then now it's not
          playGameScreen.getPlayer().setHeadInContact(false);
        }
      }
    }
  }

  @Override
  public void preSolve(final Contact contact, final Manifold oldManifold) {}

  @Override
  public void postSolve(final Contact contact, final ContactImpulse impulse) {}
}
