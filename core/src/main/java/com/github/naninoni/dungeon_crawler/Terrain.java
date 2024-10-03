package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;
public class Terrain extends GameObject{
    private int width;
    private int height;
    private float heightMap[][];
    private Texture carpettexture;
    private Texture flooringtexture;
    private Texture woodentexture;
    private int perm[] = new int[512];

    public Terrain(Vector2 position, int width, int height, float colliderRadius) {
        super(position, colliderRadius);
        this.width = width;
        this.height = height;
        this.heightMap = new float[width][height];

        carpettexture = new Texture("sprites/tilesets/floors/carpet.png");
        flooringtexture = new Texture("sprites/tilesets/floors/flooring.png");
        woodentexture = new Texture("sprites/tilesets/floors/wooden.png");

        initPerm();
        generateHeightMap();
    }

    private void generateHeightMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Use Perlin/Simplex
                float noiseValue = PerlinNoise(x * 0.1f, y * 0.1f);  // Scaling noise
                heightMap[x][y] = noiseValue;  // Assign noise value to the height map
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Determine the texture based on the height map value
                Texture tileTexture = getTileTexture(heightMap[x][y]);

                // Render appropriate tile texture
                batch.draw(tileTexture, position.x + x * 32, position.y + y * 32, 16, 16);  // play around with the numbers
            }
        }
    }

    private Texture getTileTexture(float heightValue) {
        if (heightValue > 0.7f) {
            return flooringtexture;
        } else if (heightValue > 0.4f) {
            return woodentexture;
        } else {
            return carpettexture;
        }
    }
//perlin noise
    private float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private float linInterp(float t, float a, float b) {
        //linear Interpolation
        return a + t * (b - a);
    }

    private float grad(int hash, float x, float y) {
        // Generating pseudorandom gradient vector based on hash value
        int h = hash & 7; // Take the first 3 bits of the hash
        float u = h < 4 ? x : y;
        float v = h < 4 ? y : x;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    private void initPerm() {
        int [] p = new int[256];
        for (int i = 0; i < 256; i++) {
            p[i] = i;
    }
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 255; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }

        for (int i = 0; i < 256; i++) {
            perm[i] = p[i];
            perm[256 + i] = p[i];
        }
    }


    private float PerlinNoise(float x, float y) {
        // Find unit grid cell containing point
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;

        // obtaining fractional part of the coordinates to get varied results
        x = x - (float)Math.floor(x);
        y = y - (float)Math.floor(y);

        // Compute fade curves for x, y
        float u = fade(x);
        float v = fade(y);

        // Hash coordinates of the corners
        int A = perm[X] + Y;
        int B = perm[X + 1] + Y;

        // Adding blended results from corners
        float res = linInterp(v,
            linInterp(u, grad(perm[A], x, y), grad(perm[B], x - 1, y)),
            linInterp(u, grad(perm[A + 1], x, y - 1), grad(perm[B + 1], x - 1, y - 1))
        );

        // Scale result to [0,1]
        return (res + 1) / 2;
    }


    public void dispose() {
        carpettexture.dispose();
        woodentexture.dispose();
        flooringtexture.dispose();
    }
}

