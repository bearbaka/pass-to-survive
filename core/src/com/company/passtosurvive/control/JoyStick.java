package com.company.passtosurvive.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.company.passtosurvive.view.Main;

public class JoyStick extends Actor {
  private static final Texture JoyStick;
  private static final Texture curJoyStick;
  private boolean JoyStickDown;
  private final float radius;
  private final float borderRadius;
  private final float cursorRadius;
  private final float effectiveRadius;
  private float CurJoyStickX;
  private float CurJoyStickY;
  private float valueX;
  private float valueY;

  static {
    JoyStick = new Texture(Gdx.files.internal("JoyStick.png"));
    curJoyStick = new Texture(Gdx.files.internal("CurJoyStick.png"));
  }

  public float getValueX() {
    return valueX;
  } // valueX we need to use joystick for the player control

  public float getValueY() {
    return valueY;
  } // valueY we need to use joystick for cheats in levels

  public boolean isJoyStickDown() {
    return JoyStickDown;
  }

  public JoyStick() {
    JoyStickDown = false;
    CurJoyStickX = 0;
    CurJoyStickY = 0;
    valueX = 0;
    valueY = 0;
    addListener(new JoyStickInputListener(this));
    setX(140 * Main.getScreenWidth() / 1794);
    setY(40 * Main.getScreenHeight() / 1080);
    setWidth(400 * Main.getScreenWidth() / 1794);
    setHeight(400 * Main.getScreenWidth() / 1794);
    cursorRadius = 90 * Main.getScreenWidth() / 1794;
    radius = getWidth() / 2;
    borderRadius = getWidth() / 2 - cursorRadius;
    effectiveRadius = borderRadius / 3;
  }

  public void setTouched() {
    Gdx.input.vibrate(100);
    JoyStickDown = true;
  }

  public void setUnTouched() {
    Gdx.input.vibrate(100);
    valueX = 0;
    valueY = 0;
    CurJoyStickX = 0;
    CurJoyStickY = 0;
    JoyStickDown = false;
  }

  @Override
  public Actor hit(float x, float y, boolean touchable) { // here we check whether our finger is
    // in the joystick field
    Actor actor = super.hit(x, y, touchable);
    if (actor == null) return null;
    else {
      float dx = x - radius;
      float dy = y - radius;
      return (Math.pow(dx, 2) + Math.pow(dy, 2) <= Math.pow(radius, 2))
          ? this
          : null; // if yes then return actor if not then nothing
    }
  }

  public void changeCursor(float x, float y) { // x y we get from the listener, these are the
    // coordinates where the joystick was pressed
    float dx =
        x - radius; // how much the joystick coordinates have been changed with respect to radius
    float dy = y - radius;
    float length =
        (float)
            Math.sqrt(
                Math.pow(dx, 2)
                    + Math.pow(
                        dy,
                        2)); // calculate the length from the center of the joystick to the point
    // where the player pressed
    if (length
        <= borderRadius) { // will run if the length from the center to the point where the player
      // pressed is less than the radius
      this.CurJoyStickX = dx;
      this.CurJoyStickY = dy;
    } else { // thanks to this, the cursor will not leave the joystick field
      float k = borderRadius / length;
      this.CurJoyStickX = dx * k;
      this.CurJoyStickY = dy * k;
    }
    if (Math.abs(CurJoyStickX) > effectiveRadius) {
      valueX =
          CurJoyStickX < 0
              ? -1
              : 1; // if the player pulled the cursor to the right then valueX is greater than 0, if to the left then vice versa, valueX=[-1,1]
    } else valueX = CurJoyStickX / effectiveRadius;
    if (Math.abs(CurJoyStickY) > effectiveRadius) {
      valueY = CurJoyStickY < 0 ? -1 : 1;
    } else valueY = CurJoyStickY / effectiveRadius;
  }

  public void setWidth(float width) { // set it so that the joystick is round
    super.setWidth(width);
    super.setHeight(width);
  }

  public void setHeight(float height) { // set it so that the joystick is round
    super.setWidth(height);
    super.setHeight(height);
  }

  @Override
  public void draw(
      Batch batch, float parentAlpha) { // stage will run this method using its draw method
    batch.draw(
        JoyStick,
        this.getX(),
        this.getY(),
        this.getWidth(),
        this.getHeight()); // joystick is static
    batch.draw(
        curJoyStick,
        this.getX() + radius - cursorRadius + CurJoyStickX,
        this.getY() + radius - cursorRadius + CurJoyStickY,
        2 * cursorRadius,
        2 * cursorRadius);
  }
}
