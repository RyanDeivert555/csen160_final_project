package csen160.core;

import csen160.App;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Utility class responsible for loading and caching classpath image textures
 * and audio clips for the game.
 */
public class AssetLoader {

    /**
     * Cache mapping normalized resource paths to loaded {@link BufferedImage}
     * textures.
     */
    private static HashMap<String, BufferedImage> textures = new HashMap<>();

    /**
     * Cache mapping normalized resource paths to loaded {@link Clip} audio
     * resources.
     */
    private static HashMap<String, Clip> sounds = new HashMap<>();

    /**
     * Opens an {@link InputStream} for a classpath resource relative to
     * {@link App}.
     *
     * @param path the classpath resource path
     * @return a non-null {@link InputStream} for the requested resource
     * @throws NullPointerException if no resource is found at the specified
     *                              {@code path}
     */
    public static InputStream getResourceStream(String path) {
        final var stream = App.class.getResourceAsStream(path);

        return Objects.requireNonNull(stream);
    }

    /**
     * Loads a {@link BufferedImage} texture from the classpath, returning a cached
     * instance
     * if the resource has already been loaded. Automatically prepends {@code "/"}
     * if missing.
     *
     * @param path the classpath resource path to the image file
     * @return the loaded or cached {@link BufferedImage}
     * @throws RuntimeException     if an {@link IOException} occurs while reading
     *                              the image
     * @throws NullPointerException if the resource cannot be found on the classpath
     */
    public static BufferedImage loadTexture(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (textures.containsKey(path)) {
            return textures.get(path);
        }

        final var stream = getResourceStream(path);

        try {
            final var texture = ImageIO.read(stream);
            textures.put(path, texture);

            return texture;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource at \"" + path + "\"", e);
        }
    }

    /**
     * Loads and opens an audio {@link Clip} from the classpath, returning a cached
     * instance
     * if the resource has already been loaded. Automatically prepends {@code "/"}
     * if missing.
     *
     * @param path the classpath resource path to the audio file
     * @return the initialized or cached {@link Clip}
     * @throws RuntimeException     if the audio format is unsupported, an I/O error
     *                              occurs,
     *                              or an audio line is unavailable
     * @throws NullPointerException if the resource cannot be found on the classpath
     */
    public static Clip loadSound(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (sounds.containsKey(path)) {
            return sounds.get(path);
        }

        try (
                final var stream = new BufferedInputStream(getResourceStream(path));
                final var audio = AudioSystem.getAudioInputStream(stream);) {
            final var clip = AudioSystem.getClip();
            clip.open(audio);

            sounds.put(path, clip);

            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("Failed to load resource at \"" + path + "\"", e);
        }
    }
}
