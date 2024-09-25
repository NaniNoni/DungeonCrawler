package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.EnumMap;

public class Player {
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void flipSprite() {
        for (TextureRegion frame : animations.get(getAnimationState()).getKeyFrames()){
            frame.flip(true, false);
        }
    }

    public enum PlayerAnimation {
        IdleFront,
        IdleSide,

        WalkForward,
        WalkBackward,
        WalkSide,

    }

    public Vector2 position = new Vector2();
    private float speed = 200f;
    private Direction direction;
    private PlayerAnimation animationState = PlayerAnimation.Idle;
    private final EnumMap<PlayerAnimation, Animation<TextureRegion>> animations = new EnumMap<>(PlayerAnimation.class);
    Texture playerSheet = new Texture(Gdx.files.internal("sprites/characters/player.png"));

    public Player() {
        final int TEXTURES_PER_ROW = 9;
        final int TEXTURES_PER_COLUMN = 7;
        final float FRAME_DURATION = 0.25f;

        TextureRegion[][] regions = TextureRegion.split(playerSheet,
            playerSheet.getWidth() / TEXTURES_PER_ROW,
            playerSheet.getHeight() / TEXTURES_PER_COLUMN
        );

        TextureRegion[] idleBack = Arrays.copyOfRange(regions[0], 0, 6);
        TextureRegion[] idleSide = Arrays.copyOfRange(regions[1], 0, 6);
        TextureRegion[] idleForward = Arrays.copyOfRange(regions[2], 0, 6);
        TextureRegion[] walkBack = Arrays.copyOfRange(regions[3], 0, 6);
        TextureRegion[] walkSide = Arrays.copyOfRange(regions[4], 0, 6);
        TextureRegion[] walkUp = Arrays.copyOfRange(regions[5], 0, 6);
        TextureRegion[] attackFront = Arrays.copyOfRange(regions[6], 0, 4);
        TextureRegion[] attackSide = Arrays.copyOfRange(regions[7], 0, 4);
        TextureRegion[] attackBack = Arrays.copyOfRange(regions[8], 0, 4);
        TextureRegion[] die = Arrays.copyOfRange(regions[9], 0, 3);

        animations.put(PlayerAnimation.IdleFront, idleFront);
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
