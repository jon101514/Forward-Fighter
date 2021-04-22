package com.jonso.ffighter;

import com.badlogic.gdx.graphics.Texture;

/**
 * The enemy moves laterally towards the player and handles
 * collisions based on hitting the player or their attack.
 *
 * When an enemy is hit, it falls until it is off-screen, then
 * it is erased.
 * @author Jonathan So
 */
public class Enemy extends Entity {

    private GameScreen game; // We need a ref. to the GameScreen to delete self

    private float speed = 200; // Speed moving towards the player.
    private boolean falling = false; // Whether or not this is in the state of falling.

    /**
     * Constructor that makes an enemy and gets it moving.
     * @param sprite The texture for the enemy.
     * @param x Initial X pos.
     * @param y Initial Y pos.
     * @param facing Which way it'll move; +1 for facing right, -1 for facing left.
     * @param game Ref. to the GameScreen.
     */
    public Enemy(Texture sprite, int x, int y, int facing, GameScreen game) {
        super(sprite, x, y);
        this.game = game;
        this.vx = speed * facing;
    }

    /**
     * Resolves hitbox-based collisions for hitboxes tagged
     * "Player" (damage the player) or
     * "Player_Attack" (get knocked back).
     * @param other The Hitbox colliding with the Enemy.
     */
    public void resolveCollision(Hitbox other) {
        if (other.getTag() == "Player" && !falling) {
            game.notifyDamage();
            fallInit(false);
        } else if (other.getTag() == "Player_Attack" && !falling) {
            fallInit(true);
        }
    }

    /**
     * Move, and if falling, then check if we're below the visible area to delete.
     * @param delta The amount of time from the previous frame to now.
     */
    public void move(float delta) {
        super.move(delta);
        if (falling) { fallLoop(); }
    }

    /**
     * When we first begin falling, set our movement vectors to simulate knockback.
     * Also add to the score.
     * @param knockback Whether or not to apply forceful knockback.
     */
    private void fallInit(boolean knockback) {
        falling = true;
        game.addToScore(10);
        if (knockback) {
            this.vx *= -6;
        } else {
            this.vx *= -0.5;
        }
    }

    /**
     * Call fallInit the first time, and
     * Continually accelerate towards the bottom of the screen, erasing
     * ourselves from the game if we're below the visible area.
     */
    private void fallLoop() {
        if (!falling) { fallInit(true); }
        this.vy += -speed;
        if (this.y < this.height) { // Erase from memory.
            game.removeEntity(this);
        }
    }
}
