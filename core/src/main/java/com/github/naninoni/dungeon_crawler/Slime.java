package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.EnumMap;

public class Slime {
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public enum SlimeAnimation {
        IdleFront,
        IdleBack,
        IdleLeft,
        IdleRight,

        MoveFront,
        MoveBack,
        MoveLeft,
        MoveRight
    }

    public Vector2 position = new Vector2();
    private float speed = 200f;
    private SlimeAnimation animationState = SlimeAnimation.IdleFront;
    private final EnumMap<SlimeAnimation, Animation<TextureRegion>> animations = new EnumMap<>(SlimeAnimation.class);
    Texture playerSheet = new Texture(Gdx.files.internal("sprites/characters/slime.png"));

    public Slime() {
        final int TEXTURES_PER_ROW = 7;
        final int TEXTURES_PER_COLUMN = 13;
        final float FRAME_DURATION = 0.25f;

        TextureRegion[][] regions = TextureRegion.split(playerSheet,
            playerSheet.getWidth() / TEXTURES_PER_ROW,
            playerSheet.getHeight() / TEXTURES_PER_COLUMN
        );
        //NOTE: any back animation is looking towards the player
        TextureRegion[] idleBack = Arrays.copyOfRange(regions[0], 0, 4);
        TextureRegion[] idleSide = Arrays.copyOfRange(regions[1], 0, 4);
        TextureRegion[] idleFront = Arrays.copyOfRange(regions[2], 0, 4);
        TextureRegion[] walkBack = Arrays.copyOfRange(regions[3], 0, 6);
        TextureRegion[] walkSide = Arrays.copyOfRange(regions[4], 0, 6);
        TextureRegion[] walkFront = Arrays.copyOfRange(regions[5], 0, 6);
        TextureRegion[] attackFront = Arrays.copyOfRange(regions[8], 0, 8);
        TextureRegion[] attackSide = Arrays.copyOfRange(regions[7], 0, 8);
        TextureRegion[] attackBack = Arrays.copyOfRange(regions[6], 0, 8);
        TextureRegion[] die = Arrays.copyOfRange(regions[12], 0, 5);
        //TODO: update animation for getting damage

        animations.put(SlimeAnimation.IdleFront, new Animation<>(FRAME_DURATION, idleFront));

        animations.put(SlimeAnimation.MoveFront, new Animation<>(FRAME_DURATION, walkFront));
        animations.put(SlimeAnimation.MoveBack, new Animation<>(FRAME_DURATION, walkBack));
        // TODO: update animation based on slime's direction
        animations.put(SlimeAnimation.MoveRight, new Animation<>(FRAME_DURATION, walkSide));
        animations.put(SlimeAnimation.MoveLeft, new Animation<>(FRAME_DURATION, walkSide));
    }

    public TextureRegion getCurrentFrame(float stateTime) {
        return animations.get(animationState).getKeyFrame(stateTime, true);
    }

    public SlimeAnimation getAnimationState() {
        return animationState;
    }
    public void setAnimationState(SlimeAnimation animationState) {
        this.animationState = animationState;
    }

    public void dispose() {
        playerSheet.dispose();
    }
}
