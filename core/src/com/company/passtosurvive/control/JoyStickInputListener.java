package com.company.passtosurvive.control;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class JoyStickInputListener extends InputListener {
  private JoyStick joyStick;

  public JoyStickInputListener(JoyStick joyStick) {
    this.joyStick = joyStick;
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { // runs
                                                                                          // when
                                                                                          // the
                                                                                          // joystick
                                                                                          // is
                                                                                          // pressed
    joyStick.setTouched();
    joyStick.changeCursor(x, y);
    return true;
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) { // runs when
                                                                                     // the joystick
                                                                                     // is released
    joyStick.setUnTouched();
  }

  @Override
  public void touchDragged(InputEvent event, float x, float y, int pointer) { // runs when the
                                                                              // joystick is held
                                                                              // down
                                                                              // and its cursor is
                                                                              // dragged
    joyStick.changeCursor(x, y);
    if (joyStick.isJoyStickDown()) { // check pressing
      joyStick.handleChangeListener();
    }
  }
}
