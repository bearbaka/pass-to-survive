package com.company.passtosurvive.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Bouncer extends TileObject {
  public Bouncer(World world, Rectangle rect) {
    super(world, rect, 100000f);
  }
}
