package com.company.passtosurvive.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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

public class Level1ScreenPart1 extends PlayGameScreen { // level 1 part 1 starts in the main menu
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
    public Level1ScreenPart1(final Main game) {
        this.game=game;
        batch=new SpriteBatch();
        music=new MusicalAtmosphere();
        Main.screen=1;
        buttons=new PlayButtons();
        cam=new OrthographicCamera();
        if(Main.width==1794 && Main.height==1080){ // I explained this in slides (.pptx file)
            Main.worldHeight=543f;
            Main.worldWidth=864;
        }
        else {
            Main.worldHeight=543f;
            Main.worldWidth=864f/((16/9f)/(Main.width/Main.height)/1.1f); // ratio of the FHD screen to the aspect ratio of the screen of the device on which I run
        }
        mapPort=new FitViewport(Main.worldWidth/Main.PPM, Main.worldHeight/Main.PPM, cam);
        mapLoader=new TmxMapLoader();
        map=mapLoader.load("map1.tmx");
        renderer=new OrthogonalTiledMapRenderer(map, 1/Main.PPM); // I divide almost all values associated with the map by PPM so that there are no problems with physics
        cam.position.set(mapPort.getWorldWidth()/2, mapPort.getWorldHeight()/2, 0);
        Main.v.set(0,-11); // I didn't create new vector2 because This will result in unnecessary memory allocation
        world=new World(Main.v, true);
        if(Main.HumanX!=0 && Main.HumanY!=0){
            human = new Human(world,Main.HumanX + 0.225f, Main.HumanY + 0.3f); // increase due to the fact that the player does not spawn exactly in the center
        }
        else if (Main.HumanX == 0 && Main.HumanY == 0 && Main.HumanYCheckpoint == 0 && Main.HumanXCheckpoint == 0) {
            human=new Human(world,96/Main.PPM, 96/Main.PPM);
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
            Main.v.set(0f, 5f);
            human.HumanBody.applyLinearImpulse(Main.v, human.HumanBody.getWorldCenter(), true);
            music.JumpSoundPLay();
        }
        if(buttons.joyStick.isJoyStickDown()){
            if(buttons.joyStick.getValueX()>0 && human.HumanBody.getLinearVelocity().x<=2.5f){ // 2.5f is the max. speed 0.3f as acceleration
                Main.v.set(0.3f, 0f);
                human.HumanBody.applyLinearImpulse(Main.v, human.HumanBody.getWorldCenter(), true);
            }
            else if(buttons.joyStick.getValueX()<0 && human.HumanBody.getLinearVelocity().x>=-2.5f){
                Main.v.set(-0.3f, 0f);
                human.HumanBody.applyLinearImpulse(Main.v, human.HumanBody.getWorldCenter(), true);
            }
        }
        if(!buttons.joyStick.isJoyStickDown() && human.HumanBody.getLinearVelocity().x!=0){ // we need the player to stop immediately after releasing the joystick
            Main.v.set(-human.HumanBody.getLinearVelocity().x, 0);
            human.HumanBody.applyLinearImpulse(Main.v, human.HumanBody.getWorldCenter(), true);
        }
    }
    public void update(float dt){
        handle();
        world.step(1/60f, 6,2);
        cam.position.x=human.HumanBody.getPosition().x; // camera moves behind the player
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
        else if(human.Dead() && Main.hit==7){ // 7 means an object that serves as a trigger for moving to the next part
            Main.Human2Y=human.getY();
            Main.HumanX=0;
            Main.HumanY=0;
            Main.HumanXCheckpoint=0;
            Main.HumanYCheckpoint=0;
            music.Level1SoundStop();
            dispose();
            game.setScreen(new Level1ScreenPart2(game));
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
        b2dr.dispose();
        buttons.dispose();
        game.dispose();
    }
}
