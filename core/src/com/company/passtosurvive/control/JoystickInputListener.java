package com.company.passtosurvive.control;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class JoystickInputListener extends InputListener {
  private final Joystick joyStick;

  public JoystickInputListener(final Joystick joyStick) {
    this.joyStick = joyStick;
  }

  @Override
  public boolean touchDown(
      final InputEvent event,
      final float x,
      final float y,
      final int pointer,
      final int button) { // runs when the joystick is pressed
    joyStick.setTouched();
    joyStick.changeCursor(x, y);
    return true;
  }

  @Override
  public void touchUp(
      final InputEvent event,
      final float x,
      final float y,
      final int pointer,
      final int button) { // runs when the joystick is released
    joyStick.setUnTouched();
  }

  @Override
  public void touchDragged(
      final InputEvent event,
      final float x,
      final float y,
      final int pointer) { // runs when the joystick is held down and its cursor is dragged
    joyStick.changeCursor(x, y);
  }
}
