package csen160;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputManager extends KeyAdapter {
    private static final int KEY_COUNT = 256;

    // TODO: do we need syncronization?
    private final boolean[] rawState = new boolean[KEY_COUNT];
    private final boolean[] currentState = new boolean[KEY_COUNT];
    private final boolean[] previousState = new boolean[KEY_COUNT];

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
                rawState[code] = true;
            }
        }
    }

    public void update() {
        System.arraycopy(currentState, 0, previousState, 0, KEY_COUNT);
        synchronized (rawState) {
            System.arraycopy(rawState, 0, currentState, 0, KEY_COUNT);
        }
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
}
