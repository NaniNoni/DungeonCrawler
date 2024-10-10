package com.github.naninoni.dungeon_crawler;

public class Chunk {
    public static final int CHUNK_SIZE = 100;
    private final float[][] heightMap = new float[CHUNK_SIZE][CHUNK_SIZE];

    public Chunk(int chunkX, int chunkY) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                // TODO: figure out why multiply by 0.1f
                heightMap[x][y] = PerlinNoise.perlinNoise(x * 0.1f, y * 0.1f);
            }
        }
    }
}
