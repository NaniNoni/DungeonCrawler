package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
public class Terrain extends GameObject{
    private int width;
    private int height;
    private float heightMap[][];
    private Texture carpettexture;
    private Texture flooringtexture;
    private Texture woodentexture;

    public Terrain(Vector2 position, int width, int height, float colliderRadius) {
        super(position, colliderRadius);
        this.width = width;
        this.height = height;
        this.heightMap = new float[width][height];

        carpettexture = new Texture("sprites/tilesets/floors/carpet.png");
        flooringtexture = new Texture("sprites/tilesets/floors/flooring.png");
        woodentexture = new Texture("sprites/tilesets/floors/wooden.png");

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

    private float PerlinNoise(float x, float y) {
        // TODO: use an actual implementation for better results
        return (float)Math.random();
    }

    public void dispose() {
        carpettexture.dispose();
        woodentexture.dispose();
        flooringtexture.dispose();
    }
}
