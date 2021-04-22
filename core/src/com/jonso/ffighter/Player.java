package com.jonso.ffighter;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Color;

/**
 * The Player class is primarily a hitbox and animation
 * manager; when attacks are pressed, it fires off the
 * corresponding hitbox-animation pair, and when its
 * hurtbox is hit, it takes damage.
 *
 * @author Jonathan So
 */
public class Player extends Entity {
// public class Player extends AnimEntity {

    /** Player's hurtbox and six attack hitboxes. */
    private Hitbox hurtbox, lHi, lMd, lLo, rHi, rMd, rLo;
    private ArrayList<Hitbox> activeHitboxes; // AL<> of active hitboxes.
    private Hitbox currentHitbox; // The current attack hitbox.
    private float timer; // Timer for our attacks.
    private float currAttackTime; // Set to HI_MD_LO_TIMEs

    // CONSTANTS
    /** Amount of time (in s) these attacks are active. */
    private final float HI_TIME = 0.200f;
    private final float MD_TIME = 0.100f;
    private final float LO_TIME = 0.250f;
    private final int HITBOX_SIZE = 64;

//    /**
//     * Constructor for an entity that takes in a Texture,
//     * initial position, and then calculates a rectangle
//     * based on them.
//     *
//     * @param sprites Texture.
//     * @param x       Init X position.
//     * @param y       Init Y position.
//     */
//    public Player(Texture[] sprites, int x, int y) {
//        super(sprites, x, y);
//        // Setup the seven hitboxes
//        hurtbox = new Hitbox(this.x, this.y, this.width, this.height);
//        lHi = new Hitbox(this.x - 50, this.y + 100, 100, 100);
//        rHi = new Hitbox(this.x + this.width - 50, this.y + 100, 100, 100);
//        lMd = new Hitbox(this.x - 50, this.y + 50, 100, 100);
//        rMd = new Hitbox(this.x + this.width - 50, this.y + 50, 100, 100);
//        lLo = new Hitbox(this.x - 50, this.y, 100, 100);
//        rLo = new Hitbox(this.x + this.width - 50, this.y, 100, 100);
//        activeHitboxes = new ArrayList<Hitbox>();
//        activeHitboxes.add(hurtbox);
//    }

    /**
     * Constructor for the player. Sets up the hitboxes.
     * @param sprite Texture for the player.
     * @param x Init X position.
     * @param y Init Y position.
     */
    public Player(Texture sprite, int x, int y) {
        super(sprite, x, y);
        setupHitboxes();
    }

    /**
     * Create a hurtbox and six attack hitboxes as well as the activeHitboxes array.
     */
    private void setupHitboxes() {
        // Setup the seven hitboxes
        hurtbox = new Hitbox(this.x + (this.width / 4), this.y,
                this.width / 2, this.height, Color.RED, "Player");
        lHi = new Hitbox(this.x - (HITBOX_SIZE / 2), this.y + (this.height * 2 / 3),
                HITBOX_SIZE, HITBOX_SIZE, "Player_Attack");
        rHi = new Hitbox(this.x + this.width - (HITBOX_SIZE / 2), this.y + (this.height * 2 / 3),
                HITBOX_SIZE, HITBOX_SIZE, "Player_Attack");

        lMd = new Hitbox(this.x - (HITBOX_SIZE / 2), this.y + (this.height / 3),
                HITBOX_SIZE, HITBOX_SIZE, "Player_Attack");
        rMd = new Hitbox(this.x + this.width - (HITBOX_SIZE / 2), this.y + (this.height / 3),
                HITBOX_SIZE, HITBOX_SIZE, "Player_Attack");

        lLo = new Hitbox(this.x - (HITBOX_SIZE / 2), this.y,
                HITBOX_SIZE, HITBOX_SIZE, "Player_Attack");
        rLo = new Hitbox(this.x + this.width - (HITBOX_SIZE / 2), this.y,
                HITBOX_SIZE, HITBOX_SIZE, "Player_Attack");
        activeHitboxes = new ArrayList<Hitbox>();
        activeHitboxes.add(hurtbox);
        currentHitbox = null;
    }

    /**
     * Activate an attack based on the way the attack is facing and its height.
     * Cancels the previous attack, if present.
     * @param facing String either "left" or "right" denoting which way the player faces.
     * @param height String that's "hi", "md", or "lo" denoting attack height.
     */
    public void attack(String facing, String height) {
        // Only one attack at a time!
        if (currentHitbox != null) {
            deactivateAttack();
        }
        if (facing == "left") {
            switch(height) {
                case "hi":
                    activateAttack("lhi", lHi);
                    break;
                case "lo":
                    activateAttack("llo", lLo);
                    break;
                case "md":
                default:
                    activateAttack("lmd", lMd);
                    break;
            }
        } else { // facing == right
            switch(height) {
                case "hi":
                    activateAttack("rhi", rHi);
                    break;
                case "lo":
                    activateAttack("rlo", rLo);
                    break;
                case "md":
                default:
                    activateAttack("rmd", rMd);
                    break;
            }
        }
    }

    /** Getter for activeHitboxes */
    public ArrayList<Hitbox> getActive() {
        return activeHitboxes;
    }

    /**
     * Activates an attack with its timer and assigns it to the current hitbox.
     * @param animName String denoting name of animation to activate.
     * @param hb The hitbox to activate.
     */
    private void activateAttack(String animName, Hitbox hb) {
        // call the animation
        // switchAnimation(animName);

        // reset timer
        timer = 0;
        // add a hitbox to "list of hitboxes to draw and check"
        activeHitboxes.add(hb);
        currentHitbox = hb;
        switch(animName) {
            case "lhi":
            case "rhi":
                currAttackTime = HI_TIME;
                break;
            case "llo":
            case "rlo":
                currAttackTime = LO_TIME;
                break;
            case "lmd":
            case "rmd":
            default:
                currAttackTime = MD_TIME;
                break;
        }
    }

    /**
     * If currentHitbox is assigned, update its timer and check
     * if we should deactivate it.
     * @param delta Time from previous update to now.
     */
    public void checkTiming(float delta) {
        if (currentHitbox != null) {
            timer += delta;
            if (timer > currAttackTime) {
                deactivateAttack();
            }
        }
    }

    /**
     * Deactivate the attack by removing it from our activeHitboxes and
     * resetting other things under the hood.
     */
    private void deactivateAttack() {
        // Remove from hitboxes
        activeHitboxes.remove(currentHitbox);
        currentHitbox = null;
        // reset timer
        timer = 0;
    }
}
