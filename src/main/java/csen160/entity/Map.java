package csen160.entity;

import csen160.core.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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

    private static final boolean[] OBJECT_BLOCKS = new boolean[SHEET_COLUMNS * SHEET_ROWS + 1];

    private static void markBlocking(int from, int to) {
        for (int t = from; t <= to; t++) {
            OBJECT_BLOCKS[t] = true;
        }
    }

    public static boolean objectBlocks(int tile) {
        return tile != 0 && OBJECT_BLOCKS[tile];
    }

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

        OBJECT_BLOCKS[TILE_ROCK_SMALL] = true;
        OBJECT_BLOCKS[TILE_ROCK_LARGE] = true;
        OBJECT_BLOCKS[TILE_ROCK_MEDIUM] = true;
        OBJECT_BLOCKS[TILE_ROCK_MOSS_SMALL] = true;
        OBJECT_BLOCKS[TILE_ROCK_MOSS_LARGE] = true;
        OBJECT_BLOCKS[TILE_ROCK_MOSS_MEDIUM] = true;
        markBlocking(13, 16);
        markBlocking(29, 32);
        markBlocking(45, 48);
        markBlocking(61, 64);
        markBlocking(77, 80);
        markBlocking(89, 96);
    }

    public static Region region(int tile) {
        return REGIONS.get(tile);
    }

    public static boolean groundBlocks(int tile) {
        return tile != TILE_WATER;
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
    private static final double TS = TileSet.TILE_SIZE;
    private static final double EPS = 1e-6;

    private final ArrayList<Tile> tiles = new ArrayList<>();
    private boolean[][] solid = new boolean[0][0];
    private int rows = 0;
    private int cols = 0;

    public Map(ArrayList<Tile> tiles) {
        for (final var tile : tiles) {
            add(tile, false);
        }
    }

    public static Map fromFile(String path) {
        final var map = new Map(new ArrayList<>());
        map.load(path, false);

        return map;
    }

    public Map addLayer(String path) {
        load(path, true);

        return this;
    }

    private void load(String path, boolean objectLayer) {
        final var stream = AssetLoader.getResourceStream(path);

        int row = 0;
        try (final var reader = new BufferedReader(new InputStreamReader(stream))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                int col = 0;
                for (final var token : line.trim().split("\\s+")) {
                    if (token.isEmpty()) {
                        continue;
                    }
                    final var code = Integer.parseInt(token);
                    // 0 is the "empty" sentinel
                    if (code != 0) {
                        add(new Tile(col * TileSet.TILE_SIZE, row * TileSet.TILE_SIZE, TileSet.region(code), code),
                                objectLayer);
                    }
                    col++;
                }
                row++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load \"" + path + "\"", e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Illegal tile charater", e);
        }
    }

    private void add(Tile tile, boolean objectLayer) {
        tiles.add(tile);

        final var row = (int) (tile.y() / TileSet.TILE_SIZE);
        final var col = (int) (tile.x() / TileSet.TILE_SIZE);
        // size the grid from every tile (water included) so open water inside the map
        // is not "outside"
        grow(row + 1, col + 1);

        final var blocks = objectLayer ? TileSet.objectBlocks(tile.code()) : TileSet.groundBlocks(tile.code());
        if (blocks) {
            solid[row][col] = true;
        }
    }

    private void grow(int minRows, int minCols) {
        if (minRows <= rows && minCols <= cols) {
            return;
        }
        final var newRows = Math.max(rows, minRows);
        final var newCols = Math.max(cols, minCols);
        final var grown = new boolean[newRows][newCols];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(solid[r], 0, grown[r], 0, cols);
        }
        solid = grown;
        rows = newRows;
        cols = newCols;
    }

    private boolean isSolid(int row, int col) {
        return row < 0 || col < 0 || row >= rows || col >= cols || solid[row][col];
    }

    private static int lo(double v) {
        return (int) Math.floor((v + EPS) / TS);
    }

    private static int hi(double v) {
        return (int) Math.ceil((v - EPS) / TS) - 1;
    }

    private boolean columnSolid(int col, int row0, int row1) {
        for (int r = row0; r <= row1; r++) {
            if (isSolid(r, col)) {
                return true;
            }
        }
        return false;
    }

    private boolean rowSolid(int row, int col0, int col1) {
        for (int c = col0; c <= col1; c++) {
            if (isSolid(row, c)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBlocked(double cx, double cy, double w, double h) {
        final var c0 = lo(cx - w / 2);
        final var c1 = hi(cx + w / 2);
        final var r0 = lo(cy - h / 2);
        final var r1 = hi(cy + h / 2);
        for (int r = r0; r <= r1; r++) {
            for (int c = c0; c <= c1; c++) {
                if (isSolid(r, c)) {
                    return true;
                }
            }
        }
        return false;
    }

    public double moveX(double cx, double cy, double w, double h, double dx) {
        if (dx == 0.0) {
            return cx;
        }
        final var hw = w / 2;
        final var r0 = lo(cy - h / 2);
        final var r1 = hi(cy + h / 2);

        if (dx > 0) {
            final var edge = cx + hw;
            final var last = hi(edge + dx);
            for (int c = lo(edge); c <= last; c++) {
                if (columnSolid(c, r0, r1)) {
                    return c * TS - hw;
                }
            }
        } else {
            final var edge = cx - hw;
            final var last = lo(edge + dx);
            for (int c = hi(edge); c >= last; c--) {
                if (columnSolid(c, r0, r1)) {
                    return (c + 1) * TS + hw;
                }
            }
        }
        return cx + dx;
    }

    public double moveY(double cx, double cy, double w, double h, double dy) {
        if (dy == 0.0) {
            return cy;
        }
        final var hh = h / 2;
        final var c0 = lo(cx - w / 2);
        final var c1 = hi(cx + w / 2);

        if (dy > 0) {
            final var edge = cy + hh;
            final var last = hi(edge + dy);
            for (int r = lo(edge); r <= last; r++) {
                if (rowSolid(r, c0, c1)) {
                    return r * TS - hh;
                }
            }
        } else {
            final var edge = cy - hh;
            final var last = lo(edge + dy);
            for (int r = hi(edge); r >= last; r--) {
                if (rowSolid(r, c0, c1)) {
                    return (r + 1) * TS + hh;
                }
            }
        }
        return cy + dy;
    }

    public void render(Graphics2D g) {
        for (final var tile : tiles) {
            final var x = tile.x();
            final var y = tile.y();
            TileSet.draw(g, TileSet.SPRITESHEET, TileSet.TILE_WATER, (int) x, (int) y);
        }

        for (final var tile : tiles) {
            tile.render(g);
        }
    }
}
