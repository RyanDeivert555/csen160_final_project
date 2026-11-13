package csen160.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Captures and polls keyboard and mouse input states across frames.
 * <p>
 * Raw input events received on the AWT Event Dispatch Thread are synchronized
 * and
 * buffered into frame-consistent {@code current} and {@code previous} state
 * arrays
 * when {@link #update()} is called, enabling edge-triggered queries such as
 * pressed and released events.
 */
public class InputManager implements KeyListener, MouseListener, MouseMotionListener {

    /** Maximum number of virtual key codes tracked by the keyboard state arrays. */
    private static final int KEY_COUNT = 256;

    /** Number of mouse button slots tracked by the mouse state arrays. */
    private static final int MOUSE_BUTTON_COUNT = 3; // BUTTON1, BUTTON2, BUTTON3

    // raw state must be synchronized since key input runs on separate thread
    /**
     * Live asynchronous key states updated directly by AWT key listener callbacks.
     */
    private final boolean[] rawKeyboardState = new boolean[KEY_COUNT];

    /** Key states sampled for the current game frame. */
    private final boolean[] currentKeyboardState = new boolean[KEY_COUNT];

    /** Key states sampled from the preceding game frame. */
    private final boolean[] previousKeyboardState = new boolean[KEY_COUNT];

    /**
     * Live asynchronous mouse button states updated directly by AWT mouse listener
     * callbacks.
     */
    private final boolean[] rawMouseState = new boolean[MOUSE_BUTTON_COUNT];

    /** Mouse button states sampled for the current game frame. */
    private final boolean[] currentMouseState = new boolean[MOUSE_BUTTON_COUNT];

    /** Mouse button states sampled from the preceding game frame. */
    private final boolean[] previousMouseState = new boolean[MOUSE_BUTTON_COUNT];

    // TODO: thread stuff :(
    /**
     * The most recently recorded horizontal cursor position in component
     * coordinates.
     */
    private int mouseX;

    /**
     * The most recently recorded vertical cursor position in component coordinates.
     */
    private int mouseY;

    /**
     * Records a key press in the synchronized raw keyboard state buffer.
     *
     * @param event the {@link KeyEvent} triggered when a key is pressed
     */
    @Override
    public void keyPressed(KeyEvent event) {
        final var code = event.getKeyCode();
        if (code >= 0 && code < KEY_COUNT) {
            synchronized (rawKeyboardState) {
                rawKeyboardState[code] = true;
            }
        }
    }

    /**
     * Records a key release in the synchronized raw keyboard state buffer.
     *
     * @param event the {@link KeyEvent} triggered when a key is released
     */
    @Override
    public void keyReleased(KeyEvent event) {
        final var code = event.getKeyCode();
        if (code >= 0 && code < KEY_COUNT) {
            synchronized (rawKeyboardState) {
                rawKeyboardState[code] = false;
            }
        }
    }

    /**
     * Invoked when a key has been typed. Unused in this polling implementation.
     *
     * @param e the {@link KeyEvent} triggered when a character is typed
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Records a mouse button press in the synchronized raw mouse state buffer.
     *
     * @param e the {@link MouseEvent} triggered when a mouse button is pressed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        if (button > 0 && button < MOUSE_BUTTON_COUNT) {
            synchronized (rawMouseState) {
                rawMouseState[button] = true;
            }
        }
    }

    /**
     * Records a mouse button release in the synchronized raw mouse state buffer.
     *
     * @param e the {@link MouseEvent} triggered when a mouse button is released
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton();
        if (button > 0 && button < MOUSE_BUTTON_COUNT) {
            synchronized (rawMouseState) {
                rawMouseState[button] = false;
            }
        }
    }

    /**
     * Updates the tracked cursor coordinates when the mouse moves without buttons
     * held.
     *
     * @param e the {@link MouseEvent} containing the updated cursor position
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * Updates the tracked cursor coordinates when the mouse moves with a button
     * held down.
     *
     * @param e the {@link MouseEvent} containing the updated cursor position
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released).
     * Unused.
     *
     * @param e the {@link MouseEvent} triggered on click
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Invoked when the mouse cursor enters the component bounds. Unused.
     *
     * @param e the {@link MouseEvent} triggered on entry
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Invoked when the mouse cursor exits the component bounds. Unused.
     *
     * @param e the {@link MouseEvent} triggered on exit
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Advances the input snapshot by copying the current frame's state into the
     * previous frame buffer and synchronizing the latest raw AWT states into the
     * current frame buffer. Should be called once per game tick before querying
     * input.
     */
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

    /**
     * Checks whether the specified key is currently held down during this frame.
     *
     * @param code the {@link KeyEvent} virtual key code to query
     * @return {@code true} if the key is currently down; {@code false} otherwise
     */
    public boolean isKeyDown(int code) {
        return currentKeyboardState[code];
    }

    /**
     * Checks whether the specified key was newly pressed during this frame
     * (down in the current frame, up in the previous frame).
     *
     * @param code the {@link KeyEvent} virtual key code to query
     * @return {@code true} on the single frame the key transitions from up to down;
     *         {@code false} otherwise
     */
    public boolean isKeyPressed(int code) {
        return currentKeyboardState[code] && !previousKeyboardState[code];
    }

    /**
     * Checks whether the specified key was newly released during this frame
     * (up in the current frame, down in the previous frame).
     *
     * @param code the {@link KeyEvent} virtual key code to query
     * @return {@code true} on the single frame the key transitions from down to up;
     *         {@code false} otherwise
     */
    public boolean isKeyReleased(int code) {
        return !currentKeyboardState[code] && previousKeyboardState[code];
    }

    /**
     * Checks whether the specified mouse button is currently held down during this
     * frame.
     *
     * @param button the {@link MouseEvent} button identifier to query
     * @return {@code true} if the mouse button is currently down; {@code false}
     *         otherwise
     */
    public boolean isMouseDown(int button) {
        return currentMouseState[button];
    }

    /**
     * Checks whether the specified mouse button was newly pressed during this frame
     * (down in the current frame, up in the previous frame).
     *
     * @param button the {@link MouseEvent} button identifier to query
     * @return {@code true} on the single frame the button transitions from up to
     *         down;
     *         {@code false} otherwise
     */
    public boolean isMousePressed(int button) {
        return currentMouseState[button] && !previousMouseState[button];
    }

    /**
     * Checks whether the specified mouse button was newly released during this
     * frame
     * (up in the current frame, down in the previous frame).
     *
     * @param button the {@link MouseEvent} button identifier to query
     * @return {@code true} on the single frame the button transitions from down to
     *         up;
     *         {@code false} otherwise
     */
    public boolean isMouseReleased(int button) {
        return !currentMouseState[button] && previousMouseState[button];
    }

    /**
     * Returns the latest horizontal mouse cursor coordinate in screen/component
     * space.
     *
     * @return the mouse x-coordinate in pixels
     */
    public int getMouseX() {
        return mouseX;
    }

    /**
     * Returns the latest vertical mouse cursor coordinate in screen/component
     * space.
     *
     * @return the mouse y-coordinate in pixels
     */
    public int getMouseY() {
        return mouseY;
    }
}
