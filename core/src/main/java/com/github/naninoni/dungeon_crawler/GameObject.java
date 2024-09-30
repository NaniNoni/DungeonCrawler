package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    public Vector2 position;
    public Circle collider;

    public GameObject(Vector2 position, float colliderRadius) {
        this.position = position;
        this.collider = new Circle(position.x, position.y, colliderRadius);
    }
}
