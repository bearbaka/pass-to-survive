package com.company.passtosurvive.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.company.passtosurvive.view.Main;

public class Player extends Sprite { // the player model is in every level screen
  public enum State { // statuses are needed to change the texture of a person
    JUMPING,
    RUNNING,
    STANDING,
    SLIDING
  };

  private static int deaths;
  private static BodyDef bDef;
  private State
      currentState; // two statuses are needed to avoid problems with animation and to determine the
  // status in different situations
  private static Body playerBody;
  private static TextureAtlas atlas;
  private static TextureRegion playerJump;
  private static Animation<TextureRegion> playerRun, playerStand;
  private float stateTime;
  private boolean toRight, headInContact, dead;
  private State previousState;

  static {
    atlas = new TextureAtlas("Player.pack");
    Array<TextureRegion> frames = new Array<TextureRegion>();
    frames.add(atlas.findRegion("playerrun1"));
    frames.add(atlas.findRegion("playerrun2"));
    playerRun = new Animation(0.2f, frames);
    frames.clear();
    frames.add(atlas.findRegion("playerstay"));
    frames.add(atlas.findRegion("playerstay1"));
    frames.add(atlas.findRegion("playerstay2"));
    playerStand = new Animation(0.25f, frames);
    frames.clear();
    playerJump =
        new TextureRegion(
            atlas.findRegion("playerfall"),
            0,
            0,
            78,
            93); // for jumping and falling I have one frame so this is not animation
    bDef = new BodyDef();
    bDef.type = BodyDef.BodyType.DynamicBody;
  }

  public static void changeSkin() {
    if (deaths == 20) {
      Array<TextureRegion> frames = new Array<TextureRegion>();
      frames.add(atlas.findRegion("playerrun1"));
      frames.add(atlas.findRegion("playerrun2"));
      playerRun = new Animation(0.2f, frames);
      frames.clear();
      frames.add(atlas.findRegion("playerstay"));
      frames.add(atlas.findRegion("playerstay1"));
      frames.add(atlas.findRegion("playerstay2"));
      playerStand = new Animation(0.25f, frames);
      frames.clear();
      playerJump.setRegion(atlas.findRegion("ghoulfall"));
    }
  }

  public void reset(float x, float y){
    currentState = State.STANDING;
    previousState = State.STANDING;
    stateTime = 0;
    headInContact = false;
    dead = false;
    toRight = true; // by default the player model will always look to the right
    changeSkin();
    setPosition(x, y);
    bDef.position.set(getX(), getY());

  }

  public Player(World world, float x, float y) {
    reset(x, y);
    setBounds(0, 0, 63 / (1.4f * Main.PPM), 72 / (1.4f * Main.PPM)); // sprite's bounds
    playerBody = world.createBody(bDef);
    FixtureDef fDef = new FixtureDef();
    EdgeShape vertice =
        new EdgeShape(); // we need edgeshape to make it look like walls around the PlayerBody
    vertice.set(
        new Vector2(-9f / Main.PPM, -21.5f / Main.PPM),
        new Vector2(9f / Main.PPM, -21.5f / Main.PPM)); // bottom wall
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("Player");
    vertice.set(
        new Vector2(-9f / Main.PPM, -21.5f / Main.PPM),
        new Vector2(-11.5f / Main.PPM, -19f / Main.PPM)); // part connecting bottom & side wall
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("Player");
    vertice.set(
        new Vector2(9f / Main.PPM, -21.5f / Main.PPM),
        new Vector2(11.5f / Main.PPM, -19f / Main.PPM)); // part connecting bottom & side wall
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("Player");
    vertice.set(
        new Vector2(11.5f / Main.PPM, -19f / Main.PPM),
        new Vector2(11.5f / Main.PPM, 21.5f / Main.PPM)); // side wall
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("Player");
    vertice.set(
        new Vector2(-11.5f / Main.PPM, -19f / Main.PPM),
        new Vector2(-11.5f / Main.PPM, 21.5f / Main.PPM)); // side wall
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("Player");
    vertice.set(
        new Vector2(-9f / Main.PPM, 21.5f / Main.PPM),
        new Vector2(9f / Main.PPM, 21.5f / Main.PPM)); // the top wall sensor for head, it will
    // not serve as a trigger for Game Over
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("PlayerHead");
    vertice.dispose();
  }

  public void update(float delta, float joyStickValueX) { // needed for the sprite to move
    // with the body of the
    // person in the world so that it is attracted to him
    setPosition(getPosX() - getWidth() / 2, getPosY() - getHeight() / 2.3f);
    setRegion(getFrame(delta, joyStickValueX)); // texture update
  }

  public TextureRegion getFrame(float dt, float joyStickValueX) {
    currentState = getState(); // get the status so that we can build on it later
    TextureRegion region;
    switch (currentState) {
      case JUMPING:
      case SLIDING:
        region = playerJump;
        break;
      case RUNNING:
        region = (TextureRegion) playerRun.getKeyFrame(stateTime, true);
        break;
      case STANDING:
      default:
        region = (TextureRegion) playerStand.getKeyFrame(stateTime, true);
        break;
    }
    // these conditions are needed for the sprite to be mirrored
    if ((playerBody.getLinearVelocity().x < 0 || !toRight) && !region.isFlipX()) {
      region.flip(true, false);
      toRight = false;
    } else if ((playerBody.getLinearVelocity().x > 0 || toRight) && region.isFlipX()) {
      region.flip(true, false);
      toRight = true;
    }
    if(currentState == previousState)
      stateTime = currentState==State.STANDING? stateTime + dt : stateTime + dt * Math.abs(joyStickValueX); // joystick's valueX is added so that animation is aligned with speed
    else stateTime=0; // we need to avoid problems with animation
    previousState = currentState;
    return region;
  }

  public void jump(final float yMaxAccel) {
    if (!Main.touchedBouncer && getCurrentState() != State.JUMPING) {
      performJump(yMaxAccel);
    }
  }

  public void performJump(final float yMaxAccel) {
    setCurrentState(State.JUMPING);
    playerBody.applyLinearImpulse(new Vector2(0, yMaxAccel), playerBody.getWorldCenter(), true);
    Main.getMusic().jumpSoundPlay();
  }

  public State getState() {
    if (playerBody.getLinearVelocity().y >= -0.2f
        && playerBody.getLinearVelocity().y < 0f
        && currentState != State.JUMPING) return State.SLIDING;
    else if (playerBody.getLinearVelocity().y > 0f && currentState == State.JUMPING // WARN: y>0f && cs==s.jumping
        || playerBody.getLinearVelocity().y < 0f
        || headInContact) return State.JUMPING;
    else if (playerBody.getLinearVelocity().x != 0f) return State.RUNNING;
    else return State.STANDING;
  }

  // @Override
  // public void dispose() {
  //   atlas.dispose();
  // }

  public void setHeadInContact(boolean headInContact) {
    this.headInContact = headInContact;
  }

  public boolean isHeadInContact() {
    return this.headInContact;
  }

  public float getPosX() {
    return playerBody.getPosition().x;
  }

  public float getPosY() {
    return playerBody.getPosition().y;
  }

  public static int getDeaths() {
    return deaths;
  }

  public static void incrementDeaths() {
    Player.deaths++;
  }

  public Body getPlayerBody() {
    return playerBody;
  }

  public State getCurrentState() {
    return currentState;
  }

  public void setCurrentState(State currentState) {
    this.currentState = currentState;
  }

  public boolean isDead() {
    return dead;
  }

  public void setDead(boolean dead) {
    this.dead = dead;
  }
}
