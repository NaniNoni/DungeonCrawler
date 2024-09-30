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
        Vector2 direction = new Vector2();

        // Check for WASD key presses and update direction accordingly
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y += 1;  // Move up
            player.setAnimationState(Player.PlayerAnimation.WalkBack);
            player.setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y -= 1;  // Move down
            player.setAnimationState(Player.PlayerAnimation.WalkFront);
            player.setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x -= 1;  // Move left
            player.setAnimationState(Player.PlayerAnimation.WalkLeft);
            player.setMoving(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x += 1;  // Move right
            player.setAnimationState(Player.PlayerAnimation.WalkRight);
            player.setMoving(true);
        }

        // Not moving
        if (direction.equals(Vector2.Zero)) {
            player.setMoving(false);
        }

        // If the player isn't moving, switch to the idle animation for the last direction
        if (!player.isMoving()) {
            switch (player.getAnimationState()) {
                case WalkBack:
                    player.setAnimationState(Player.PlayerAnimation.IdleBack);
                    break;
                case WalkFront:
                    player.setAnimationState(Player.PlayerAnimation.IdleFront);
                    break;
                case WalkLeft:
                    player.setAnimationState(Player.PlayerAnimation.IdleLeft);
                    break;
                case WalkRight:
                    player.setAnimationState(Player.PlayerAnimation.IdleRight);
                    break;
            }
        }

        // Normalize translation vector so that the player doesn't move faster diagonally
        direction.nor();

        // Move player based on direction and speed
        Vector2 translation = direction.scl(player.getSpeed() * Gdx.graphics.getDeltaTime());
        player.position.add(translation);
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
