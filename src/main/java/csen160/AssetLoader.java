package csen160;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AssetLoader {
    public static BufferedImage loadTexture(String path) {
        if (!path.startsWith("/")) {
            throw new RuntimeException("Classpath resource must start with \"/\"");
        }

        try {
            final var stream = App.class.getResourceAsStream(path);

            return ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource at \\" + path + "\\", e);
        }
    }
}
