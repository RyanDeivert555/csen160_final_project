package csen160.entity;

import csen160.core.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

class TileSet {
    public record Region(int x, int y, int w, int h) {
    }

    public static final String SHEET_PATH = "/PiratePack/Tilesheet/tiles_sheet.png";
    public static final int TILE_SIZE = 64;
    public static final int SHEET_COLUMNS = 16;
    public static final int SHEET_ROWS = 6;

    public static final int TILE_SAND_CORNER_TL = 1;
    public static final int TILE_SAND_EDGE_TOP = 2;
    public static final int TILE_SAND_CORNER_TR = 3;
    public static final int TILE_SAND_PLAIN_BLOB_A = 4;
    public static final int TILE_SAND_PLAIN = 5;
    public static final int TILE_SAND_CORNER_TL_GRASS = 6;
    public static final int TILE_SAND_EDGE_TOP_GRASS_BOTTOM = 7;
    public static final int TILE_SAND_EDGE_TOP_GRASS_BUMP = 8;
    public static final int TILE_SAND_CORNER_TR_GRASS = 9;

    public static final int TILE_OVERLAY_CORNER_TL = 10;
    public static final int TILE_OVERLAY_EDGE_TOP = 11;
    public static final int TILE_OVERLAY_CORNER_TR = 12;

    public static final int TILE_PAD = 13;
    public static final int TILE_PAD_HATCH = 14;
    public static final int TILE_ROAD_V = 15;
    public static final int TILE_ROAD_H = 16;

    public static final int TILE_SAND_EDGE_LEFT = 17;
    public static final int TILE_SAND_DARK = 18;
    public static final int TILE_SAND_EDGE_RIGHT = 19;
    public static final int TILE_SAND_PLAIN_BLOB_B = 20;
    public static final int TILE_SAND_PLAIN_BLOB_C = 21;
    public static final int TILE_SAND_EDGE_LEFT_GRASS_RIGHT = 22;

    public static final int TILE_GRASS = 23;
    public static final int TILE_GRASS_SAND_SPECKS = 24;
    public static final int TILE_SAND_STRIP_GRASS_LEFT = 25;

    public static final int TILE_OVERLAY_EDGE_LEFT = 26;
    public static final int TILE_OVERLAY_FULL = 27;
    public static final int TILE_OVERLAY_EDGE_RIGHT = 28;

    public static final int TILE_ROAD_V_PAD = 29;
    public static final int TILE_ROAD_H_PAD = 30;
    public static final int TILE_ROAD_V_CANNON_RIGHT = 31;
    public static final int TILE_ROAD_V_CANNON_LEFT = 32;

    public static final int TILE_SAND_CORNER_BL = 33;
    public static final int TILE_SAND_EDGE_BOTTOM = 34;
    public static final int TILE_SAND_CORNER_BR = 35;
    public static final int TILE_GRASS_CORNER_TL_SAND = 36;
    public static final int TILE_GRASS_CORNER_TR_SAND = 37;
    public static final int TILE_SAND_EDGE_LEFT_GRASS_RIGHT_BUMPY = 38;
    public static final int TILE_GRASS_SPECKLE_A = 39;
    public static final int TILE_GRASS_SPECKLE_B = 40;
    public static final int TILE_GRASS_LEFT_SAND_EDGE_RIGHT = 41;

    public static final int TILE_OVERLAY_CORNER_BL = 42;
    public static final int TILE_OVERLAY_EDGE_BOTTOM = 43;
    public static final int TILE_OVERLAY_CORNER_BR = 44;

    public static final int TILE_PAD_EXIT_DOWN = 45;
    public static final int TILE_PAD_EXIT_RIGHT = 46;
    public static final int TILE_ROAD_H_CANNON_UP = 47;
    public static final int TILE_ROAD_H_CANNON_DOWN = 48;

    public static final int TILE_ROCK_SMALL = 49;
    public static final int TILE_ROCK_LARGE = 50;
    public static final int TILE_ROCK_MEDIUM = 51;

    public static final int TILE_SAND_GRASS_SIDES_TOP_BLOB = 52;
    public static final int TILE_SAND_GRASS_SIDES_TOP = 53;
    public static final int TILE_SAND_CORNER_BL_GRASS = 54;
    public static final int TILE_SAND_EDGE_BOTTOM_GRASS_TOP = 55;
    public static final int TILE_SAND_EDGE_BOTTOM_GRASS_BUMP = 56;
    public static final int TILE_SAND_CORNER_BR_GRASS = 57;

    public static final int TILE_OVERLAY_BLOB_BOTTOM_A = 58;
    public static final int TILE_OVERLAY_BLOB_BOTTOM_B = 59;

    public static final int TILE_BRIDGE_V = 60;
    public static final int TILE_PAD_EXIT_UP = 61;
    public static final int TILE_PAD_EXIT_LEFT = 62;
    public static final int TILE_ROAD_CAP_TOP = 63;
    public static final int TILE_ROAD_CAP_LEFT = 64;

    public static final int TILE_ROCK_MOSS_SMALL = 65;
    public static final int TILE_ROCK_MOSS_LARGE = 66;
    public static final int TILE_ROCK_MOSS_MEDIUM = 67;

    public static final int TILE_SAND_SPECKS_LIGHT = 68;
    public static final int TILE_SAND_SPECKS_DARK = 69;
    public static final int TILE_PLANT_SPROUT = 70;
    public static final int TILE_PLANT_STAR = 71;
    public static final int TILE_PLANT_BUSH = 72;

    public static final int TILE_WATER = 73;
    public static final int TILE_OVERLAY_BLOB_TOP_A = 74;
    public static final int TILE_OVERLAY_BLOB_TOP_B = 75;

    public static final int TILE_BRIDGE_H = 76;
    public static final int TILE_PAD_EXIT_RIGHT_DOWN = 77;
    public static final int TILE_PAD_EXIT_LEFT_DOWN = 78;
    public static final int TILE_ROAD_CAP_BOTTOM = 79;
    public static final int TILE_ROAD_CAP_RIGHT = 80;

    public static final int TILE_SAND_BOAT = 81;
    public static final int TILE_SAND_BOAT_GRASS_TOP = 82;
    public static final int TILE_SAND_CANNON_DEBRIS = 83;
    public static final int TILE_SAND_CANNON_DEBRIS_GRASS_TOP = 84;
    public static final int TILE_SAND_ROCK_PLANTS = 85;
    public static final int TILE_SAND_ROCK_PLANTS_GRASS_TOP = 86;

    public static final int TILE_SPROUT_SINGLE = 87;
    public static final int TILE_SPROUT_TRIPLE = 88;

    public static final int TILE_ROAD_V_CRACKED_A = 89;
    public static final int TILE_ROAD_H_CRACKED_A = 90;
    public static final int TILE_ROAD_V_CRACKED_B = 91;
    public static final int TILE_ROAD_H_CRACKED_B = 92;

    public static final int TILE_PAD_EXIT_UP_RIGHT = 93;
    public static final int TILE_PAD_EXIT_UP_LEFT = 94;
    public static final int TILE_ROAD_V_BULGE = 95;
    public static final int TILE_ROAD_H_BULGE = 96;

    public static final HashMap<Integer, Region> REGIONS = new HashMap<>();
    private static final int[] SRC_X = new int[SHEET_COLUMNS * SHEET_ROWS + 1];
    private static final int[] SRC_Y = new int[SHEET_COLUMNS * SHEET_ROWS + 1];
    public static final BufferedImage SPRITESHEET;

    static {
        for (int tile = 1; tile <= SHEET_COLUMNS * SHEET_ROWS; tile++) {
            final var index = tile - 1;
            REGIONS.put(tile, new Region(
                    (index % SHEET_COLUMNS) * TILE_SIZE,
                    (index / SHEET_COLUMNS) * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE));
        }

        for (int t = 1; t < SRC_X.length; t++) {
            SRC_X[t] = ((t - 1) % SHEET_COLUMNS) * TILE_SIZE;
            SRC_Y[t] = ((t - 1) / SHEET_COLUMNS) * TILE_SIZE;
        }

        SPRITESHEET = AssetLoader.loadTexture(SHEET_PATH);
    }

    public static Region region(int tile) {
        return REGIONS.get(tile);
    }

    public static void draw(Graphics2D g, BufferedImage sheet, int tile, int dx, int dy) {
        final var sx = SRC_X[tile];
        final var sy = SRC_Y[tile];

        g.drawImage(sheet, dx, dy, dx + TILE_SIZE, dy + TILE_SIZE, sx, sy, sx + TILE_SIZE, sy + TILE_SIZE, null);
    }
}

record Tile(double x, double y, TileSet.Region region, int code) {
    public void render(Graphics2D g) {
        TileSet.draw(g, TileSet.SPRITESHEET, code, (int) x, (int) y);
    }
}

public class Map {
    private final ArrayList<Tile> tiles;

    public Map(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    public static Map fromFile(String path) {
        final var tiles = new ArrayList<Tile>();
        final var stream = AssetLoader.getResourceStream(path);

        double x = 0.0;
        double y = 0.0;
        try (final var reader = new BufferedReader(new InputStreamReader(stream))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                final var tile_codes = line.split("\\s+");

                for (final var tile_code : tile_codes) {
                    if (!tile_code.isEmpty()) {
                        final var code = Integer.parseInt(tile_code);
                        // sentinel tile
                        if (code != 0) {
                            final var region = TileSet.region(code);
                            final var tile = new Tile(x, y, region, code);

                            tiles.add(tile);
                        }
                        x += TileSet.TILE_SIZE;
                    }
                }

                x = 0.0;
                y += TileSet.TILE_SIZE;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load \"" + path + "\"", e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Illegal tile charater", e);
        }

        return new Map(tiles);
    }

    public Map addLayer(String path) {
        final var newMap = Map.fromFile(path);

        tiles.addAll(newMap.tiles);

        return this;
    }

    public void render(Graphics2D g) {
        for (final var tile : tiles) {
            tile.render(g);
        }
    }
}
