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

        if (!currentPlayerChunk.equals(previousPlayerChunk)) {
            updateChunks();
            previousPlayerChunk = currentPlayerChunk;
        }
    }

    public void updateChunks() {
        int CHUNK_LOAD_RADIUS = 3;
        Vector2i currentPlayerChunk = Player.getInstance().getChunk();
        Map<Vector2i, Chunk> updatedChunks = new HashMap<>();

        for (int dx = -CHUNK_LOAD_RADIUS; dx <= CHUNK_LOAD_RADIUS; dx++) {
            for (int dy = -CHUNK_LOAD_RADIUS; dy <= CHUNK_LOAD_RADIUS; dy++) {
                Vector2i chunkPos = currentPlayerChunk.cpy().add(dx, dy);

                // Use existing chunk if available, otherwise create a new one
                if (chunks.containsKey(chunkPos)) {
                    updatedChunks.put(chunkPos, chunks.get(chunkPos));
                    continue;
                }

                System.out.println("Generating chunk at " + chunkPos);
                updatedChunks.put(chunkPos, new Chunk(chunkPos));
            }
        }

        // Replace old chunks with updated chunk map
        chunks.clear();
        chunks.putAll(updatedChunks);
    }

    public void draw(SpriteBatch batch) {
        for (Chunk chunk : chunks.values()) {
            chunk.draw(batch);
        }
    }
}
