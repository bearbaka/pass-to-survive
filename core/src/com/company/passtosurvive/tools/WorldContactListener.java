package com.company.passtosurvive.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.company.passtosurvive.models.TileObject;
import com.company.passtosurvive.levels.PlayGameScreen;
import java.util.regex.Pattern;

public class WorldContactListener implements ContactListener { // Serves as a Collision Listener for
                                                               // two
                                                               // objects in the game
  private PlayGameScreen playGameScreen; // I created Player in PlayGameScreen so that there would
                                         // be one listener for all level screens

  public WorldContactListener(PlayGameScreen playGameScreen) {
    this.playGameScreen = playGameScreen;
  }

  @Override
  public void beginContact(Contact contact) { // the start of collision
    Fixture fixA = contact.getFixtureA(); // first object
    Fixture fixB = contact.getFixtureB(); // second object
    if (fixA.getUserData()!=null && Pattern.matches("Player.*", fixA.getUserData().toString())
        || fixB.getUserData()!=null && Pattern.matches("Player.*", fixB.getUserData().toString())) { // check if one of the
      // objects is player
      Fixture player = fixA.getUserData()!=null && Pattern.matches("Player.*", fixA.getUserData().toString()) ? fixA : fixB; // which
                                                                                                 // one
                                                                                                 // is
                                                                                                 // player
      Fixture object = player == fixA ? fixB : fixA; // which one is not player
      if (object.getUserData() != null
          && TileObject.class.isAssignableFrom(object.getUserData().getClass())) { // is not the
                                                                                   // object
                                                                                   // equal to
                                                                                   // nothing and
                                                                                   // does it
                                                                                   // inherit from
                                                                                   // TileObject
        ((TileObject) object.getUserData()).inContactAct(playGameScreen);
        if (player.getUserData() == "PlayerHead") {
          playGameScreen.getPlayer().setHeadInContact(true);
        }
      }
    }
  }

  @Override
  public void endContact(Contact contact) { // end of collision
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();
    if (fixA.getUserData()!=null && Pattern.matches("Player.*", fixA.getUserData().toString())
        || fixB.getUserData()!=null && Pattern.matches("Player.*", fixB.getUserData().toString())) {
      Fixture player = fixA.getUserData()!=null && Pattern.matches("Player.*", fixA.getUserData().toString()) ? fixA : fixB;
      Fixture object = player == fixA ? fixB : fixA;
      if (object.getUserData() != null
          && TileObject.class.isAssignableFrom(object.getUserData().getClass())) {
        if (player.getUserData() == "PlayerHead" && playGameScreen.getPlayer().isHeadInContact()) { // if
                                                                                                    // the
                                                                                                    // player's
                                                                                                    // head
                                                                                                    // was
                                                                                                    // in
                                                                                                    // contact
                                                                                                    // then
                                                                                                    // now
                                                                                                    // it's
                                                                                                    // not
          playGameScreen.getPlayer().setHeadInContact(false);
        }
      }
    }
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {}

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {}
}
