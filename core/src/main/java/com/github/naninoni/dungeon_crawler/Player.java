package com.github.naninoni.dungeon_crawler;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;

public class Player {
    public enum PlayerAnimation {
        Idle
    }

    private PlayerAnimation animationState = PlayerAnimation.Idle;
    private final EnumMap<PlayerAnimation, Texture> textures = new EnumMap<>(PlayerAnimation.class);
    private final EnumMap<PlayerAnimation, Animation<TextureRegion>> animations = new EnumMap<>(PlayerAnimation.class);

    public Player() {
        textures.put(PlayerAnimation.Idle, new Texture(Gdx.files.internal("Soldier/Soldier/Soldier-idle.png")));

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        int FRAME_COLS = 6;
        int FRAME_ROWS = 1;
        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

        // TODO: Optimize this bullshit
        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        for (PlayerAnimation animation : PlayerAnimation.values()) {
            TextureRegion[][] tmp = TextureRegion.split(textures.get(animation),
                textures.get(animation).getWidth() / FRAME_COLS,
                textures.get(animation).getHeight() / FRAME_ROWS
            );

            int index = 0;
            for (int i = 0; i < FRAME_ROWS; i++) {
                for (int j = 0; j < FRAME_COLS; j++) {
                    frames[index++] = tmp[i][j];
                }
            }

            animations.put(animation, new Animation<>(0.025f, frames));
        }
    }

    public TextureRegion getCurrentFrame(float stateTime) {
        return animations.get(animationState).getKeyFrame(stateTime, true);
    }

    public PlayerAnimation getAnimationState() {
        return animationState;
    }
    public void setAnimationState(PlayerAnimation animationState) {
        this.animationState = animationState;
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
    }
}
