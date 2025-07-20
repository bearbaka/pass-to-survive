package com.company.passtosurvive.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.company.passtosurvive.view.Main;

import lombok.Getter;

public class JoyStick extends Actor {
  private static final Texture JoyStick, curJoyStick;
  static {
    JoyStick = new Texture(Gdx.files.internal("JoyStick.png"));
    curJoyStick = new Texture(Gdx.files.internal("CurJoyStick.png"));
  }
  @Getter private boolean JoyStickDown;
  private final float radius, borderRadius, cursorRadius, effectiveRadius;
  private float CurJoyStickX, CurJoyStickY;

  @Getter private float valueX, valueY;

  public JoyStick() {
    JoyStickDown = false;
    CurJoyStickX = 0;
    CurJoyStickY = 0;
    valueX = 0;
    valueY = 0;
    addListener(new JoyStickInputListener(this));
    setX(140 * Main.getScreenWidth() / 1794);
    setY(40 * Main.getScreenHeight() / 1080);
    setSize(400 * Main.getScreenWidth() / 1794);
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
  public Actor hit(
      final float x,
      final float y,
      final boolean touchable) { // here we check whether our finger is in the joystick field
    final Actor actor = super.hit(x, y, touchable);
    if (actor == null) return null;
    else {
      final float dx = x - radius;
      final float dy = y - radius;
      return (Math.pow(dx, 2) + Math.pow(dy, 2) <= Math.pow(radius, 2))
          ? this
          : null; // if yes then return actor if not then nothing
    }
  }

  public void changeCursor(
      final float x,
      final float
          y) { // x y we get from the listener, these are the coordinates where the joystick was
               // pressed
    final float dx =
        x - radius; // how much the joystick coordinates have been changed with respect to radius
    final float dy = y - radius;
    final float length =
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
      final float k = borderRadius / length;
      this.CurJoyStickX = dx * k;
      this.CurJoyStickY = dy * k;
    }
    if (Math.abs(CurJoyStickX) > effectiveRadius)
      valueX =
          CurJoyStickX < 0
              ? -1
              : 1; // if the player pulled the cursor to the right then valueX is greater than 0, if
                   // to the left then vice versa, valueX=[-1,1]
    else valueX = CurJoyStickX / effectiveRadius;
    if (Math.abs(CurJoyStickY) > effectiveRadius) valueY = CurJoyStickY < 0 ? -1 : 1;
    else valueY = CurJoyStickY / effectiveRadius;
  }

  public void setSize(final float size) { // set it so that the joystick is round
    super.setWidth(size);
    super.setHeight(size);
  }

  @Override
  public void draw(
      final Batch batch, final float parentAlpha) { // stage will run this method using its draw method
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
