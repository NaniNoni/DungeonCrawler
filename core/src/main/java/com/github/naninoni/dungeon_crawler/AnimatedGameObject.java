package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;

public abstract class AnimatedGameObject<E extends Enum<E>> extends GameObject {
    protected E animationState;
    protected EnumMap<E, Animation<TextureRegion>> animations;

    public AnimatedGameObject(Vector2 position, float colliderRadius, E initialAnimationState) {
        super(position, colliderRadius);

        this.animationState = initialAnimationState;
        this.animations = new EnumMap<>(initialAnimationState.getDeclaringClass());
    }

    public TextureRegion getCurrentFrame(float stateTime) {
        return animations.get(animationState).getKeyFrame(stateTime, true);
    }

    public void draw(SpriteBatch batch, float stateTime, float scale) {
        TextureRegion currentFrame = getCurrentFrame(stateTime);

        batch.draw(
            currentFrame,
            position.x, position.y,
            position.x / 2, position.y / 2,
            currentFrame.getRegionWidth(),
            currentFrame.getRegionHeight(),
            scale, scale, 0.0f
        );
    }
}
