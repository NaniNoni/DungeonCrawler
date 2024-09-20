package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;
import java.util.EnumMap;

public class Player {
    public enum PlayerAnimation {
        Idle,
        Walk
    }

    private PlayerAnimation animationState = PlayerAnimation.Idle;
    private final EnumMap<PlayerAnimation, Animation<TextureRegion>> animations = new EnumMap<>(PlayerAnimation.class);
    Texture playerSheet = new Texture(Gdx.files.internal("Soldier/Soldier/Soldier.png"));

    public Player() {
        final int TEXTURES_PER_ROW = 9;
        final int TEXTURES_PER_COLUMN = 7;
        final float FRAME_DURATION = 0.25f;

        TextureRegion[][] regions = TextureRegion.split(playerSheet,
            playerSheet.getWidth() / TEXTURES_PER_ROW,
            playerSheet.getHeight() / TEXTURES_PER_COLUMN
        );

        TextureRegion[] idleFrames = Arrays.copyOfRange(regions[0], 0, 6);
        TextureRegion[] walkFrames = Arrays.copyOfRange(regions[1], 0, 8);
        animations.put(PlayerAnimation.Idle, new Animation<>(FRAME_DURATION, idleFrames));
        animations.put(PlayerAnimation.Walk, new Animation<>(FRAME_DURATION, walkFrames));
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
        playerSheet.dispose();
    }
}
