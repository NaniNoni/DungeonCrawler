package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener {
    // TODO: figure out why this has to be so big
    private static final float KNOCKBACK_FORCE = 1000000;

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

//        if (userDataA == null || userDataB == null) {
//            Gdx.app.error(getClass().getName(), "Fixture body user data is null");
//            Gdx.app.error(getClass().getName(), "A: " + fixtureA.getUserData());
//            Gdx.app.error(getClass().getName(), "B: " + fixtureB.getUserData());
//            return;
//        }
//
//        Gdx.app.log(userDataA.getClass().getName(), "A");
//        Gdx.app.log(userDataB.getClass().getName(), "B");

        // FIXME: this is gross
        Player player;
        Slime slime;
        if (userDataA instanceof Player && userDataB instanceof Slime) {
            player = (Player) userDataA;
            slime = (Slime) userDataB;
        } else if (userDataA instanceof Slime && userDataB instanceof Player) {
            player = (Player) userDataB;
            slime = (Slime) userDataA;
        } else {
            return; // Not a player-slime collision
        }

        applyKnockback(player, slime);
    }

    private void applyKnockback(Player player, Slime slime) {
        Vector2 knockbackDirection = player.physicsBody.getPosition().cpy()
            .sub(slime.physicsBody.getPosition()).nor();

        Gdx.app.log(getClass().getName(), "knockbackDirection: " + knockbackDirection);

        player.physicsBody.applyLinearImpulse(knockbackDirection.scl(KNOCKBACK_FORCE), player.physicsBody.getWorldCenter(), true);
        player.setKnockbackTimer(Player.KNOCKBACK_DURATION);
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
