package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.Arrays;

public class Chunk {
    private final static int TILE_SIZE = 16;
    private final static Texture spriteSheet = new Texture(Gdx.files.internal("sprites/tilesets/decor_16x16.png"));

    public static final int CHUNK_SIZE = 100;
    private final int[][] textureMap = new int[CHUNK_SIZE][CHUNK_SIZE];

    public Chunk(int chunkX, int chunkY) {

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                // TODO: figure out why multiply by 0.1f
                float noiseValue = PerlinNoise.perlinNoise(x * 0.1f, y * 0.1f);
                System.out.println(noiseValue);

                int textureY = (int) (noiseValue * (splitTiles.length - 1));
                int textureX = (int) (noiseValue * (splitTiles[0].length - 1));


            }
        }

        System.out.println(Arrays.deepToString(textureMap));
    }
}
