package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;

public class Slime extends AnimatedGameObject<Slime.SlimeAnimation> {
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
    // TODO: change animation to UP/DOWN instead of front back
    private float speed = 50f;
    Texture spriteSheet = new Texture(Gdx.files.internal("sprites/characters/slime.png"));

    public Slime() {
        super(SlimeAnimation.IdleFront);

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
        TextureRegion[] attackRight = Arrays.copyOfRange(regions[7], 0, 8);
        TextureRegion[] attackBack = Arrays.copyOfRange(regions[6], 0, 8);
        TextureRegion[] die = Arrays.copyOfRange(regions[12], 0, 5);
        //TODO: update animation for getting damage

        TextureRegion[] idleLeft = flipTextureRegions(idleRight);
        TextureRegion[] walkLeft = flipTextureRegions(walkRight);

        animations.put(SlimeAnimation.IdleFront, new Animation<>(FRAME_DURATION, idleFront));
        animations.put(SlimeAnimation.IdleBack, new Animation<>(FRAME_DURATION, idleBack));
        animations.put(SlimeAnimation.IdleLeft, new Animation<>(FRAME_DURATION, idleLeft));
        animations.put(SlimeAnimation.IdleRight, new Animation<>(FRAME_DURATION, idleRight));

        animations.put(SlimeAnimation.MoveFront, new Animation<>(FRAME_DURATION, walkFront));
        animations.put(SlimeAnimation.MoveBack, new Animation<>(FRAME_DURATION, walkBack));
        animations.put(SlimeAnimation.MoveLeft, new Animation<>(FRAME_DURATION, walkLeft));
        animations.put(SlimeAnimation.MoveRight, new Animation<>(FRAME_DURATION, walkRight));

        createBody(Main.getWorld(), new Vector2(0,0));
        physicsBody.setLinearDamping(30f);
    }

    void createBody(World world, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        physicsBody = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.25f);
        // TODO: adjust radius later

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        physicsBody.createFixture(fixtureDef);

        physicsBody.setUserData(this);


        // Dispose of shape to not display it
        shape.dispose();
    }

    public void update() {
        Vector2 playerPos = Player.getInstance().physicsBody.getPosition();
        Vector2 direction = playerPos.cpy().sub(physicsBody.getPosition());

        if (!direction.isZero()) {
            Vector2 velocity = direction.cpy().nor().scl(getSpeed());
            physicsBody.setLinearVelocity(velocity);
        }

        // Determining angle based on dir of player
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

        // when slime doesn't move switch to idle animation
        if (direction.isZero()) {
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
