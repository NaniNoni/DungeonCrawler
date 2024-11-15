package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Box2D;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private Slime slime;
    // TODO: use the chest
    private Chest chest;
    private ChunkManager chunkManager;

    private SpriteBatch spriteBatch;
    private float stateTime;
    private ExtendViewport viewport;

    // Box 2D objects
    private static World world;
    private Box2DDebugRenderer debugRenderer;

    /**
     * Used to access the global Box2D World.
     * @return the global static instance of a Box2D World.
     */
    public static World getWorld() {
        return world;
    }

    @Override
    public void create() {
        final float WORLD_WIDTH = 800;
        final float WORLD_HEIGHT = 800;

        Box2D.init();
        world = new World(new Vector2(), true);
        debugRenderer = new Box2DDebugRenderer();
        world.setContactListener(new GameContactListener());

        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT);
        chunkManager = new ChunkManager();

        chest = new Chest();
        slime = new Slime();

        // This sprite batch is used to draw everything.
        // TODO: check if this is optimal
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
    }

    @Override
    // This isn't really the render function. It's the game loop,
    // But the LibGDX loop function is called render. The "real" render function is draw.
    public void render() {
        input();
        update();
        draw();
    }

    private void update() {
        final int VELOCITY_ITERATIONS = 6;
        final int POSITION_ITERATIONS = 2;

        float deltaTime = Gdx.graphics.getDeltaTime();
        world.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        Player.getInstance().update(viewport);
        chunkManager.update();
        slime.update();
    }

    private void input() {
        Player.getInstance().input();
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        // Update camera
        spriteBatch.begin();

        chunkManager.draw(spriteBatch);
        slime.draw(spriteBatch, stateTime);
        Player.getInstance().draw(spriteBatch, stateTime);

        spriteBatch.end();

        debugRenderer.render(world,viewport.getCamera().combined);
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
        world.dispose();
        debugRenderer.dispose();
    }
}
