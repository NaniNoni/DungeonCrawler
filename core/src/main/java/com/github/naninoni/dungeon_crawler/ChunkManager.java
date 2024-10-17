package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class ChunkManager {
    private final Map<Vector2i, Chunk> chunks = new HashMap<Vector2i, Chunk>();
    private Vector2i previousPlayerChunk;

    public ChunkManager() {
        updateChunks();
    }

    /**
     * Runs every frame.
     */
    public void update() {
        Vector2 playerPos = Player.getInstance().position;
        int playerChunkX = (int) (playerPos.x / Chunk.CHUNK_SIZE);
        int playerChunkY = (int) (playerPos.y / Chunk.CHUNK_SIZE);
        Vector2i currentPlayerChunk = new Vector2i(playerChunkX, playerChunkY);

        System.out.println(currentPlayerChunk);

        if (!currentPlayerChunk.equals(previousPlayerChunk)) {
            updateChunks();
            previousPlayerChunk = currentPlayerChunk;
        }

        // Figure out if we should load new chunks
    }

    public void updateChunks() {
        System.out.println("Regenerating chunks!");
        Vector2 playerPos = Player.getInstance().position;
        int playerChunkX = (int) (playerPos.x / Chunk.CHUNK_SIZE);
        int playerChunkY = (int) (playerPos.y / Chunk.CHUNK_SIZE);

        /*
          The radius of chunks to load around the player.
          e.g. 1 means a 3x3 grid of chunks will be loaded.
         */
        int CHUNK_LOAD_RADIUS = 1;

        // Load/unload needed chunks
        for (int dx = -CHUNK_LOAD_RADIUS; dx <= CHUNK_LOAD_RADIUS; dx++) {
            for (int dy = -CHUNK_LOAD_RADIUS; dy <= CHUNK_LOAD_RADIUS; dy++) {
                int chunkX = playerChunkX + dx;
                int chunkY = playerChunkY + dy;
                Vector2i chunkPos = new Vector2i(chunkX, chunkY);

                // Chunk already loaded
                if (chunks.containsKey(chunkPos)) {
                    return;
                }

                Chunk chunk = new Chunk(chunkPos);
                chunks.put(chunkPos, chunk);

                // TODO: unload old chunks LOL
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Chunk chunk : chunks.values()) {
            chunk.draw(batch);
        }
    }
}
