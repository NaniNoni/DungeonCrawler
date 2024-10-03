package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private Slime slime;
    private Chest chest;
    private Terrain terrain;

    private SpriteBatch spriteBatch;
    private float stateTime;
    private OrthographicCamera camera;
    private ExtendViewport viewport;
    OrthogonalTiledMapRenderer renderer;

    @Override
    public void create() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        final float WORLD_WIDTH = 800;
        final float WORLD_HEIGHT = 800;
        camera = new OrthographicCamera();
        terrain = new Terrain(new Vector2(0,0), 100, 100, 1f);
        camera.setToOrtho(false, (width / height) * WORLD_WIDTH, WORLD_HEIGHT);

        Texture tiles = new Texture(Gdx.files.internal("sprites/tilesets/decor_16x16.png"));
        TextureRegion[][] splitTiles = TextureRegion.split(tiles, 16, 16);
        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();
        for (int l = 0; l < 20; l++) {
            TiledMapTileLayer layer = new TiledMapTileLayer(25, 25, 16, 16);
            for (int x = 0; x < 150; x++) {
                for (int y = 0; y < 100; y++) {
                    int ty = (int)(Math.random() * splitTiles.length);
                    int tx = (int)(Math.random() * splitTiles[ty].length);
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
                    layer.setCell(x, y, cell);
                }
            }
            layers.add(layer);
        }
        renderer = new OrthogonalTiledMapRenderer(map);

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
        Player.getInstance().input();
        slime.input();
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Update camera
        camera.update();
        renderer.setView(camera);
        renderer.render();
    }

    @Override
    public void dispose() {
        Player.getInstance().dispose();
        slime.dispose();
        spriteBatch.dispose();
        terrain.dispose();
    }
}
