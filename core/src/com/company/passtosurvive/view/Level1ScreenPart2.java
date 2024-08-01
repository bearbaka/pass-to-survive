package com.company.passtosurvive.view;

import static com.company.passtosurvive.view.Main.Human2Y;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.company.passtosurvive.control.PlayButtons;
import com.company.passtosurvive.models.Human;
import com.company.passtosurvive.tools.MusicalAtmosphere;
import com.company.passtosurvive.tools.WorldContactListener;
import com.company.passtosurvive.tools.b2WorldCreator;

import java.util.NavigableMap;

public class Level1ScreenPart2 extends PlayGameScreen{ // level 1 part 2 is triggered when the player collides with a certain object in part 1
    final Main game;
    private MusicalAtmosphere music;
    private PlayButtons buttons;
    private SpriteBatch batch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private TmxMapLoader mapLoader;
    private OrthographicCamera cam;
    private Viewport mapPort;
    public boolean animationIsFinished=false; // needed for animation at the beginning after moving to this part of the map
    public Level1ScreenPart2(final Main game) {
        this.game=game;
        batch=new SpriteBatch();
        music=new MusicalAtmosphere();
        Main.screen=2;
        buttons=new PlayButtons();
        cam=new OrthographicCamera();
        if(Main.width==1794 && Main.height==1080){ // I explained this in slides (.pptx file)
            Main.worldHeight=543f;
            Main.worldWidth=864;
        }
        else {
            Main.worldHeight=543f;
            Main.worldWidth=864f/((16/9f)/(Main.width/Main.height)/1.1f);
        }
        mapPort=new FitViewport(Main.worldWidth/Main.PPM, Main.worldHeight/Main.PPM, cam);
        mapLoader=new TmxMapLoader();
        map=mapLoader.load("map2.tmx");
        renderer=new OrthogonalTiledMapRenderer(map, 1/Main.PPM); // I divide almost all values associated with the map by PPM so that there are no problems with physics
        cam.position.set(mapPort.getWorldWidth()/2, mapPort.getWorldHeight()/2, 0);
        Main.v.set(0,-11); // I didn't create new vector2 because This will result in unnecessary memory allocation
        world=new World(Main.v, true);
        if(Main.HumanX!=0 && Main.HumanY!=0){
            human = new Human(world,Main.HumanX + 0.225f, Main.HumanY + 0.3f); // increase due to the fact that the player does not spawn exactly in the center
        }
        else if (Main.HumanX == 0 && Main.HumanY == 0 && Main.HumanYCheckpoint == 0 && Main.HumanXCheckpoint == 0) {
            human=new Human(world,0/Main.PPM, Human2Y+0.3f); // save the height of the previous person to make it more realistic
        }
        else if(Main.HumanX==0 && Main.HumanY==0){
            human = new Human(world, Main.HumanXCheckpoint, Main.HumanYCheckpoint+0.3f); // increase Y by 0.3f so that the player spawns slightly higher than the checkpoint itself
        }
        b2dr=new Box2DDebugRenderer();
        new b2WorldCreator(world, map, this);
        world.setContactListener(new WorldContactListener(this));
        buttons.pause.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.HumanX=human.getX();
                Main.HumanY=human.getY();
                music.Level1SoundStop();
                dispose();
                game.setScreen(new PauseScreen(game));
            }
        });
        if(Main.soundIsOn) {
            music.Level1SoundPLay();
        }
        else {
            music.Level1SoundStop();
        }
    }
    @Override
    public void show() { }
    @Override
    public void handle() {
        if(buttons.jump.isPressed() && human.HumanBody.getLinearVelocity().y==0 && !human.Head()) {
            Main.v.set(0, 5f);
            human.HumanBody.applyLinearImpulse(Main.v, human.HumanBody.getWorldCenter(), true);
            music.JumpSoundPLay();
        }
        if(buttons.joyStick.isJoyStickDown()){
            if(buttons.joyStick.getValueX()>0 && human.HumanBody.getLinearVelocity().x<=2.5f){ // 2.5f is the max. speed 0.3f as acceleration
                Main.v.set(0.3f, 0);
                human.HumanBody.applyLinearImpulse(Main.v, human.HumanBody.getWorldCenter(), true);
            }
            else if(buttons.joyStick.getValueX()<0 && human.HumanBody.getLinearVelocity().x>=-2.5f){
                Main.v.set(-0.3f, 0);
                human.HumanBody.applyLinearImpulse(Main.v, human.HumanBody.getWorldCenter(), true);
            }
        }
        if(!buttons.joyStick.isJoyStickDown() && human.HumanBody.getLinearVelocity().x!=0){ // the player needs to stop immediately after releasing the joystick
            Main.v.set(-human.HumanBody.getLinearVelocity().x, 0);
            human.HumanBody.applyLinearImpulse(Main.v, human.HumanBody.getWorldCenter(), true);
        }
        if(human.getX()<280/Main.PPM && !animationIsFinished){ // This script will not work after death because There is a checkpoint at position 280 so as not to repeat
            buttons.joyStick.setVisible(false);
            buttons.jump.setVisible(false);
            if(human.HumanBody.getLinearVelocity().y==0){
                Main.v.set(0, 2f);
                human.HumanBody.applyLinearImpulse(Main.v, human.HumanBody.getWorldCenter(), true);
                music.JumpSoundPLay();
            }
            Main.v.set(2f, 0);
            human.HumanBody.applyLinearImpulse(Main.v, human.HumanBody.getWorldCenter(), true);
        }
        else if(human.getX()>=280/Main.PPM){
            buttons.joyStick.setVisible(true);
            buttons.jump.setVisible(true);
            animationIsFinished=true;
        }
    }

    public void update(float dt){
        handle();
        world.step(1/60f, 6,2);
        cam.position.x=human.HumanBody.getPosition().x; // camera moves with player
        human.update(dt);
        cam.update();
        renderer.setView(cam);
    }
    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1); // cleanup
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // cleanup
        renderer.render();
        //b2dr.render(world, cam.combined); // you can turn this on if you want to see a green outline around objects of world
        batch.setProjectionMatrix(cam.combined);
        collision();
        batch.begin();
        human.draw(batch);
        batch.end();
        buttons.stage.act(delta);
        buttons.stage.draw();
    }

    @Override
    public void collision() {
        if(human.Dead() && Main.hit==1 || human.Dead() && Main.hit==2){ // 1 means lava, 2 means spikes
            Main.deaths++;
            music.Level1SoundStop();
            dispose();
            game.setScreen(new DeadScreen(game));
        }
        if(human.Dead() && Main.hit==3){ // 3 means finish
            Main.level1IsFinished=true;
            music.Level1SoundStop();
            dispose();
            game.setScreen(new WinScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        mapPort.update(width, height);
        buttons.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
    @Override
    public void dispose() {
        music.dispose();
        map.dispose();
        batch.dispose();
        renderer.dispose();
        world.dispose();
        game.dispose();
        b2dr.dispose();
        buttons.dispose();
    }
}
