package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class GameContactListener implements ContactListener {
    private static final float KNOCKBACK_FORCE = 500f;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // FIXME: figure out why this happens
        if (fixtureA.getBody().getUserData() == fixtureB.getBody().getUserData()) {
            return;
        }

        Object userDataA = fixtureA.getBody().getUserData();
        Object userDataB = fixtureB.getBody().getUserData();

        if (userDataA == null || userDataB == null) {
            Gdx.app.error(getClass().getName(), "Fixture body user data is null");
            Gdx.app.error(getClass().getName(), "A: " + fixtureA.getUserData());
            Gdx.app.error(getClass().getName(), "B: " + fixtureB.getUserData());
            return;
        }

        Gdx.app.log(userDataA.getClass().getName(), "A");
        Gdx.app.log(userDataB.getClass().getName(), "B");

        // NOTE: it seems that the player is always fixture A and the slime is B.
        // I don't know if this is universal, but whatever.

        assert(userDataA instanceof Player);
        assert(userDataB instanceof Slime);

        Player player = (Player) userDataA;
        Slime slime = (Slime) userDataB;

        applyKnockback(player, slime);
    }

    private void applyKnockback(Player player, Slime slime) {
        Vector2 knockbackDirection = player.physicsBody.getPosition().cpy()
            .sub(slime.physicsBody.getPosition()).nor();

        Gdx.app.log(getClass().getName(), "knockbackDirection: " + knockbackDirection);

        player.physicsBody.applyLinearImpulse(new Vector2(5000, 0), player.physicsBody.getWorldCenter(), true);
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
