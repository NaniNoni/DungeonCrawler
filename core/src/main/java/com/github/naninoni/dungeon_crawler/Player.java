package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.EnumMap;

public class Player extends AnimatedGameObject<Player.PlayerAnimation> {
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public enum PlayerAnimation {
        IdleFront,
        IdleBack,
        IdleLeft,
        IdleRight,

        WalkFront,
        WalkBack,
        WalkLeft,
        WalkRight
    }

    public boolean isMoving = false;
    private float speed = 400f;
    private final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/characters/player.png"));

    public Player() {
        super(new Vector2(), 50f, PlayerAnimation.IdleFront);

        final int TEXTURES_PER_ROW = 6;
        final int TEXTURES_PER_COLUMN = 10;
        final float FRAME_DURATION = 0.25f;

        TextureRegion[][] regions = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / TEXTURES_PER_ROW,
            spriteSheet.getHeight() / TEXTURES_PER_COLUMN
        );

        TextureRegion[] idleBack = Arrays.copyOfRange(regions[0], 0, 6);
        TextureRegion[] idleSide = Arrays.copyOfRange(regions[1], 0, 6);
        TextureRegion[] idleFront = Arrays.copyOfRange(regions[2], 0, 6);
        TextureRegion[] walkFront = Arrays.copyOfRange(regions[3], 0, 6);
        TextureRegion[] walkSide = Arrays.copyOfRange(regions[4], 0, 6);
        TextureRegion[] walkBack = Arrays.copyOfRange(regions[5], 0, 6);
        TextureRegion[] attackFront = Arrays.copyOfRange(regions[6], 0, 4);
        TextureRegion[] attackSide = Arrays.copyOfRange(regions[7], 0, 4);
        TextureRegion[] attackBack = Arrays.copyOfRange(regions[8], 0, 4);
        TextureRegion[] die = Arrays.copyOfRange(regions[9], 0, 3);

        animations.put(PlayerAnimation.IdleFront, new Animation<>(FRAME_DURATION, idleFront));
        animations.put(PlayerAnimation.IdleBack, new Animation<>(FRAME_DURATION, idleBack));
        animations.put(PlayerAnimation.IdleLeft, new Animation<>(FRAME_DURATION, idleSide));
        animations.put(PlayerAnimation.IdleRight, new Animation<>(FRAME_DURATION, idleFront));

        animations.put(PlayerAnimation.WalkFront, new Animation<>(FRAME_DURATION, walkFront));
        animations.put(PlayerAnimation.WalkBack, new Animation<>(FRAME_DURATION, walkBack));
        // TODO: update animation based on player's direction
        animations.put(PlayerAnimation.WalkLeft, new Animation<>(FRAME_DURATION, walkSide));
        animations.put(PlayerAnimation.WalkRight, new Animation<>(FRAME_DURATION, walkSide));
    }

    public PlayerAnimation getAnimationState() {
        return animationState;
    }
    public void setAnimationState(PlayerAnimation animationState) {
        this.animationState = animationState;
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
