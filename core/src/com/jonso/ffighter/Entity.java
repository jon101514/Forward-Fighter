package com.jonso.ffighter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.css.Rect;

/** An "entity" is a common abstract class for an object
 * in our game which has a position (x,y), width and height,
 * velocity, and a sprite.
 * They're represented by a Rectangle internally, so they can collide
 * with one another.
 *
 * @author Jonathan So
 *
 * The structure and layout of this class is based on that of
 * Kevin Glass' Space Invaders tutorial found at
 * https://www.cokeandcode.com/main/.
 * */
public abstract class Entity {

    protected int x, y; // POSITION
    protected int width, height; // SIZE
    protected float vx, vy; // VELOCITY
    protected Texture sprite; // SPRITE

    private Rectangle rect; // Rectangle for collisions.

    /**
     * Constructor for an entity that takes in a Texture,
     * initial position, and then calculates a rectangle
     * based on them.
     * @param sprite Texture.
     * @param x Init X position.
     * @param y Init Y position.
     */
    public Entity(Texture sprite, int x, int y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        width = sprite.getWidth();
        height = sprite.getHeight();
        this.rect = new Rectangle(x, y, width, height);
    }

    /**
     * Move by x and y velocity and deltaTime, and
     * keep the rectangle updated.
     * @param delta The amount of time from the previous frame to now.
     */
    public void move(float delta) {
        x += (delta * vx);
        y += (delta * vy);
        rect.set(x, y, width, height);
    }

    /**
     * Checks if this rect overlaps the rect of another Entity.
     * @param other The entity which we're checking collisions with.
     * @return Boolean whether or not this rectangle overlaps another.
     */
    public void checkCollision(Entity other) {
        if (rect.overlaps(other.getRect())) {
            resolveCollision(other);
        }
    }
    /** Overload for checkCollision that takes in a Hitbox. */
    public void checkCollision(Hitbox other) {
        if (rect.overlaps(other.getRect())) {
            resolveCollision(other);
        }
    }

    /** Implemented in some child classes. */
    public void resolveCollision(Entity other) { }
    public void resolveCollision(Hitbox other) { }

    /** Getters */
    public Texture getSprite() { return sprite; }
    public int getX() { return x; }
    public int getY() { return y; }
    /** Get the center X and Y of this entity. */
    public int getCX() { return x + (width/2); }
    public int getCY() { return y + (height/2); }
    public Rectangle getRect() { return rect; }
}
