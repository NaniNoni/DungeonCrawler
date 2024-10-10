package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class ChunkManager {
    private Map<Vector2, Chunk> chunks = new HashMap<Vector2, Chunk>();

    public ChunkManager() {
        // 1. Get player position
        Vector2 playerPos = Player.getInstance().position;
        int playerChunkX = MathUtils.floor(playerPos.x / Chunk.CHUNK_SIZE);
        int playerChunkY = MathUtils.floor(playerPos.y / Chunk.CHUNK_SIZE);

        // 2. Generate adjacent chunks
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Chunk chunk = new Chunk(playerChunkX + dx, playerChunkY + dy);
            }
        }
    }

    public void render() {
        for (Chunk chunk : chunks.values()) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                    // 1. Figure out what textures correspond to which noise data
                    // 2. Figure out where to place the textures in the tile map
                }
            }
        }
    }
}
