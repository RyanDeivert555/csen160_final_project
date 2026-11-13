package csen160.entity;

import csen160.core.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines tile ID constants, spritesheet slicing coordinates, collision
 * properties,
 * and rendering helpers for the pirate-themed tile map.
 */
class TileSet {

    /**
     * Represents a rectangular pixel sub-region within the tile spritesheet.
     *
     * @param x the top-left x-coordinate in pixels
     * @param y the top-left y-coordinate in pixels
     * @param w the width of the region in pixels
     * @param h the height of the region in pixels
     */
    public record Region(int x, int y, int w, int h) {
    }

    /** Classpath resource path to the master tile spritesheet image. */
    public static final String SHEET_PATH = "/PiratePack/Tilesheet/tiles_sheet.png";

    /** Width and height of a single square tile in pixels. */
    public static final int TILE_SIZE = 64;

    /** Number of tile columns in the spritesheet grid. */
    public static final int SHEET_COLUMNS = 16;

    /** Number of tile rows in the spritesheet grid. */
    public static final int SHEET_ROWS = 6;

    /** Tile ID for top-left sand corner. */
    public static final int TILE_SAND_CORNER_TL = 1;
    /** Tile ID for top sand edge. */
    public static final int TILE_SAND_EDGE_TOP = 2;
    /** Tile ID for top-right sand corner. */
    public static final int TILE_SAND_CORNER_TR = 3;
    /** Tile ID for plain sand with blob variant A. */
    public static final int TILE_SAND_PLAIN_BLOB_A = 4;
    /** Tile ID for plain solid sand. */
    public static final int TILE_SAND_PLAIN = 5;
    /** Tile ID for top-left sand-to-grass corner. */
    public static final int TILE_SAND_CORNER_TL_GRASS = 6;
    /** Tile ID for top sand edge transitioning to grass at the bottom. */
    public static final int TILE_SAND_EDGE_TOP_GRASS_BOTTOM = 7;
    /** Tile ID for top sand edge with bumpy grass transition. */
    public static final int TILE_SAND_EDGE_TOP_GRASS_BUMP = 8;
    /** Tile ID for top-right sand-to-grass corner. */
    public static final int TILE_SAND_CORNER_TR_GRASS = 9;

    /** Tile ID for top-left overlay corner. */
    public static final int TILE_OVERLAY_CORNER_TL = 10;
    /** Tile ID for top overlay edge. */
    public static final int TILE_OVERLAY_EDGE_TOP = 11;
    /** Tile ID for top-right overlay corner. */
    public static final int TILE_OVERLAY_CORNER_TR = 12;

    /** Tile ID for a solid platform pad. */
    public static final int TILE_PAD = 13;
    /** Tile ID for a platform pad with a hatch. */
    public static final int TILE_PAD_HATCH = 14;
    /** Tile ID for a vertical road segment. */
    public static final int TILE_ROAD_V = 15;
    /** Tile ID for a horizontal road segment. */
    public static final int TILE_ROAD_H = 16;

    /** Tile ID for left sand edge. */
    public static final int TILE_SAND_EDGE_LEFT = 17;
    /** Tile ID for dark sand fill. */
    public static final int TILE_SAND_DARK = 18;
    /** Tile ID for right sand edge. */
    public static final int TILE_SAND_EDGE_RIGHT = 19;
    /** Tile ID for plain sand with blob variant B. */
    public static final int TILE_SAND_PLAIN_BLOB_B = 20;
    /** Tile ID for plain sand with blob variant C. */
    public static final int TILE_SAND_PLAIN_BLOB_C = 21;
    /** Tile ID for left sand edge transitioning to grass on the right. */
    public static final int TILE_SAND_EDGE_LEFT_GRASS_RIGHT = 22;

    /** Tile ID for plain solid grass. */
    public static final int TILE_GRASS = 23;
    /** Tile ID for grass with scattered sand specks. */
    public static final int TILE_GRASS_SAND_SPECKS = 24;
    /** Tile ID for a sand strip with grass on the left. */
    public static final int TILE_SAND_STRIP_GRASS_LEFT = 25;

    /** Tile ID for left overlay edge. */
    public static final int TILE_OVERLAY_EDGE_LEFT = 26;
    /** Tile ID for full overlay fill. */
    public static final int TILE_OVERLAY_FULL = 27;
    /** Tile ID for right overlay edge. */
    public static final int TILE_OVERLAY_EDGE_RIGHT = 28;

    /** Tile ID for vertical road connected to a pad. */
    public static final int TILE_ROAD_V_PAD = 29;
    /** Tile ID for horizontal road connected to a pad. */
    public static final int TILE_ROAD_H_PAD = 30;
    /** Tile ID for vertical road with a right-facing cannon emplacement. */
    public static final int TILE_ROAD_V_CANNON_RIGHT = 31;
    /** Tile ID for vertical road with a left-facing cannon emplacement. */
    public static final int TILE_ROAD_V_CANNON_LEFT = 32;

    /** Tile ID for bottom-left sand corner. */
    public static final int TILE_SAND_CORNER_BL = 33;
    /** Tile ID for bottom sand edge. */
    public static final int TILE_SAND_EDGE_BOTTOM = 34;
    /** Tile ID for bottom-right sand corner. */
    public static final int TILE_SAND_CORNER_BR = 35;
    /** Tile ID for top-left grass-to-sand corner. */
    public static final int TILE_GRASS_CORNER_TL_SAND = 36;
    /** Tile ID for top-right grass-to-sand corner. */
    public static final int TILE_GRASS_CORNER_TR_SAND = 37;
    /** Tile ID for left sand edge with bumpy grass transition on the right. */
    public static final int TILE_SAND_EDGE_LEFT_GRASS_RIGHT_BUMPY = 38;
    /** Tile ID for speckled grass variant A. */
    public static final int TILE_GRASS_SPECKLE_A = 39;
    /** Tile ID for speckled grass variant B. */
    public static final int TILE_GRASS_SPECKLE_B = 40;
    /** Tile ID for grass on the left with right sand edge. */
    public static final int TILE_GRASS_LEFT_SAND_EDGE_RIGHT = 41;

    /** Tile ID for bottom-left overlay corner. */
    public static final int TILE_OVERLAY_CORNER_BL = 42;
    /** Tile ID for bottom overlay edge. */
    public static final int TILE_OVERLAY_EDGE_BOTTOM = 43;
    /** Tile ID for bottom-right overlay corner. */
    public static final int TILE_OVERLAY_CORNER_BR = 44;

    /** Tile ID for platform pad with downward exit. */
    public static final int TILE_PAD_EXIT_DOWN = 45;
    /** Tile ID for platform pad with rightward exit. */
    public static final int TILE_PAD_EXIT_RIGHT = 46;
    /** Tile ID for horizontal road with an upward-facing cannon emplacement. */
    public static final int TILE_ROAD_H_CANNON_UP = 47;
    /** Tile ID for horizontal road with a downward-facing cannon emplacement. */
    public static final int TILE_ROAD_H_CANNON_DOWN = 48;

    /** Tile ID for a small rock obstacle. */
    public static final int TILE_ROCK_SMALL = 49;
    /** Tile ID for a large rock obstacle. */
    public static final int TILE_ROCK_LARGE = 50;
    /** Tile ID for a medium rock obstacle. */
    public static final int TILE_ROCK_MEDIUM = 51;

    /** Tile ID for sand flanked by grass with a top blob. */
    public static final int TILE_SAND_GRASS_SIDES_TOP_BLOB = 52;
    /** Tile ID for sand flanked by grass on the sides and top. */
    public static final int TILE_SAND_GRASS_SIDES_TOP = 53;
    /** Tile ID for bottom-left sand-to-grass corner. */
    public static final int TILE_SAND_CORNER_BL_GRASS = 54;
    /** Tile ID for bottom sand edge transitioning to grass at the top. */
    public static final int TILE_SAND_EDGE_BOTTOM_GRASS_TOP = 55;
    /** Tile ID for bottom sand edge with bumpy grass transition. */
    public static final int TILE_SAND_EDGE_BOTTOM_GRASS_BUMP = 56;
    /** Tile ID for bottom-right sand-to-grass corner. */
    public static final int TILE_SAND_CORNER_BR_GRASS = 57;

    /** Tile ID for bottom overlay blob variant A. */
    public static final int TILE_OVERLAY_BLOB_BOTTOM_A = 58;
    /** Tile ID for bottom overlay blob variant B. */
    public static final int TILE_OVERLAY_BLOB_BOTTOM_B = 59;

    /** Tile ID for a vertical bridge segment. */
    public static final int TILE_BRIDGE_V = 60;
    /** Tile ID for platform pad with upward exit. */
    public static final int TILE_PAD_EXIT_UP = 61;
    /** Tile ID for platform pad with leftward exit. */
    public static final int TILE_PAD_EXIT_LEFT = 62;
    /** Tile ID for top road end-cap. */
    public static final int TILE_ROAD_CAP_TOP = 63;
    /** Tile ID for left road end-cap. */
    public static final int TILE_ROAD_CAP_LEFT = 64;

    /** Tile ID for a small mossy rock obstacle. */
    public static final int TILE_ROCK_MOSS_SMALL = 65;
    /** Tile ID for a large mossy rock obstacle. */
    public static final int TILE_ROCK_MOSS_LARGE = 66;
    /** Tile ID for a medium mossy rock obstacle. */
    public static final int TILE_ROCK_MOSS_MEDIUM = 67;

    /** Tile ID for sand with light specks. */
    public static final int TILE_SAND_SPECKS_LIGHT = 68;
    /** Tile ID for sand with dark specks. */
    public static final int TILE_SAND_SPECKS_DARK = 69;
    /** Tile ID for a small sprout plant decoration. */
    public static final int TILE_PLANT_SPROUT = 70;
    /** Tile ID for a star-shaped plant decoration. */
    public static final int TILE_PLANT_STAR = 71;
    /** Tile ID for a bush decoration. */
    public static final int TILE_PLANT_BUSH = 72;

    /** Tile ID for navigable open water. */
    public static final int TILE_WATER = 73;
    /** Tile ID for top overlay blob variant A. */
    public static final int TILE_OVERLAY_BLOB_TOP_A = 74;
    /** Tile ID for top overlay blob variant B. */
    public static final int TILE_OVERLAY_BLOB_TOP_B = 75;

    /** Tile ID for a horizontal bridge segment. */
    public static final int TILE_BRIDGE_H = 76;
    /** Tile ID for platform pad with bottom-right exit. */
    public static final int TILE_PAD_EXIT_RIGHT_DOWN = 77;
    /** Tile ID for platform pad with bottom-left exit. */
    public static final int TILE_PAD_EXIT_LEFT_DOWN = 78;
    /** Tile ID for bottom road end-cap. */
    public static final int TILE_ROAD_CAP_BOTTOM = 79;
    /** Tile ID for right road end-cap. */
    public static final int TILE_ROAD_CAP_RIGHT = 80;

    /** Tile ID for a beached boat on sand. */
    public static final int TILE_SAND_BOAT = 81;
    /** Tile ID for a beached boat on sand with grass at the top. */
    public static final int TILE_SAND_BOAT_GRASS_TOP = 82;
    /** Tile ID for cannon debris on sand. */
    public static final int TILE_SAND_CANNON_DEBRIS = 83;
    /** Tile ID for cannon debris on sand with grass at the top. */
    public static final int TILE_SAND_CANNON_DEBRIS_GRASS_TOP = 84;
    /** Tile ID for rocks and plants on sand. */
    public static final int TILE_SAND_ROCK_PLANTS = 85;
    /** Tile ID for rocks and plants on sand with grass at the top. */
    public static final int TILE_SAND_ROCK_PLANTS_GRASS_TOP = 86;

    /** Tile ID for a single sprout decoration. */
    public static final int TILE_SPROUT_SINGLE = 87;
    /** Tile ID for a triple sprout decoration. */
    public static final int TILE_SPROUT_TRIPLE = 88;

    /** Tile ID for cracked vertical road variant A. */
    public static final int TILE_ROAD_V_CRACKED_A = 89;
    /** Tile ID for cracked horizontal road variant A. */
    public static final int TILE_ROAD_H_CRACKED_A = 90;
    /** Tile ID for cracked vertical road variant B. */
    public static final int TILE_ROAD_V_CRACKED_B = 91;
    /** Tile ID for cracked horizontal road variant B. */
    public static final int TILE_ROAD_H_CRACKED_B = 92;

    /** Tile ID for platform pad with top-right exit. */
    public static final int TILE_PAD_EXIT_UP_RIGHT = 93;
    /** Tile ID for platform pad with top-left exit. */
    public static final int TILE_PAD_EXIT_UP_LEFT = 94;
    /** Tile ID for vertical road with a widened bulge. */
    public static final int TILE_ROAD_V_BULGE = 95;
    /** Tile ID for horizontal road with a widened bulge. */
    public static final int TILE_ROAD_H_BULGE = 96;

    /**
     * Lookup map associating 1-based tile IDs with their {@link Region} bounds in
     * the spritesheet.
     */
    public static final HashMap<Integer, Region> REGIONS = new HashMap<>();

    /**
     * Precomputed source x-coordinates in the spritesheet indexed by 1-based tile
     * ID.
     */
    private static final int[] SRC_X = new int[SHEET_COLUMNS * SHEET_ROWS + 1];

    /**
     * Precomputed source y-coordinates in the spritesheet indexed by 1-based tile
     * ID.
     */
    private static final int[] SRC_Y = new int[SHEET_COLUMNS * SHEET_ROWS + 1];

    /** Loaded master spritesheet image containing all tile graphics. */
    public static final BufferedImage SPRITESHEET;

    /**
     * Lookup table indicating whether a 1-based tile ID acts as a solid obstacle on
     * an object layer.
     */
    private static final boolean[] OBJECT_BLOCKS = new boolean[SHEET_COLUMNS * SHEET_ROWS + 1];

    /**
     * Marks an inclusive range of tile IDs as solid obstacles on object layers.
     *
     * @param from the starting tile ID (inclusive)
     * @param to   the ending tile ID (inclusive)
     */
    private static void markBlocking(int from, int to) {
        for (int t = from; t <= to; t++) {
            OBJECT_BLOCKS[t] = true;
        }
    }

    /**
     * Determines whether the given object-layer tile ID blocks entity movement.
     *
     * @param tile the 1-based tile ID (or {@code 0} for empty)
     * @return {@code true} if the tile is non-empty and marked as blocking;
     *         {@code false} otherwise
     */
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

    /**
     * Retrieves the spritesheet {@link Region} corresponding to the specified tile
     * ID.
     *
     * @param tile the 1-based tile ID
     * @return the {@link Region} for {@code tile}, or {@code null} if invalid
     */
    public static Region region(int tile) {
        return REGIONS.get(tile);
    }

    /**
     * Determines whether a ground-layer tile ID blocks naval movement.
     * Only open water ({@link #TILE_WATER}) is navigable; all land and shoreline
     * tiles block.
     *
     * @param tile the 1-based ground tile ID
     * @return {@code true} if the tile is not {@link #TILE_WATER}; {@code false}
     *         otherwise
     */
    public static boolean groundBlocks(int tile) {
        return tile != TILE_WATER;
    }

    /**
     * Draws a single tile from the specified spritesheet onto the target graphics
     * context.
     *
     * @param g     the {@link Graphics2D} context on which to draw
     * @param sheet the spritesheet {@link BufferedImage}
     * @param tile  the 1-based tile ID to render
     * @param dx    the destination x-coordinate in world pixels
     * @param dy    the destination y-coordinate in world pixels
     */
    public static void draw(Graphics2D g, BufferedImage sheet, int tile, int dx, int dy) {
        final var sx = SRC_X[tile];
        final var sy = SRC_Y[tile];

        g.drawImage(sheet, dx, dy, dx + TILE_SIZE, dy + TILE_SIZE, sx, sy, sx + TILE_SIZE, sy + TILE_SIZE, null);
    }
}

/**
 * Represents a single placed tile instance on the map with world coordinates,
 * spritesheet source region, and tile type code.
 *
 * @param x      the top-left x-coordinate of the tile in world pixels
 * @param y      the top-left y-coordinate of the tile in world pixels
 * @param region the spritesheet {@link TileSet.Region} for this tile
 * @param code   the 1-based tile ID
 */
record Tile(double x, double y, TileSet.Region region, int code) {

    /**
     * Renders this tile at its world coordinates onto the provided graphics
     * context.
     *
     * @param g the {@link Graphics2D} context on which to draw the tile
     */
    public void render(Graphics2D g) {
        TileSet.draw(g, TileSet.SPRITESHEET, code, (int) x, (int) y);
    }
}

/**
 * Represents a multi-layered 2D tile map that manages tile rendering and
 * swept axis-aligned bounding-box (AABB) collision resolution against solid
 * terrain and objects.
 */
public class Map {

    /**
     * Tile dimension in world pixels cached as a {@code double} for collision math.
     */
    private static final double TS = TileSet.TILE_SIZE;

    /** Small epsilon tolerance used to avoid false edge-touching tile overlaps. */
    private static final double EPS = 1e-6;

    /** Ordered list of all non-empty tiles across ground and object layers. */
    private final ArrayList<Tile> tiles = new ArrayList<>();

    /**
     * 2D grid indexed by {@code [row][col]} indicating whether each tile cell is
     * impassable.
     */
    private boolean[][] solid = new boolean[0][0];

    /** Current number of tile rows in the map grid. */
    private int rows = 0;

    /** Current number of tile columns in the map grid. */
    private int cols = 0;

    /**
     * Constructs a {@code Map} populated with the given initial list of
     * ground-layer {@link Tile}s.
     *
     * @param tiles the list of tiles to add to the map
     */
    public Map(ArrayList<Tile> tiles) {
        for (final var tile : tiles) {
            add(tile, false);
        }
    }

    /**
     * Creates and loads a new {@code Map} from a whitespace-delimited ground-layer
     * tile file.
     *
     * @param path the classpath resource path to the ground map file
     * @return a newly initialized {@code Map} containing the loaded ground layer
     */
    public static Map fromFile(String path) {
        final var map = new Map(new ArrayList<>());
        map.load(path, false);

        return map;
    }

    /**
     * Loads an additional object layer from a whitespace-delimited tile file onto
     * this map,
     * updating solidity flags for blocking object tiles.
     *
     * @param path the classpath resource path to the object layer file
     * @return this {@code Map} instance for method chaining
     */
    public Map addLayer(String path) {
        load(path, true);

        return this;
    }

    /**
     * Parses a grid of integer tile codes from a classpath resource and adds
     * non-zero tiles
     * to the map.
     *
     * @param path        the classpath resource path to the map layer text file
     * @param objectLayer {@code true} if parsing an object overlay layer;
     *                    {@code false} for the base ground layer
     * @throws RuntimeException if an I/O error occurs or a non-integer tile token
     *                          is encountered
     */
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

    /**
     * Appends a {@link Tile} to the render list, expands the {@link #solid} grid if
     * necessary,
     * and marks the corresponding cell solid if the tile blocks movement.
     *
     * @param tile        the {@link Tile} to insert
     * @param objectLayer {@code true} to evaluate solidity using
     *                    {@link TileSet#objectBlocks(int)};
     *                    {@code false} to use {@link TileSet#groundBlocks(int)}
     */
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

    /**
     * Expands the internal {@link #solid} collision grid to accommodate at least
     * {@code minRows} by {@code minCols} cells while preserving existing data.
     *
     * @param minRows the minimum required row count
     * @param minCols the minimum required column count
     */
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

    /**
     * Checks whether a tile cell is impassable or lies outside the map boundaries.
     *
     * @param row the zero-based tile row index
     * @param col the zero-based tile column index
     * @return {@code true} if the cell is out of bounds or marked solid;
     *         {@code false} otherwise
     */
    private boolean isSolid(int row, int col) {
        return row < 0 || col < 0 || row >= rows || col >= cols || solid[row][col];
    }

    /**
     * Computes the lowest tile grid index overlapped by a minimum bounding edge
     * coordinate.
     *
     * @param v the minimum coordinate in world pixels
     * @return the inclusive lower tile index
     */
    private static int lo(double v) {
        return (int) Math.floor((v + EPS) / TS);
    }

    /**
     * Computes the highest tile grid index overlapped by a maximum bounding edge
     * coordinate.
     *
     * @param v the maximum coordinate in world pixels
     * @return the inclusive upper tile index
     */
    private static int hi(double v) {
        return (int) Math.ceil((v - EPS) / TS) - 1;
    }

    /**
     * Checks whether any cell in column {@code col} between {@code row0} and
     * {@code row1} (inclusive) is solid.
     *
     * @param col  the tile column index to inspect
     * @param row0 the starting tile row index (inclusive)
     * @param row1 the ending tile row index (inclusive)
     * @return {@code true} if at least one cell in the span is solid; {@code false}
     *         otherwise
     */
    private boolean columnSolid(int col, int row0, int row1) {
        for (int r = row0; r <= row1; r++) {
            if (isSolid(r, col)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether any cell in row {@code row} between {@code col0} and
     * {@code col1} (inclusive) is solid.
     *
     * @param row  the tile row index to inspect
     * @param col0 the starting tile column index (inclusive)
     * @param col1 the ending tile column index (inclusive)
     * @return {@code true} if at least one cell in the span is solid; {@code false}
     *         otherwise
     */
    private boolean rowSolid(int row, int col0, int col1) {
        for (int c = col0; c <= col1; c++) {
            if (isSolid(row, c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether a centered axis-aligned bounding box overlaps any solid tile or
     * extends
     * outside the map bounds.
     *
     * @param cx the center x-coordinate in world pixels
     * @param cy the center y-coordinate in world pixels
     * @param w  the bounding box width in pixels
     * @param h  the bounding box height in pixels
     * @return {@code true} if any overlapped tile cell is solid or out of bounds;
     *         {@code false} otherwise
     */
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

    /**
     * Sweeps a centered bounding box horizontally by {@code dx} pixels, clamping
     * the resulting
     * x-coordinate flush against the nearest solid tile column if a collision
     * occurs.
     *
     * @param cx the starting center x-coordinate in world pixels
     * @param cy the center y-coordinate in world pixels
     * @param w  the bounding box width in pixels
     * @param h  the bounding box height in pixels
     * @param dx the desired horizontal displacement in pixels
     * @return the resolved center x-coordinate after horizontal collision checks
     */
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

    /**
     * Sweeps a centered bounding box vertically by {@code dy} pixels, clamping the
     * resulting
     * y-coordinate flush against the nearest solid tile row if a collision occurs.
     *
     * @param cx the center x-coordinate in world pixels
     * @param cy the starting center y-coordinate in world pixels
     * @param w  the bounding box width in pixels
     * @param h  the bounding box height in pixels
     * @param dy the desired vertical displacement in pixels
     * @return the resolved center y-coordinate after vertical collision checks
     */
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

    /**
     * Returns the total dimensions of the map in world pixels.
     *
     * @return a {@link Dimension} containing
     *         {@code (cols * TILE_SIZE, rows * TILE_SIZE)}
     */
    public Dimension getMapSize() {
        return new Dimension(cols * TileSet.TILE_SIZE, rows * TileSet.TILE_SIZE);
    }

    /**
     * Renders the map by first drawing base water tiles under all occupied
     * coordinates
     * and then drawing all ground and object {@link Tile} layers in insertion
     * order.
     *
     * @param g the {@link Graphics2D} context on which to render the map
     */
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
