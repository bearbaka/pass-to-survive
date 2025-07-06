package com.company.passtosurvive.models;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class NextFloor extends TileObject{
    public NextFloor(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this); // add so that WorldContactListener can recognize
    }
}
