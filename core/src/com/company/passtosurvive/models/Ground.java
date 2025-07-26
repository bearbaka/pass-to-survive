package com.company.passtosurvive.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Ground extends TileObject {
  public Ground(World world, Rectangle rect) {
    super(world, rect, 0f);
  }
}
