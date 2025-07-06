package com.company.passtosurvive.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.company.passtosurvive.models.Bouncer;
import com.company.passtosurvive.models.CheckPoint;
import com.company.passtosurvive.models.Finish;
import com.company.passtosurvive.models.NextPart;
import com.company.passtosurvive.models.Ground;
import com.company.passtosurvive.models.Lava;
import com.company.passtosurvive.models.NextFloor;
import com.company.passtosurvive.models.Spike;
import com.company.passtosurvive.levels.Level1Part1Screen;
import com.company.passtosurvive.levels.Level1Part2Screen;
import com.company.passtosurvive.levels.Level2Part1Screen;
import com.company.passtosurvive.levels.Level2Part2Screen;

public class b2WorldCreator { // creates objects from maps for the world and its listener, in the level screens there is a Box2DDebugRenderer it draws these objects
    public b2WorldCreator(World world, TiledMap map, Level1Part1Screen screen){
        // creating grass
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Ground(world, map, rect);
        }
        // creating lava
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Lava(world, map, rect);
        }
        // creating spikes
        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Spike(world, map, rect);
        }
        // creating the other spikes
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Spike(world, map, rect);
        }
        // We create an object that serves as a trigger for going to the next part of the level
        for(MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new NextPart(world, map, rect);
        }
        // creating checkpoints
        for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new CheckPoint(world, map, rect);
        }
    }
    public b2WorldCreator(World world, TiledMap map, Level1Part2Screen screen){
        // creating grass
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Ground(world, map, rect);
        }
        // creating spikes
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Spike(world, map, rect);
        }
        // creating chest
        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Finish(world, map, rect);
        }
        // creating checkpoints
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new CheckPoint(world, map, rect);
        }
        // creating lava
        for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Lava(world, map, rect);
        }
    }
    public b2WorldCreator(World world, TiledMap map, Level2Part1Screen screen){
        // creating bouncers
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Bouncer(world, map, rect);
        }
        // creating grass
        for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Ground(world, map, rect);
        }
        // creating lava
        for(MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Lava(world, map, rect);
        }
        // create an object that serves to run the script (in Level2ScreenFloor1)
        for(MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new NextFloor(world, map, rect);
        }
        // creating checkpoints
        for(MapObject object: map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new CheckPoint(world, map, rect);
        }
        // We create an object that serves as a trigger for going to the next part of the level
        for(MapObject object: map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new NextPart(world, map, rect);
        }
    }
    public b2WorldCreator(World world, TiledMap map, Level2Part2Screen screen){
        // creating bouncers
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Bouncer(world, map, rect);
        }
        // creating grass
        for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Ground(world, map, rect);
        }
        // creating lava
        for(MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Lava(world, map, rect);
        }
        // creating finish
        for(MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new Finish(world, map, rect);
        }
        // creating checkpoints
        for(MapObject object: map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject) object).getRectangle();
            new CheckPoint(world, map, rect);
        }
    }
}
