package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

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
        WalkRight,
        AttackFront,
        AttackLeft,
        AttackRight,
        AttackBack
    }

    private boolean isMoving = false;
    private int health;
    private int maxHealth;
    private float speed = 400f;
    private final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/characters/player.png"));
    private float timeSinceLastHeal = 0f;
    private final float HEAL_INTERVAL = 3f;  // Heal every 3 seconds
    private final int HEAL_AMOUNT = 5;

    public Player() {
        super(new Vector2(), 50f, PlayerAnimation.IdleFront);
        maxHealth = 100;
        health = maxHealth;

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
        TextureRegion[] attackRight = Arrays.copyOfRange(regions[7], 0, 4);
        TextureRegion[] attackBack = Arrays.copyOfRange(regions[8], 0, 4);
        TextureRegion[] die = Arrays.copyOfRange(regions[9], 0, 3);

        // Flipped animations
        TextureRegion[] idleLeft = flipTextureRegions(idleRight);
        TextureRegion[] walkLeft = flipTextureRegions(walkRight);
        TextureRegion[] attackLeft = flipTextureRegions(attackRight);

        animations.put(PlayerAnimation.IdleFront, new Animation<>(FRAME_DURATION, idleFront));
        animations.put(PlayerAnimation.IdleBack, new Animation<>(FRAME_DURATION, idleBack));
        animations.put(PlayerAnimation.IdleLeft, new Animation<>(FRAME_DURATION, idleLeft));
        animations.put(PlayerAnimation.IdleRight, new Animation<>(FRAME_DURATION, idleRight));

        animations.put(PlayerAnimation.WalkFront, new Animation<>(FRAME_DURATION, walkFront));
        animations.put(PlayerAnimation.WalkBack, new Animation<>(FRAME_DURATION, walkBack));
        animations.put(PlayerAnimation.WalkLeft, new Animation<>(FRAME_DURATION, walkLeft));
        animations.put(PlayerAnimation.WalkRight, new Animation<>(FRAME_DURATION, walkRight));

        animations.put(PlayerAnimation.AttackFront, new Animation<>(FRAME_DURATION, attackFront));
        animations.put(PlayerAnimation.AttackBack, new Animation<>(FRAME_DURATION, attackBack));
        animations.put(PlayerAnimation.AttackLeft, new Animation<>(FRAME_DURATION, attackLeft));
        animations.put(PlayerAnimation.AttackRight, new Animation<>(FRAME_DURATION, attackRight));
    }
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.min(health, maxHealth);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            die();
        }
    }

    public void heal(int healAmount) {
        health = Math.min(health + healAmount, maxHealth);
    }
    public void update(float deltaTime) {

        timeSinceLastHeal += deltaTime;

        if (timeSinceLastHeal >= HEAL_INTERVAL) {
            heal(HEAL_AMOUNT);

            timeSinceLastHeal = 0f;
        }
    }

    private void die() {



    }

    public void input() {
        Vector2 direction = new Vector2();
        update(Gdx.graphics.getDeltaTime());

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
    public void dispose() {
        spriteSheet.dispose();
    }
}
