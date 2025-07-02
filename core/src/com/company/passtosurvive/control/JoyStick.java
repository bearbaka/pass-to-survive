package com.company.passtosurvive.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.company.passtosurvive.view.Main;
import java.util.ArrayList;
import java.util.List;

public class JoyStick extends Actor implements Disposable {
  private Texture JoyStick;
  private Texture curJoyStick;
  private boolean isJoyStickDown;
  private float radius;
  private float borderRadius;
  private float cursorDiameter;
  private float CurJoyStickX;
  private float CurJoyStickY;
  private float valueX;
  private float valueY;
  private List<JoyStickChangedListener> listeners = new ArrayList<JoyStickChangedListener>();

  public float getValueX() {
    return valueX;
  } // valueX we need to use joystick for the player control
  public float getValueY() {
    return valueY;
  } // valueY we need to use joystick for cheats in levels

  public boolean isJoyStickDown() {
    return isJoyStickDown;
  }

  public void handleChangeListener() { // needed when the player drags the cursor
    for (JoyStickChangedListener listener : listeners) {
      listener.changed(valueX, valueY);
    }
  }

  public JoyStick() {
    isJoyStickDown = false;
    CurJoyStickX = 0;
    CurJoyStickY = 0;
    valueX = 0;
    valueY = 0;
    this.JoyStick = new Texture(Gdx.files.internal("JoyStick.png"));
    this.curJoyStick = new Texture(Gdx.files.internal("curJoyStick.png"));
    addListener(new JoyStickInputListener(this));
    setX(140 * Main.getWidth() / 1794);
    setY(40 * Main.getHeight() / 1080);
    setWidth(400 * Main.getWidth() / 1794);
    setHeight(400 * Main.getWidth() / 1794);
    cursorDiameter = 90 * Main.getWidth() / 1794;
    radius = getWidth() / 2;
    borderRadius = getWidth() / 2-cursorDiameter;
  }

  public void setTouched() {
    isJoyStickDown = true;
  }

  public void setUnTouched() {
    isJoyStickDown = false;
  }

  @Override
  public Actor hit(float x, float y, boolean touchable) { // here we check whether our finger is
                                                          // in the joystick field
    Actor actor = super.hit(x, y, touchable);
    if (actor == null)
      return null;
    else {
      float dx = x - radius;
      float dy = y - radius;
      return (dx * dx + dy * dy <= radius * radius) ? this : null; // if yes then return actor if
                                                                   // not then nothing
    }
  }

  public void changeCursor(float x, float y) { // x y we get from the listener, these are the
                                               // coordinates where the joystick was pressed
    float dx = x - radius; // how much the joystick coordinates have been changed with respect to radius
    float dy = y - radius;
    float length = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)); // calculate the length
                                                                             // from the center of
                                                                             // the
                                                                             // joystick to the
                                                                             // point where the
                                                                             // player pressed
    if (length <= borderRadius) { // will run if the length from the center to the point where
                           // the player pressed is less than the radius
      this.CurJoyStickX = dx;
      this.CurJoyStickY = dy;
    } else { // thanks to this, the cursor will not leave the joystick field
      float k = borderRadius / length;
      this.CurJoyStickX = dx * k;
      this.CurJoyStickY = dy * k;
    }
    valueX = CurJoyStickX / borderRadius; // if the player pulled the cursor to the
                                           // right then valueX is greater than 0, if
                                           // to the left then vice versa
    valueY = CurJoyStickY / borderRadius;
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
  public void draw(Batch batch, float parentAlpha) { // stage will run this method using its
                                                     // draw method
    batch.draw(JoyStick, this.getX(), this.getY(), this.getWidth(), this.getHeight()); // joystick
                                                                                       // is static
    if (isJoyStickDown) { // needed to draw the cursor in the place where the
                          // joystick was pressed
      batch.draw(curJoyStick, this.getX() + radius - cursorDiameter + CurJoyStickX,
          this.getY() + radius - cursorDiameter + CurJoyStickY, 2 * cursorDiameter, 2 * cursorDiameter);
    } else {
      batch.draw(curJoyStick, this.getX() + radius - cursorDiameter,
          this.getY() + radius - cursorDiameter, 2 * cursorDiameter, 2 * cursorDiameter);
    }
  }

  @Override
  public void dispose() {
    JoyStick.dispose();
    curJoyStick.dispose();
  }
}
