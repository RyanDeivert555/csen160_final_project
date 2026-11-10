package csen160.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

// TODO: change adapter to listener
public class InputManager implements KeyListener, MouseListener, MouseMotionListener {
    private static final int KEY_COUNT = 256;
    private static final int MOUSE_BUTTON_COUNT = 3; // BUTTON1, BUTTON2, BUTTON3

    // raw state must be synchronized since key input runs on separate thread
    private final boolean[] rawKeyboardState = new boolean[KEY_COUNT];
    private final boolean[] currentKeyboardState = new boolean[KEY_COUNT];
    private final boolean[] previousKeyboardState = new boolean[KEY_COUNT];

    private final boolean[] rawMouseState = new boolean[MOUSE_BUTTON_COUNT];
    private final boolean[] currentMouseState = new boolean[MOUSE_BUTTON_COUNT];
    private final boolean[] previousMouseState = new boolean[MOUSE_BUTTON_COUNT];

    // TODO: thread stuff :(
    private int mouseX;
    private int mouseY;

    @Override
    public void keyPressed(KeyEvent event) {
        final var code = event.getKeyCode();
        if (code >= 0 && code < KEY_COUNT) {
            synchronized (rawKeyboardState) {
                rawKeyboardState[code] = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        final var code = event.getKeyCode();
        if (code >= 0 && code < KEY_COUNT) {
            synchronized (rawKeyboardState) {
                rawKeyboardState[code] = false;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
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
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
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
        System.arraycopy(currentKeyboardState, 0, previousKeyboardState, 0, KEY_COUNT);
        synchronized (rawKeyboardState) {
            System.arraycopy(rawKeyboardState, 0, currentKeyboardState, 0, KEY_COUNT);
        }

        System.arraycopy(currentMouseState, 0, previousMouseState, 0, MOUSE_BUTTON_COUNT);
        synchronized (rawMouseState) {
            System.arraycopy(rawMouseState, 0, currentMouseState, 0, MOUSE_BUTTON_COUNT);
        }
    }

    public boolean isKeyDown(int code) {
        return currentKeyboardState[code];
    }

    public boolean isKeyPressed(int code) {
        return currentKeyboardState[code] && !previousKeyboardState[code];
    }

    public boolean isKeyReleased(int code) {
        return !currentKeyboardState[code] && previousKeyboardState[code];
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
