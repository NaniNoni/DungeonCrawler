package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;

public class Player extends AnimatedGameObject<Player.PlayerAnimation> {
    private static Player instance;
    private Body body;

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
        AttackBack,
        AttackLeft,
        AttackRight

    }

    private boolean isMoving = false;
    private final float speed = 400f;
    private final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/characters/player.png"));
    private int health;
    private int maxHealth;
    private int attackDamage;



    public Player() {
        super(new Vector2(), 50f, PlayerAnimation.IdleFront);
        this.maxHealth = 100;
        this.health = maxHealth;
        this.attackDamage = 25;


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

    void createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.25f);
        //TODO: adjust radius later

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void update() {
        position.set(body.getPosition());
    }

    public void input(Viewport viewport) {
        Vector2 velocity = new Vector2();

        // Check for WASD key presses and update direction accordingly
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y += 1;  // Move up
            setAnimationState(PlayerAnimation.WalkBack);
            setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y -= 1;  // Move down
            setAnimationState(PlayerAnimation.WalkFront);
            setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x -= 1;  // Move left
            setAnimationState(PlayerAnimation.WalkLeft);
            setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x += 1;  // Move right
            setAnimationState(PlayerAnimation.WalkRight);
            setMoving(true);
        }

        // Not moving
        if (velocity.isZero()) {
            setMoving(false);
            // If the player isn't moving, switch to the idle animation for the last direction
            switch (getAnimationState()) {
                case WalkBack:
                    setAnimationState(PlayerAnimation.IdleBack);
                    break;
                case WalkFront:
                    setAnimationState(PlayerAnimation.IdleFront);
                    break;
                case WalkLeft:
                    setAnimationState(PlayerAnimation.IdleLeft);
                    break;
                case WalkRight:
                    setAnimationState(PlayerAnimation.IdleRight);
                    break;
            }
        } else {
            // Normalize and scale the velocity to maintain consistent speed
            velocity.nor().scl(getSpeed());
        }

        /**
        sleeping:  in box2d, bodies can enter a 'sleeping' state when they come to rest, and they need a significant force to 'wake' them up.
         */
        body.setLinearVelocity(velocity);
        body.setAwake(true); // Wake up the body


        // Set the body's linear velocity
        body.setLinearVelocity(velocity);
/*
        translation
            .nor() // Normalize translation vector so that the player doesn't move faster diagonally
            .scl(getSpeed())
            .scl(Gdx.graphics.getDeltaTime());

        position.add(translation);
*/
        // Have the camera follow the player
        viewport.getCamera().position.set(position.x, position.y, 0);
        viewport.getCamera().update();
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
            MathUtils.floor(position.x / (Chunk.CHUNK_SIZE * Chunk.TILE_SIZE)),
            MathUtils.floor(position.y / (Chunk.CHUNK_SIZE * Chunk.TILE_SIZE))
        );
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}

