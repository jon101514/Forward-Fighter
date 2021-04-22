package com.jonso.ffighter;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;

/**
 * A Hitbox contains a representation of a rectangle
 * which checks collision with other boxes.
 * In practice, it's just a rectangle that has intersect checks
 * and the ability to draw itself for debugging.
 * @author  Jonathan So
 */
public class Hitbox {

    private String tag; // Tag for our hitbox; collision handling.
    private Rectangle rect; // Rectangle representation of hitbox.
    private Color col; // Color of our hitbox.

    /**
     * Constructor for the Hitbox; default sets color to green.
     * @param x Init X position.
     * @param y Init Y position.
     * @param width Width of the hitbox.
     * @param height Height of the hitbox.
     * @param tag Hitbox's tag for collision handling.
     */
    public Hitbox(int x, int y, int width, int height, String tag) {
        this.rect = new Rectangle(x, y, width, height);
        this.tag = tag;
        col = Color.GREEN;
    }

    /** Similar to the default constructor, but color is specified here. */
    public Hitbox(int x, int y, int width, int height, Color col, String tag) {
        this.rect = new Rectangle(x, y, width, height);
        this.col = col;
        this.tag = tag;
    }

    /**
     * Draws a representation of the hitbox using ShapeRenderer.
     * @param sr ShapeRenderer we're drawing our hitbox with.
     */
    public void draw(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(col);
        sr.rect(rect.x, rect.y, rect.width, rect.height);
        sr.end();
    }

    /**
     * Checks if this rect overlaps the rect of another Hitbox.
     * @param other The Hitbox which we're checking collisions with.
     * @return Boolean whether or not this rectangle overlaps another.
     */
    public boolean checkCollision(Hitbox other) {
        return rect.overlaps(other.getRect());
    }

    public boolean checkCollision(Rectangle other) {
        return rect.overlaps(other);
    }

    /** GETTERS */
    public Rectangle getRect() { return rect; }
    public String getTag() {return tag;}

}
