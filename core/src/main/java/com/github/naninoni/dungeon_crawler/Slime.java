package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.EnumMap;

public class Slime extends AnimatedGameObject<Slime.SlimeAnimation> {
    public float getSpeed() {
        return speed;
    }

    private boolean isMoving = false;
    private boolean isAttacking = false;
    private final float attackRange = 1.0f;

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
        MoveRight,
        AttackFront,
        AttackRight,
        AttackLeft,
        AttackBack
    }
    // TODO: change animation to UP/DOWN instead of front back
    private float speed = 100f;
    Texture spriteSheet = new Texture(Gdx.files.internal("sprites/characters/slime.png"));

    public Slime() {
        super(new Vector2(50, 50), 50f, SlimeAnimation.IdleFront);

        final int TEXTURES_PER_ROW = 7;
        final int TEXTURES_PER_COLUMN = 13;
        final float FRAME_DURATION = 0.25f;

        TextureRegion[][] regions = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / TEXTURES_PER_ROW,
            spriteSheet.getHeight() / TEXTURES_PER_COLUMN
        );

        //NOTE: any back animation is looking towards the player
        TextureRegion[] idleBack = Arrays.copyOfRange(regions[0], 0, 4);
        TextureRegion[] idleRight = Arrays.copyOfRange(regions[1], 0, 4);
        TextureRegion[] idleFront = Arrays.copyOfRange(regions[2], 0, 4);
        TextureRegion[] walkBack = Arrays.copyOfRange(regions[3], 0, 6);
        TextureRegion[] walkRight = Arrays.copyOfRange(regions[4], 0, 6);
        TextureRegion[] walkFront = Arrays.copyOfRange(regions[5], 0, 6);
        TextureRegion[] attackFront = Arrays.copyOfRange(regions[8], 0, 8);
        TextureRegion[] attackRight = Arrays.copyOfRange(regions[7], 0, 7);
        TextureRegion[] attackBack = Arrays.copyOfRange(regions[6], 0, 8);
        TextureRegion[] die = Arrays.copyOfRange(regions[12], 0, 5);
        //TODO: update animation for getting damage

        TextureRegion[] idleLeft = flipTextureRegions(idleRight);
        TextureRegion[] walkLeft = flipTextureRegions(walkRight);
        TextureRegion[] attackLeft = flipTextureRegions(attackRight);

        animations.put(SlimeAnimation.IdleFront, new Animation<>(FRAME_DURATION, idleFront));
        animations.put(SlimeAnimation.IdleBack, new Animation<>(FRAME_DURATION, idleBack));
        animations.put(SlimeAnimation.IdleLeft, new Animation<>(FRAME_DURATION, idleLeft));
        animations.put(SlimeAnimation.IdleRight, new Animation<>(FRAME_DURATION, idleRight));

        animations.put(SlimeAnimation.MoveFront, new Animation<>(FRAME_DURATION, walkFront));
        animations.put(SlimeAnimation.MoveBack, new Animation<>(FRAME_DURATION, walkBack));
        animations.put(SlimeAnimation.MoveLeft, new Animation<>(FRAME_DURATION, walkLeft));
        animations.put(SlimeAnimation.MoveRight, new Animation<>(FRAME_DURATION, walkRight));

        animations.put(SlimeAnimation.AttackFront, new Animation<>(FRAME_DURATION, attackFront));
        animations.put(SlimeAnimation.AttackBack, new Animation<>(FRAME_DURATION, attackBack));
        animations.put(SlimeAnimation.AttackLeft, new Animation<>(FRAME_DURATION, attackLeft));
        animations.put(SlimeAnimation.AttackRight, new Animation<>(FRAME_DURATION, attackRight));

    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public SlimeAnimation getAnimationState() {
        return animationState;
    }

    public void setAnimationState(Slime.SlimeAnimation animationState) {
        this.animationState = animationState;
    }

    public void input() {
        Vector2 slimeDirection = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            slimeDirection.y += 1;  // Move up
            setAnimationState(SlimeAnimation.MoveFront);
            setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            slimeDirection.y -= 1;  // Move down
            setAnimationState(SlimeAnimation.MoveBack);
            setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            slimeDirection.x -= 1;  // Move left
            setAnimationState(SlimeAnimation.MoveLeft);
            setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            slimeDirection.x += 1;  // Move right
            setAnimationState(SlimeAnimation.MoveRight);
            setMoving(true);
        }
        // when Not moving
        if (slimeDirection.equals(Vector2.Zero)) {
            setMoving(false);
        }

        // If the slime isn't moving, switch to the idle animation for the last direction
        if (!isMoving()) {
            switch (getAnimationState()) {
                case MoveBack:
                    setAnimationState(SlimeAnimation.IdleBack);
                    break;
                case MoveFront:
                    setAnimationState(SlimeAnimation.IdleFront);
                    break;
                case MoveLeft:
                    setAnimationState(SlimeAnimation.IdleLeft);
                    break;
                case MoveRight:
                    setAnimationState(SlimeAnimation.IdleRight);
                    break;
            }
        }

        // Normalize translation vector so that the player doesn't move faster diagonally
        slimeDirection.nor();

        // Move player based on direction and speed
        Vector2 translation = slimeDirection.scl(getSpeed() * Gdx.graphics.getDeltaTime());
        position.add(translation);
    }
    private void attackPlayer() {
        Player player = Player.getInstance();
        player.takeDamage(10);

    }
    public void move() {
        Vector2 playerPos = Player.getInstance().position;
        Vector2 direction = new Vector2(playerPos).sub(position);
        float distancetoPlayer = direction.len();

        if (distancetoPlayer > attackRange) {
            // Move towards the player
            isAttacking = false;
            direction.nor();
            Vector2 translation = direction.scl(speed * Gdx.graphics.getDeltaTime());
            position.add(translation);

            // set animation state according to direction
            float angle = direction.angleDeg();
            if (angle >= 45 && angle < 135) {
                setAnimationState(SlimeAnimation.MoveFront);
            } else if (angle >= 135 && angle < 225) {
                setAnimationState(SlimeAnimation.MoveLeft);
            } else if (angle >= 225 && angle < 315) {
                setAnimationState(SlimeAnimation.MoveBack);
            } else {
                setAnimationState(SlimeAnimation.MoveRight);
            }
        } else {
            // Slime is close enough to attack
            isAttacking = true;

            // Set the appropriate attack animation based on direction
            float angle = direction.angleDeg();
            if (angle >= 45 && angle < 135) {
                setAnimationState(SlimeAnimation.AttackFront);
            } else if (angle >= 135 && angle < 225) {
                setAnimationState(SlimeAnimation.AttackLeft);
            } else if (angle >= 225 && angle < 315) {
                setAnimationState(SlimeAnimation.AttackBack);
            } else {
                setAnimationState(SlimeAnimation.AttackRight);
            }

            attackPlayer();
        }

        // Set idle animation when the slime isn't moving or attacking
        if (!isAttacking && distancetoPlayer <= attackRange) {
            switch (getAnimationState()) {
                case MoveFront:
                    setAnimationState(SlimeAnimation.IdleBack);
                    break;
                case MoveBack:
                    setAnimationState(SlimeAnimation.IdleFront);
                    break;
                case MoveLeft:
                    setAnimationState(SlimeAnimation.IdleLeft);
                    break;
                case MoveRight:
                    setAnimationState(SlimeAnimation.IdleRight);
                    break;
            }
        }
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
