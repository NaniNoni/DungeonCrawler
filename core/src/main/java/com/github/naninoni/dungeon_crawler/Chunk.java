package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Chunk {
    public final static int CHUNK_SIZE = 32;

    public final static int TILE_SIZE = 16;
    private final static Texture spriteSheet = new Texture(Gdx.files.internal("sprites/tilesets/decor_16x16.png"));
    private final static TextureRegion[][] splitSheet = TextureRegion.split(spriteSheet, TILE_SIZE, TILE_SIZE);

    // This stores the texture coordinate that maps to the value created by the noise function
    private final Vector2i[][] textureMap = new Vector2i[CHUNK_SIZE][CHUNK_SIZE];
    private final Vector2i position;

    public Chunk(Vector2i position) {
        this.position = position;

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                float globalX = position.x * CHUNK_SIZE + x;
                float globalY = position.y * CHUNK_SIZE + y;
                // Multiply by 0.1f to get decimal places. They are used in the noise algorithm.
                float noiseValue = PerlinNoise.perlinNoise(globalX * 0.1f, globalY * 0.1f);

                int tx = MathUtils.floorPositive(noiseValue * (splitSheet.length - 1));
                int ty = MathUtils.floorPositive(noiseValue * (splitSheet[0].length - 1));
                Vector2i textureCoordinates = new Vector2i(tx, ty);
                textureMap[x][y] = textureCoordinates;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                Vector2i textureCoordinates = textureMap[x][y];
                Vector2i globalPos = position.cpy().scl(Chunk.CHUNK_SIZE).add(x, y);

                batch.draw(
                    splitSheet[textureCoordinates.x][textureCoordinates.y],
                    globalPos.x * TILE_SIZE,
                    globalPos.y * TILE_SIZE
                );
            }
        }
    }
}
