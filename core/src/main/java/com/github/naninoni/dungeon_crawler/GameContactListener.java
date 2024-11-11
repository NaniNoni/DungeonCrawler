package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class GameContactListener implements ContactListener {
    private static final float KNOCKBACK_FORCE = 20f;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null) return;
        if (fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;

        Object userDataA = fixtureA.getBody().getUserData();
        Object userDataB = fixtureB.getBody().getUserData();

        // Check if the collision is between a Player and a Slime
        if ((userDataA instanceof Player && userDataB instanceof Slime) ||
            (userDataA instanceof Slime && userDataB instanceof Player)) {

            // Determine which is the player and which is the slime
            Player player = userDataA instanceof Player ? (Player) userDataA : (Player) userDataB;
            Slime slime = userDataA instanceof Slime ? (Slime) userDataA : (Slime) userDataB;

            applyKnockback(player, slime);
        }
    }

    private void applyKnockback(Player player, Slime slime) {
        // Calculate knockback direction from slime to player
        Vector2 knockbackDirection = player.physicsBody.getPosition().cpy()
            .sub(slime.physicsBody.getPosition()).nor();

        // Apply knockback
        player.physicsBody.applyLinearImpulse(knockbackDirection.scl(KNOCKBACK_FORCE),
            player.physicsBody.getWorldCenter(), true);
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
