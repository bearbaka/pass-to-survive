package com.company.passtosurvive.models;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.levels.PlayGameScreen;
import com.company.passtosurvive.view.Main;

public abstract class TileObject { // all objects for world and its listener
                                   // except Player are inherited from this
                                   // class
  World world;
  TiledMap map;
  TiledMapTile tile;
  Rectangle bounds;
  Body body;
  Fixture fixture;

  public TileObject(World world, TiledMap map,
                    Rectangle bounds) { // everything is taken from
                                        // b2WorldCreator
    this.world = world;
    this.map = map;
    this.bounds = bounds;
    BodyDef bDef = new BodyDef();
    FixtureDef fDef = new FixtureDef();
    PolygonShape shape = new PolygonShape();
    bDef.type = BodyDef.BodyType.StaticBody;
    bDef.position.set((bounds.getX() + bounds.getWidth() / 2) / Main.PPM,
                      (bounds.getY() + bounds.getHeight() / 2) / Main.PPM);
    body = world.createBody(bDef); // add into world
    shape.setAsBox((bounds.getWidth() / 2) / Main.PPM,
                   (bounds.getHeight() / 2) / Main.PPM);
    fDef.shape = shape;
    fixture = body.createFixture(fDef); // add the shape
    shape.dispose();
  }

  public void inContactAct(PlayGameScreen playGameScreen){// to determine which object is in the WorldContactListener
    Player.setTouchedBouncer(this instanceof Bouncer);
    Player.setNextFloor(this instanceof NextFloor);
  } 

  public float getX() { return body.getPosition().x; } // for checkpoint

  public float getY() { return body.getPosition().y; } // for checkpoint
}
