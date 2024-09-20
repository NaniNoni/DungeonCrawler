package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private Player player;
    private SpriteBatch spriteBatch;
    private float stateTime;
    private OrthographicCamera camera;
    private FitViewport viewport;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        final float WORLD_WIDTH = 800;
        final float WORLD_HEIGHT = 600;
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

        player = new Player();

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        Vector2 direction = new Vector2();
    }

    private void logic() {
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);

        // Update camera
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = player.getCurrentFrame(stateTime);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50,
            currentFrame.getRegionWidth() * 2,
            currentFrame.getRegionHeight() * 2
        );
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
