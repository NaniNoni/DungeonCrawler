package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.EnumMap;

public abstract class AnimatedGameObject<E extends Enum<E>> {
    protected E animationState;
    protected EnumMap<E, Animation<TextureRegion>> animations;
    protected Body physicsBody;

    public AnimatedGameObject(E initialAnimationState) {
        this.animationState = initialAnimationState;
        this.animations = new EnumMap<>(initialAnimationState.getDeclaringClass());
    }

    public TextureRegion getCurrentFrame(float stateTime) {
        return animations.get(animationState).getKeyFrame(stateTime, true);
    }

    public void draw(SpriteBatch batch, float stateTime) {
        TextureRegion currentFrame = getCurrentFrame(stateTime);

        batch.draw(
            currentFrame,
            physicsBody.getPosition().x, physicsBody.getPosition().y
        );
    }

    public E getAnimationState() {
        return animationState;
    }

    public void setAnimationState(E animationState) {
        this.animationState = animationState;
    }

    protected static TextureRegion[] flipTextureRegions(final TextureRegion[] regions) {
        TextureRegion[] copy = new TextureRegion[regions.length];

        // Create a deep copy of the texture data
        // to not affect the regular (not-flipped) animation
        for (int i = 0; i < regions.length; i++) {
            copy[i] = new TextureRegion(regions[i]);
            copy[i].flip(true, false);
        }

        return copy;
    }
}
