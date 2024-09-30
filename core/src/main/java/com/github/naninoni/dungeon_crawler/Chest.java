package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.influencers.RegionInfluencer;
import com.badlogic.gdx.math.Vector2;

public class Chest extends AnimatedGameObject<Chest.ChestAnimation> {
    public enum ChestAnimation {
        Open
    }

    private final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/objects/chest_01.png"));
    private boolean isOpen = false;
    private final Animation<TextureRegion> openAnimation;

    public Chest() {
        super(new Vector2(), 50f, ChestAnimation.Open);

        final float FRAME_DURATION = 0.1f;
        final int TEXTURE_COUNT = 4;

        final int frameWidth = spriteSheet.getWidth() / TEXTURE_COUNT;
        final int frameHeight = spriteSheet.getHeight();

        TextureRegion[] sprites = new TextureRegion[TEXTURE_COUNT];
        for (int i = 0; i < TEXTURE_COUNT; i++) {
            sprites[i] = new TextureRegion(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight);
        }

        openAnimation = new Animation<>(FRAME_DURATION, sprites);

        animations.put(ChestAnimation.Open, openAnimation);
    }

    public void open() {
        isOpen = true;
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
