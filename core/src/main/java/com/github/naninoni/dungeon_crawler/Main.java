package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private Slime slime;
    private Chest chest;
    private ChunkManager chunkManager;

    private SpriteBatch spriteBatch;
    private float stateTime;
    private ExtendViewport viewport;

    @Override
    public void create() {
        final float WORLD_WIDTH = 800;
        final float WORLD_HEIGHT = 800;
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT);
        chunkManager = new ChunkManager();

        chest = new Chest();
        slime = new Slime();

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        spriteBatch = new SpriteBatch();
        stateTime = 0f;// Register the input processor to handle scroll events

        // On-scroll callback
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                OrthographicCamera camera = (OrthographicCamera) viewport.getCamera();

                // Zoom in when scrolling down (amountY is negative), zoom out when scrolling up
                camera.zoom = MathUtils.clamp(camera.zoom + amountY * 0.1f, 0.1f, 2.0f);
                camera.update();
                return true;
            }
        });
    }

    @Override
    // This isn't really the render function. It's the game loop,
    // But the LibGDX loop function is called render. The "real" render function is draw.
    public void render() {
        input();
        logic();
        draw();
    }

    private void logic() {
        slime.move();
    }

    private void input() {
        Player.getInstance().input(viewport);
        slime.input();
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        // Update camera
        spriteBatch.begin();

        chunkManager.render(spriteBatch);
        chest.draw(spriteBatch, stateTime);
        Player.getInstance().draw(spriteBatch, stateTime);

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        Player.getInstance().dispose();
        slime.dispose();
        spriteBatch.dispose();
    }
}
