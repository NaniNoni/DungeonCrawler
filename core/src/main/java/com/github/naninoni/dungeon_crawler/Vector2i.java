package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.math.Vector2;

/**
 * An extremely simple 2D vector that takes in `int`s
 * VERY LOOSELY based on the LibGDX Vector2
 */
public class Vector2i {
    public int x;
    public int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i(Vector2i v) {
        set(v);
    }

    public Vector2i cpy() {
        return new Vector2i(this);
    }

    public void set(Vector2i v) {
        x = v.x;
        y = v.y;
    }

    /**
     * Multiplies this vector by a scalar
     *
     * @return This vector for chaining
     */
    public Vector2i scl(int scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    /**
     * Adds the given components to this vector
     *
     * @param x The x-component
     * @param y The y-component
     * @return This vector for chaining
     */
    public Vector2i add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
