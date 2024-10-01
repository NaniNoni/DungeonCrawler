package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private Slime slime;
    private Player player;
    private Chest chest;

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
        draw();
    }

    private void input() {
        player.input();
        slime.input();
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Update camera
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();

        player.draw(spriteBatch, stateTime, 2.0f);
        chest.draw(spriteBatch, stateTime, 2.0f);
        slime.draw(spriteBatch, stateTime, 2.0f);

        spriteBatch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        player.dispose();
        slime.dispose();
        spriteBatch.dispose();
    }
}
