package com.company.passtosurvive.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.view.Main;

import lombok.AccessLevel;
import lombok.Getter;

public abstract
class TileObject { // all objects for world and its listener except Player are inherited from this
                   // class
  @Getter(AccessLevel.PACKAGE)
  private Body body;

  public TileObject(World world, Rectangle rect, float friction) { // everything is taken from b2WorldCreator
    BodyDef bDef = new BodyDef();
    FixtureDef fDef = new FixtureDef();
    PolygonShape shape = new PolygonShape();
    bDef.type = BodyDef.BodyType.StaticBody;
    bDef.position.set(
        (rect.getX() + rect.getWidth() / 2) / Main.PPM,
        (rect.getY() + rect.getHeight() / 2) / Main.PPM);
    body = world.createBody(bDef); // add into world
    shape.setAsBox((rect.getWidth() / 2) / Main.PPM, (rect.getHeight() / 2) / Main.PPM);
    fDef.shape = shape;
    fDef.friction = friction;
    body.createFixture(fDef).setUserData(this); // add the shape
    shape.dispose();
  }

  public void inContactAct(
      PlayGameScreen playGameScreen) { // to determine which object is in the WorldContactListener
    Player player = playGameScreen.getPlayer();
    player.setTouchedBouncer(this instanceof Bouncer);
    player.setEndingBouncer(this instanceof EndingBouncer);
  }
}
