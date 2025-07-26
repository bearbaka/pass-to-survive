package com.company.passtosurvive.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.company.passtosurvive.view.Main;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class Player extends Sprite implements Disposable { // the player model is in every level screen
  public enum State { // statuses are needed to change the texture of a person
    JUMPING,
    RUNNING,
    STANDING,
    SLIDING
  };

  @Getter private static int restarts;
  private BodyDef bDef;
  private TextureAtlas atlas;
  private TextureRegion playerJump;
  private Animation<TextureRegion> playerRun, playerStand;

  public static void incrementRestarts() {
    Player.restarts++;
  }
  
  private void setPlayerSkin(int n) {
      final Array<TextureRegion> frames = new Array<TextureRegion>();
      frames.add(atlas.findRegion("player"+n+"Run1"));
      frames.add(atlas.findRegion("player"+n+"Run2"));
      playerRun = new Animation<TextureRegion>(0.2f, frames);
      frames.clear();
      frames.add(atlas.findRegion("player"+n+"Stay"));
      frames.add(atlas.findRegion("player"+n+"Stay1"));
      frames.add(atlas.findRegion("player"+n+"Stay2"));
      playerStand = new Animation<TextureRegion>(0.25f, frames);
      frames.clear();
      if( playerJump!=null ) playerJump.setRegion(atlas.findRegion("player"+n+"Fall"));// for jumping and falling I have one frame so this is not animation
      else playerJump = new TextureRegion(atlas.findRegion("player"+n+"Fall"), 0, 0, 78, 93);
  }

  private State
      currentState; // two statuses are needed to avoid problems with animation and to determine the
                    // status in different situations
  private final Body playerBody;
  public Vector2 getPosition() {
    return playerBody.getPosition();
  }

  public Vector2 getWorldCenter() {
    return playerBody.getWorldCenter();
  }

  public Vector2 getLinearVelocity() {
    return playerBody.getLinearVelocity();
  }

  public void applyLinearImpulse(Vector2 impulse, Vector2 point, boolean wake) {
    playerBody.applyLinearImpulse(impulse, point, wake);
  }

  public Array<Fixture> getFixtureList() {
    return playerBody.getFixtureList();
  }

  @Getter
  @Setter(AccessLevel.PACKAGE)
  private boolean endingBouncer,
      touchedBouncer; // endingBouncer is to start `interactive` animation in level 2 of part 1 to
                      // transit to part 2
  private float animationStateTime;

  private boolean toRight;

  @Getter @Setter private boolean headInContact;

  private State previousState;

  public Player(final World world, final float x, final float y) {
    atlas = new TextureAtlas("Player.pack");
    setPlayerSkin(restarts>=20? 2:1);
    bDef = new BodyDef();
    bDef.type = BodyDef.BodyType.DynamicBody;
    setBounds(0, 0, 63 / (1.4f * Main.PPM), 72 / (1.4f * Main.PPM)); // sprite's bounds
    reset(x, y);
    playerBody = world.createBody(bDef);
    final FixtureDef fDef = new FixtureDef();
    fDef.friction = 0f;
    final EdgeShape vertice =
        new EdgeShape(); // we need edgeshape to make it look like walls around the PlayerBody
    vertice.set(
        new Vector2(-5f / Main.PPM, -21.5f / Main.PPM),
        new Vector2(5f / Main.PPM, -21.5f / Main.PPM)); // bottom wall
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("Player");
    vertice.set(
        new Vector2(-5f / Main.PPM, -21.5f / Main.PPM),
        new Vector2(-11.5f / Main.PPM, -15f / Main.PPM)); // part connecting bottom & side wall
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("Player");
    vertice.set(
        new Vector2(5f / Main.PPM, -21.5f / Main.PPM),
        new Vector2(11.5f / Main.PPM, -15f / Main.PPM)); // part connecting bottom & side wall
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("Player");
    vertice.set(
        new Vector2(11.5f / Main.PPM, -15f / Main.PPM),
        new Vector2(11.5f / Main.PPM, 21.5f / Main.PPM)); // side wall
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("Player");
    vertice.set(
        new Vector2(-11.5f / Main.PPM, -15f / Main.PPM),
        new Vector2(-11.5f / Main.PPM, 21.5f / Main.PPM)); // side wall
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("Player");
    vertice.set(
        new Vector2(-9f / Main.PPM, 21.5f / Main.PPM),
        new Vector2(9f / Main.PPM, 21.5f / Main.PPM)); // the top wall is head
    fDef.shape = vertice;
    playerBody.createFixture(fDef).setUserData("PlayerHead");
    vertice.dispose();
  }

  public void reset(final float x, final float y) {
    currentState = State.STANDING;
    previousState = State.STANDING;
    animationStateTime = 0;
    headInContact = false;
    toRight = true; // by default the player model will always look to the right
    if (restarts == 20) setPlayerSkin(2);
    setPosition(x, y);
    bDef.position.set(x, y);
    if (playerBody != null) playerBody.setTransform(x, y, playerBody.getAngle());
  }

  public void update(
      final float delta,
      final float
          joyStickValueX) { // needed for the sprite to move with the body of the person in the
                            // world so that it is attracted to him
    setPosition(
        getPosition().x - getWidth() / 2,
        getPosition().y - getHeight() / 2.3f);
    setRegion(getFrame(delta, joyStickValueX)); // texture update
  }

  public void setLinearVelocity(float vX, float vY) {
    playerBody.setLinearVelocity(vX, vY);
  }

  public TextureRegion getFrame(final float dt, final float joyStickValueX) {
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
                      * Math.abs(
                          joyStickValueX); // joystick's valueX is added so that animation is
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

  @Override
  public void dispose() {
    atlas.dispose();
  }
}
