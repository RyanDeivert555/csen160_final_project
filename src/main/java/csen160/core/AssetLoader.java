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

public class AssetLoader {
    private static HashMap<String, BufferedImage> textures = new HashMap<>();
    private static HashMap<String, Clip> sounds = new HashMap<>();

    private static InputStream getResourceStream(String path) {
        final var stream = App.class.getResourceAsStream(path);

        return Objects.requireNonNull(stream);
    }

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

    public static Clip loadSound(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (sounds.containsKey(path)) {
            return sounds.get(path);
        }

        try (
                // TODO: does this need to be a BufferedInputStream?
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
