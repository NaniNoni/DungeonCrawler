package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private Slime slime;
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
        slime = new Slime();

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

        // Check for WASD key presses and update direction accordingly
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y += 1;  // Move up
            player.setAnimationState(Player.PlayerAnimation.WalkFront);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y -= 1;  // Move down
            player.setAnimationState(Player.PlayerAnimation.WalkBack);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x -= 1;  // Move left
            player.setAnimationState(Player.PlayerAnimation.WalkLeft);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x += 1;  // Move right
            player.setAnimationState(Player.PlayerAnimation.WalkRight);
        } else {
            player.setAnimationState(Player.PlayerAnimation.IdleFront);
            // TODO: figure out last direction and update idle animation
        }

        // Normalize translation vector so that the player doesn't move faster diagonally
        direction.nor();

        Vector2 translation = direction.scl(player.getSpeed() * Gdx.graphics.getDeltaTime());
        player.position.add(translation);
    }

    private void logic() {
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Update camera
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        // Get current frame of animation for the current stateTime
        TextureRegion playerFrame = player.getCurrentFrame(stateTime);
        TextureRegion slimeFrame = slime.getCurrentFrame(stateTime);
        spriteBatch.begin();
        float playerWidth = playerFrame.getRegionWidth();
        float playerHeight = playerFrame.getRegionHeight();
        float slimeWidth = slimeFrame.getRegionWidth();
        float slimeHeight = slimeFrame.getRegionHeight();
        spriteBatch.draw(
            playerFrame,
            player.position.x, player.position.y,
            playerWidth * 2, playerHeight * 2
        );
        spriteBatch.draw(
            slimeFrame,
            slime.position.x, slime.position.y,
            slimeWidth * 2, slimeHeight * 2
        );
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
