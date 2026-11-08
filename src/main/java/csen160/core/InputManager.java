package csen160.core;

import csen160.App;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputManager extends KeyAdapter implements MouseListener {
    private static final int KEY_COUNT = 256;
    private static final int MOUSE_BUTTON_COUNT = 4; // NONE , PRIMARY , MIDDLE , SECONDARY

    // rawState must be synchronized since key input runs on separate thread
    private final boolean[] rawState = new boolean[KEY_COUNT];
    private final boolean[] currentState = new boolean[KEY_COUNT];
    private final boolean[] previousState = new boolean[KEY_COUNT];

    private final boolean[] rawMouseState = new boolean[MOUSE_BUTTON_COUNT];
    private final boolean[] currentMouseState = new boolean[MOUSE_BUTTON_COUNT];
    private final boolean[] previousMouseState = new boolean[MOUSE_BUTTON_COUNT];

    private int mouseX;
    private int mouseY;

    @Override
    public void keyPressed(KeyEvent event) {
        final var code = event.getKeyCode();
        if (code >= 0 && code < KEY_COUNT) {
            synchronized (rawState) {
                rawState[code] = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        final var code = event.getKeyCode();
        if (code >= 0 && code < KEY_COUNT) {
            synchronized (rawState) {
                rawState[code] = false;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        if (button > 0 && button < MOUSE_BUTTON_COUNT) {
            synchronized (rawMouseState) {
                rawMouseState[button] = true;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton();
        if (button > 0 && button < MOUSE_BUTTON_COUNT) {
            synchronized (rawMouseState) {
                rawMouseState[button] = false;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void update() {
        System.arraycopy(currentState, 0, previousState, 0, KEY_COUNT);
        System.arraycopy(currentMouseState, 0, previousMouseState, 0, MOUSE_BUTTON_COUNT);

        synchronized (rawState) {
            System.arraycopy(rawState, 0, currentState, 0, KEY_COUNT);
        }
        synchronized (rawMouseState) {
            System.arraycopy(rawMouseState, 0, currentMouseState, 0, MOUSE_BUTTON_COUNT);
        }

        Point mouse = MouseInfo.getPointerInfo().getLocation();
        Point origin = App.getFrame().getContentPane().getLocationOnScreen();
        mouseX = mouse.x - origin.x;
        mouseY = mouse.y - origin.y;
    }

    public boolean isKeyDown(int code) {
        return currentState[code];
    }

    public boolean isKeyPressed(int code) {
        return currentState[code] && !previousState[code];
    }

    public boolean isKeyReleased(int code) {
        return !currentState[code] && previousState[code];
    }

    public boolean isMouseDown(int button) {
        return currentMouseState[button];
    }

    public boolean isMousePressed(int button) {
        return currentMouseState[button] && !previousMouseState[button];
    }

    public boolean isMouseReleased(int button) {
        return !currentMouseState[button] && previousMouseState[button];
    }


    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
