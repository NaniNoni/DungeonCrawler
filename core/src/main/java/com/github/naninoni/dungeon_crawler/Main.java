package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private Slime slime;
    private Chest chest;
    private Terrain terrain;

    private SpriteBatch spriteBatch;
    private float stateTime;
    private ExtendViewport viewport;

    @Override
    public void create() {
        final float WORLD_WIDTH = 800;
        final float WORLD_HEIGHT = 800;
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT);
        terrain = new Terrain(100, 100);

        chest = new Chest();
        slime = new Slime();

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
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

        terrain.draw(viewport.getCamera());
        Player.getInstance().draw(spriteBatch, stateTime);
        chest.draw(spriteBatch, stateTime);

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
        terrain.dispose();
    }
}
