package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public class Terrain implements Disposable {
    private final int TILE_WIDTH = 16;
    private final int TILE_HEIGHT = 16;

    private final int width;
    private final int height;
    private final float[][] heightMap;
    private final TiledMap tileMap = new TiledMap();
    private OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(tileMap);
    private final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/tilesets/decor_16x16.png"));
    private final TextureRegion[][] splitTiles = TextureRegion.split(spriteSheet, TILE_WIDTH, TILE_HEIGHT);
    // The permutations used for the Perlin noise
    private final int[] p = new int[512];

    public Terrain(int width, int height) {
        this.width = width;
        this.height = height;
        this.heightMap = new float[width][height];

        initPerms();
        generateHeightMap();
    }

    private void initPerms() {
        int[] basePerm = new int[256];
        for (int i = 0; i < 256; i++) {
            basePerm[i] = i;
        }

        for (int i = 255; i > 0; i--) {
            int random = MathUtils.random(basePerm[i]);
            int temp = basePerm[i];
            basePerm[i] = basePerm[temp];
            basePerm[random] = temp;
        }

        // Duplicate the array to avoid overflow issues
        // when xi and yi = 255. Then, accessing at xi + 1 or yi + 1 is an overflow.
        for (int i = 0; i < 256; i++) {
            p[i] = basePerm[i];
            p[i + 256] = i;
        }
    }

    private void generateHeightMap() {
        TiledMapTileLayer terrainLayer = new TiledMapTileLayer(width, height, TILE_WIDTH, TILE_HEIGHT);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float noiseValue = perlinNoise(x * 0.1f, y * 0.1f);
                System.out.println(noiseValue);
                heightMap[x][y] = noiseValue;

                int textureY = (int) (noiseValue * (splitTiles.length - 1));
                int textureX = (int) (noiseValue * (splitTiles[0].length - 1));

                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(new StaticTiledMapTile(splitTiles[textureY][textureX]));
                terrainLayer.setCell(x, y, cell);
            }
        }

        tileMap.getLayers().add(terrainLayer);
    }

    public void draw(Camera camera) {
        renderer.setView((OrthographicCamera) camera);
        renderer.render();
    }

    /**
     * Fade function as defined by Ken Perlin.
     * 6t^5 - 15t^4 + 10t^3
     */
    private float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    /**
     * @param hash The hash generated using the permutations array
     * @param x The x coordinate
     * @param y The y coordinate
     * @return a "random" vector from the following 12 vectors:
     * (1,1,0),(-1,1,0),(1,-1,0),(-1,-1,0),
     * (1,0,1),(-1,0,1),(1,0,-1),(-1,0,-1),
     * (0,1,1),(0,-1,1),(0,1,-1),(0,-1,-1)
     */
    private float grad(int hash, float x, float y) {
        // Take the hashed value and take the first 3 bits of it
        // (h id in the range [0, 7])
        int h = hash & 0b111;
        float u = h < 4 ? x : y;
        float v = h < 4 ? y : x;

        // Use the last 2 bits to decide if u and v are positive or negative. Then return their addition.
        if ((h & 1) != 0) {
            u = -u;
        }
        if ((h & 2) != 0) {
            v = -v;
        }

        return u + v;
    }

    /**
     * Perlin noise generation function.
     * Made with reference to the <a href="https://adrianb.io/2014/08/09/perlinnoise.html">Understanding Perlin Noise</a>) article.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The perlin noise value between 0 and 1
     */
    public float perlinNoise(float x, float y) {
        // Step 1:
        // Divide x, y coordinates into unit cells.

        // These represent the unit square the out coordinate is in.
        // We bind these in the range [0, 255] to avoid overflow error later.
        int xi = MathUtils.floor(x) & 255;
        int yi = MathUtils.floor(y) & 255;
        /*
        & 255 is actually equivalent to % 256
        & 255 ensures that only the lower 8 bits of the number are kept
        % 256 returns the remainder when a number is divided by 256.

        Example:
        259 = 100000011     (binary representation of 259)
      & 255 =  11111111     (binary representation of 255)
        -----------------
            =  00000011     (binary representation of 3)
         */

        // obtaining fractional part of the coordinates to get varied results
        x = x - MathUtils.floorPositive(x);
        y = y - MathUtils.floorPositive(y);

        // Compute fade curves for x, y
        float u = fade(x);
        float v = fade(y);

        /*
        The Perlin Noise hash function is used to get a unique value for every coordinate input.
        A hash function, as defined by wikipedia, is:
        …any function that can be used to map data of arbitrary size to data of fixed size,
        with slight differences in input data producing very big differences in output data.
         */
        // Hash coordinates of the corners
        int topLeft     = p[p[xi] + yi];
        int topRight    = p[p[xi + 1] + yi];
        int bottomLeft  = p[p[xi] + (yi + 1)];
        int bottomRight = p[p[xi + 1] + (yi + 1)];

        // Adding blended results from corners
        float res = MathUtils.lerp(
            MathUtils.lerp(grad(topLeft, x, y), grad(topRight, x - 1, y), u),
            MathUtils.lerp(grad(bottomLeft, x, y - 1), grad(bottomRight, x - 1, y - 1), u),
            v
        );

        // Scale result to [0,1]
        return (res + 1) / 2;
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}

