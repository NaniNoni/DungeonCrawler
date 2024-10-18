package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

public class ChunkManager {
    private final Map<Vector2i, Chunk> chunks = new HashMap<>();
    private Vector2i previousPlayerChunk;

    public ChunkManager() {
        updateChunks();
    }

    /**
     * Runs every frame.
     */
    public void update() {
        Vector2i currentPlayerChunk = Player.getInstance().getChunk();

        System.out.println(currentPlayerChunk);

        if (!currentPlayerChunk.equals(previousPlayerChunk)) {
            updateChunks();
            previousPlayerChunk = currentPlayerChunk;
        }
    }

    public void updateChunks() {
        /*
          The radius of chunks to load around the player.
          e.g. 1 means a 3x3 grid of chunks will be loaded.
         */
        int CHUNK_LOAD_RADIUS = 1;

        for (int dx = -CHUNK_LOAD_RADIUS; dx <= CHUNK_LOAD_RADIUS; dx++) {
            for (int dy = -CHUNK_LOAD_RADIUS; dy <= CHUNK_LOAD_RADIUS; dy++) {
                Vector2i chunkPos = Player.getInstance().getChunk().cpy().add(dx, dy);

                // Chunk already loaded
                if (chunks.containsKey(chunkPos)) {
                    continue;
                }

                System.out.println("Generating chunk at " + chunkPos);
                Chunk chunk = new Chunk(chunkPos);
                chunks.put(chunkPos, chunk);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Chunk chunk : chunks.values()) {
            chunk.draw(batch);
        }
    }
}
