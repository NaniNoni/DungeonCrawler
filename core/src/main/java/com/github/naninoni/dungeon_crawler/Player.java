package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;
import java.util.Vector;

public class Player extends AnimatedGameObject<Player.PlayerAnimation> {
    private static Player instance;

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
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

    private boolean isMoving = false;
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

        TextureRegion[] idleFront = Arrays.copyOfRange(regions[0], 0, 6);
        TextureRegion[] idleRight = Arrays.copyOfRange(regions[1], 0, 6);
        TextureRegion[] idleBack = Arrays.copyOfRange(regions[2], 0, 6);
        TextureRegion[] walkFront = Arrays.copyOfRange(regions[3], 0, 6);
        TextureRegion[] walkRight = Arrays.copyOfRange(regions[4], 0, 6);
        TextureRegion[] walkBack = Arrays.copyOfRange(regions[5], 0, 6);
        TextureRegion[] attackFront = Arrays.copyOfRange(regions[6], 0, 4);
        TextureRegion[] attackSide = Arrays.copyOfRange(regions[7], 0, 4);
        TextureRegion[] attackBack = Arrays.copyOfRange(regions[8], 0, 4);
        TextureRegion[] die = Arrays.copyOfRange(regions[9], 0, 3);

        // Flipped animations
        TextureRegion[] idleLeft = flipTextureRegions(idleRight);
        TextureRegion[] walkLeft = flipTextureRegions(walkRight);

        animations.put(PlayerAnimation.IdleFront, new Animation<>(FRAME_DURATION, idleFront));
        animations.put(PlayerAnimation.IdleBack, new Animation<>(FRAME_DURATION, idleBack));
        animations.put(PlayerAnimation.IdleLeft, new Animation<>(FRAME_DURATION, idleLeft));
        animations.put(PlayerAnimation.IdleRight, new Animation<>(FRAME_DURATION, idleRight));

        animations.put(PlayerAnimation.WalkFront, new Animation<>(FRAME_DURATION, walkFront));
        animations.put(PlayerAnimation.WalkBack, new Animation<>(FRAME_DURATION, walkBack));
        animations.put(PlayerAnimation.WalkLeft, new Animation<>(FRAME_DURATION, walkLeft));
        animations.put(PlayerAnimation.WalkRight, new Animation<>(FRAME_DURATION, walkRight));
    }

    public void input(Viewport viewport) {
        Vector2 direction = new Vector2();

        // Check for WASD key presses and update direction accordingly
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y += 1;  // Move up
            setAnimationState(Player.PlayerAnimation.WalkBack);
            setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y -= 1;  // Move down
            setAnimationState(Player.PlayerAnimation.WalkFront);
            setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x -= 1;  // Move left
            setAnimationState(Player.PlayerAnimation.WalkLeft);
            setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x += 1;  // Move right
            setAnimationState(Player.PlayerAnimation.WalkRight);
            setMoving(true);
        }

        // Not moving
        if (direction.equals(Vector2.Zero)) {
            setMoving(false);
        }

        // If the player isn't moving, switch to the idle animation for the last direction
        if (!isMoving()) {
            switch (getAnimationState()) {
                case WalkBack:
                    setAnimationState(Player.PlayerAnimation.IdleBack);
                    break;
                case WalkFront:
                    setAnimationState(Player.PlayerAnimation.IdleFront);
                    break;
                case WalkLeft:
                    setAnimationState(Player.PlayerAnimation.IdleLeft);
                    break;
                case WalkRight:
                    setAnimationState(Player.PlayerAnimation.IdleRight);
                    break;
            }
        }

        // Normalize translation vector so that the player doesn't move faster diagonally
        direction.nor();

        // Move player based on direction and speed
        Vector2 translation = direction.scl(getSpeed() * Gdx.graphics.getDeltaTime());
        position.add(translation);

        // Have the camera follow the player
        viewport.getCamera().position.set(position.x, position.y, 0);
    }

    public PlayerAnimation getAnimationState() {
        return animationState;
    }

    public void setAnimationState(PlayerAnimation animationState) {
        this.animationState = animationState;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    /**
     * @return The position of the chunk the player is currently in.
     */
    public Vector2i getChunk() {
        return new Vector2i(
            MathUtils.floor(position.x / Chunk.CHUNK_SIZE),
            MathUtils.floor(position.y / Chunk.CHUNK_SIZE)
        );
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
