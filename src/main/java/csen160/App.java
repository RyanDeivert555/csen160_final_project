package csen160;

import csen160.core.*;
import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        final var frame = new JFrame("Final Project");
        final var game = new GameContext();
        final var renderer = game.getRenderer();
        renderer.setSize(800, 600);

        frame.add(renderer);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.run();
        System.exit(0);
    }
}
