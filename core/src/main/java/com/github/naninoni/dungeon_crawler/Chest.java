package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Chest {
    public Vector2 position = new Vector2();
    private final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/objects/chest_01.png"));
    private boolean isOpen = false;
    private Animation<TextureRegion> openAnimation;

    public Chest() {
        final float FRAME_DURATION = 0.1f;
        final int TEXTURE_COUNT = 4;

        final int frameWidth = spriteSheet.getWidth() / TEXTURE_COUNT;
        final int frameHeight = spriteSheet.getHeight();

        TextureRegion[] sprites = new TextureRegion[TEXTURE_COUNT];
        for (int i = 0; i < TEXTURE_COUNT; i++) {
            sprites[i] = new TextureRegion(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight);
        }

        openAnimation = new Animation<>(FRAME_DURATION, sprites);
    }

    public void open() {
        isOpen = true;
    }

    public void draw(SpriteBatch batch, float stateTime) {
        TextureRegion currentFrame = getCurrentFrame(stateTime);

        batch.draw(
            currentFrame,
            position.x, position.y,
            position.x / 2, position.y / 2,
            currentFrame.getRegionWidth(),
            currentFrame.getRegionHeight(),
            2.0f, 2.0f, 0.0f
        );
    }

    public TextureRegion getCurrentFrame(float stateTime) {
        if (!isOpen) {
            // first key frame is the closed chest
            return openAnimation.getKeyFrames()[0];
        }

        return openAnimation.getKeyFrame(stateTime, false);
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
