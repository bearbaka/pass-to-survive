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
  private State
      currentState; // two statuses are needed to avoid problems with animation and to determine the
  // status in different situations
  private Body playerBody;
  private static BodyDef bDef;
  private static TextureAtlas atlas;
  private static TextureRegion playerJump;
  private static Animation<TextureRegion> playerRun, playerStand;
  private boolean
      endingBouncer; // to start `interactive` animation in level 2 of part 1 to transit to part 2
  private boolean touchedBouncer;
  private float animationStateTime;
  private boolean toRight, headInContact;
  private State previousState;

  static {
    atlas = new TextureAtlas("Player.pack");
    Array<TextureRegion> frames = new Array<TextureRegion>();
    frames.add(atlas.findRegion("playerRun1"));
    frames.add(atlas.findRegion("playerRun2"));
    playerRun = new Animation(0.2f, frames);
    frames.clear();
    frames.add(atlas.findRegion("playerStay"));
    frames.add(atlas.findRegion("playerStay1"));
    frames.add(atlas.findRegion("playerStay2"));
    playerStand = new Animation(0.25f, frames);
    frames.clear();
    playerJump =
        new TextureRegion(
            atlas.findRegion("playerFall"),
            0,
            0,
            78,
            93); // for jumping and falling I have one frame so this is not animation
    bDef = new BodyDef();
    bDef.type = BodyDef.BodyType.DynamicBody;
  }

  private static void changeSkin() {
    if (deaths == 20) {
      Array<TextureRegion> frames = new Array<TextureRegion>();
      frames.add(atlas.findRegion("ghoulRun1"));
      frames.add(atlas.findRegion("ghoulRun2"));
      playerRun = new Animation(0.2f, frames);
      frames.clear();
      frames.add(atlas.findRegion("ghoulStay"));
      frames.add(atlas.findRegion("ghoulStay1"));
      frames.add(atlas.findRegion("ghoulStay2"));
      playerStand = new Animation(0.25f, frames);
      frames.clear();
      playerJump.setRegion(atlas.findRegion("ghoulFall"));
    }
  }

  public void reset(float x, float y) {
    currentState = State.STANDING;
    previousState = State.STANDING;
    animationStateTime = 0;
    headInContact = false;
    toRight = true; // by default the player model will always look to the right
    changeSkin();
    setPosition(x, y);
    bDef.position.set(x, y);
    if (playerBody != null) playerBody.setTransform(x, y, playerBody.getAngle());
  }

  public Player(World world, float x, float y) {
    setBounds(0, 0, 63 / (1.4f * Main.PPM), 72 / (1.4f * Main.PPM)); // sprite's bounds
    reset(x, y);
    playerBody = world.createBody(bDef);
    FixtureDef fDef = new FixtureDef();
    fDef.friction = 0f;
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
    setPosition(getBodyPositionX() - getWidth() / 2, getBodyPositionY() - getHeight() / 2.3f);
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
        region = (TextureRegion) playerRun.getKeyFrame(animationStateTime, true);
        break;
      case STANDING:
      default:
        region = (TextureRegion) playerStand.getKeyFrame(animationStateTime, true);
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
    if (currentState == previousState)
      animationStateTime =
          currentState == State.STANDING
              ? animationStateTime + dt
              : animationStateTime
                  + dt
                      * Math.abs(joyStickValueX); // joystick's valueX is added so that animation is
    // aligned with speed
    else animationStateTime = 0; // we need to avoid problems with animation
    previousState = currentState;
    return region;
  }

  public void jump(final float yMaxAccel) {
    if (!endingBouncer && !touchedBouncer) {
      performJump(yMaxAccel);
    }
  }

  public void performJump(final float yMaxAccel) {
    if (currentState != State.JUMPING) {
      playerBody.applyLinearImpulse(new Vector2(0, yMaxAccel), playerBody.getWorldCenter(), true);
      Main.getMusic().jumpSoundPlay();
    }
  }

  public State getState() {
    if (playerBody.getLinearVelocity().y >= -0.2f
        && playerBody.getLinearVelocity().y < 0f
        && currentState != State.JUMPING) return State.SLIDING;
    else if (playerBody.getLinearVelocity().y > 0f
        || playerBody.getLinearVelocity().y < 0f
        || headInContact) return State.JUMPING;
    else if (playerBody.getLinearVelocity().x != 0f) return State.RUNNING;
    else return State.STANDING;
  }

  public boolean isTouchedBouncer() {
    return touchedBouncer;
  }

  void setTouchedBouncer(boolean touchedBouncer) {
    this.touchedBouncer = touchedBouncer;
  }

  public boolean isEndingBouncer() {
    return endingBouncer;
  }

  void setEndingBouncer(boolean endingBouncer) {
    this.endingBouncer = endingBouncer;
  }

  public void setHeadInContact(boolean headInContact) {
    this.headInContact = headInContact;
  }

  public boolean isHeadInContact() {
    return this.headInContact;
  }

  public float getBodyPositionX() {
    return playerBody.getPosition().x;
  }

  public float getBodyPositionY() {
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
}
