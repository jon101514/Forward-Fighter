package com.jonso.ffighter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * The main screen of the game. Handles a lot of the
 * game logic, camera, and entity spawning/management.
 * @author Jonathan So
 */
public class GameScreen implements Screen {
    final FFighter game;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer; // For hitbox drawing

    // ASSETS
    // Textures
    private Texture playerTX;
    private Texture enemyTX;

    // ENTITIES AND HITBOXES
    private ArrayList<Entity> entities;
    private ArrayList<Entity> removeList;
    private ArrayList<Hitbox> hitboxes;

    private Player player;

    // Spawners.
    private Spawner lSpawn, rSpawn;

    // SCREEN PROPERTIES
    private int WIDTH = 1024;
    private int HEIGHT = 512;

    // GAME PROPS
    private int health = 1;
    private int hits = 0;
    private int score = 0;

    /**
     * Load assets, create entities with assets, and start the game.
     * @param game The FFighter object, passed from the previous screen.
     */
    public GameScreen(final FFighter game) {
        this.game = game;
        // LOAD ASSETS
        playerTX = new Texture(Gdx.files.internal("playertemp.png"));
        enemyTX = new Texture(Gdx.files.internal("basicenemy.png"));

        // CREATE AND SETUP CAMERA
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        // CREATE AND SETUP SHAPE RENDERER
        shapeRenderer = new ShapeRenderer();

        // CREATE ENTITIES
        entities = new ArrayList<Entity>();
        removeList = new ArrayList<Entity>();
        player = new Player(playerTX, (WIDTH / 2) - (playerTX.getWidth() / 2), HEIGHT / 3);
        entities.add(player);
        // Create spawners
        lSpawn = new Spawner(-enemyTX.getWidth(), HEIGHT / 3, playerTX.getHeight() / 3, this);
        rSpawn = new Spawner(WIDTH, HEIGHT / 3, playerTX.getHeight() / 3, this);
        // Create hitboxes
        hitboxes = new ArrayList<Hitbox>();
    }

    /**
     * The game loop, which will:
     * 1. Clear the screen and update the camera.
     * 2. Get rid of any garbage.
     * 3. Draw all entities.
     * 4. Process input.
     * 5. Handle logic.
     *
     * @param delta Time from the previous update to now.
     */
    @Override
    public void render(float delta) {
        // 1. CLEAR THE SCREEN AND UPDATE THE CAMERA
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // 2. GET RID OF ANY GARBAGE.
        entities.removeAll(removeList);
        removeList.clear();

        // 3. DRAW ALL ENTITIES, HITBOXES, AND TEXT
        hitboxes.addAll(player.getActive()); // Add hitboxes to draw.

        game.batch.begin();
        for (Entity ntt : entities) {
            ntt.move(delta);
            game.batch.draw(ntt.sprite, ntt.x, ntt.y);
        }

//        game.font.draw(game.batch, "Hitboxes Count: " + hitboxes.size(), 16, 32);
//        game.font.draw(game.batch, "Active Hitboxes Count: " + player.getActive().size(), 16, 64);
        // Draw text
        game.font.draw(game.batch, "Score: " + score, 16, 64);
        game.font.draw(game.batch, "Times Hit: " + hits, 16, 96);
        game.font.draw(game.batch, "D | F | V  and  K | J | N for Hi, Mid, Low attacks respectively.", 16, 128);
        game.font.draw(game.batch, "Forward Fighter v0.1 by Jonathan So, 2021.", 16, 160);

        game.batch.end();
        // Draw hitboxes
        for (Hitbox hb : hitboxes) {
            hb.draw(shapeRenderer);
        }

        // 4. PROCESS INPUT.
        processInput();

        // 5. HANDLE GAME LOGIC, LIKE COLLISIONS
        for (int i = 0; i < entities.size(); i++) {
            for (int j = 0; j < entities.size(); j++) {
                entities.get(i).checkCollision(entities.get(j));
            }
            for (int j = 0; j < hitboxes.size(); j++) {
                entities.get(i).checkCollision(hitboxes.get(j));
            }
        }
        // Update spawner timers.
        lSpawn.updateTimer(delta);
        rSpawn.updateTimer(delta);
        // Update player timers.
        player.checkTiming(delta);

        // 6. GET RID OF MORE GARBAGE.
        hitboxes.clear();
    }

    /**
     * Add an enemy at a position and face it towards the player.
     * @param x Init X position.
     * @param y Init Y position.
     */
    public void addEnemy(int x, int y) {
        // Deal with facing here (ternary operator)
        entities.add(new Enemy(enemyTX, x, y, x < 0 ? 1 : -1, this));
    }

    /** Add an entity to draw/update. */
    private void addEntity(Entity toAdd) {
        entities.add(toAdd);
    }

    /** Put an entity on our removeList to remove on next update.*/
    public void removeEntity(Entity toRemove) {
        removeList.add(toRemove);
    }

    /** Add points to the score; @param toAdd is the amount to add. */
    public void addToScore(int toAdd) { score += toAdd; }

    /** Update damage values internally. */
    public void notifyDamage() {
        health--;
        hits++;
        if (health <= 0) {
            // System.out.println("GAME OVER");
        }
    }

    /**
     * Take user input; DFV, KJN, and Esc quits.
     */
    public void processInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            player.attack("left", "md");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            player.attack("right", "md");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            player.attack("left", "hi");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            player.attack("right", "hi");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            player.attack("left", "lo");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            player.attack("right", "lo");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.exit(0);
        }
    }

    /** Dispose of any disposable resources. */
    @Override
    public void dispose() {
        playerTX.dispose();
        shapeRenderer.dispose();
    }

    /** REQUISITE METHODS */
    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
