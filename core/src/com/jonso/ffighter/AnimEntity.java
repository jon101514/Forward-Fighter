package com.jonso.ffighter;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * An animated entity takes in all possible sprites and
 * swaps among them using update time.
 *
 * @author Jonathan So
 */
public class AnimEntity extends Entity {

    private Texture[] sprites; // Every possible sprite for this entity.
    /** animations is a HashMap that maps
     * String: the name of the animation, to
     * int[]: an array that specifies:
     * [0]: whether or not the animation loops
     * [1]: the speed of the animation
     * [2-]: indices in sprites array of the animation frames
     */
    private HashMap<String, int[]> animations;

    // TRACKING CURRENT ANIMATION
    private int[] currAnim; // Current frames of animation.
    private int currAnimIndex; // Current index in the currAnim array.
    private boolean canLoop; // Whether or not the animation should loop.
    private int timePerFrame; // How much time to spend per frame, in ms.
    private long timer; // Used to track when we can switch frames.

    /**
     * Constructor for an entity that takes in a Texture,
     * initial position, and then calculates a rectangle
     * based on them.
     *
     * @param sprites Texture.
     * @param x      Init X position.
     * @param y      Init Y position.
     */
    public AnimEntity(Texture[] sprites, int x, int y) {
        super(sprites[0], x, y);
        this.sprites = sprites;
    }

    /**
     * Set the animations HashMap so that AnimEntities can control
     * their animations.
     * @param animations A HashMap as specified above.
     */
    public void setupAnimations(HashMap<String, int[]> animations) {
        this.animations = animations;
    }

    /**
     * Triggers an animation contained within the animations HM.
     * @param animation The name of the animation we're swapping to.
     */
    public void switchAnimation(String animation) {
        currAnim = animations.get(animation);
        currAnimIndex = 2;
        canLoop = currAnim[0] == 1;
        timePerFrame = currAnim[1];
        timer = 0;
        this.sprite = sprites[currAnim[currAnimIndex]];
    }

    /**
     * Move the Entity (with super) and update the timer.
     * Call checkSwap to see if we need to swap frames.
     * @param delta The amount of time from the previous frame to now.
     */
    public void move(float delta) {
        super.move(delta);
        timer += delta;
        checkSwap();
    }

    /**
     * If we've spent too much time on the current frame, then...
     * swap to the next frame.
     * Depending on if this animation can loop, go to either the
     * first frame of the animation (if true) or stay on the final one (if false).
     */
    private void checkSwap() {
        if (timer > timePerFrame) { // It's time to swap
            currAnimIndex++;
            if (currAnimIndex >= currAnim.length) { // Out-of-bounds
                if (canLoop) { // Loop to the start
                    currAnimIndex = 2;
                } else { // Stay on the final frame
                    currAnimIndex = currAnim.length - 1;
                }
            }
            timer = 0;
            this.sprite = sprites[currAnim[currAnimIndex]];
        }
    }

}
