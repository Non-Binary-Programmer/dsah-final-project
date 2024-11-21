package src.iteminterfaces;

import src.Entity;

public interface Ammo {
    int getBaseRange();

    int getHitMod();

    int getDamage();

    /**
     * Applies any special effects corresponding to this ammo hitting a target.
     * @param source The Entity that fired this ammo.
     * @param target The Entity that was hit by this ammo.
     * @return Whether this ammo breaks after hitting the target.
     */
    boolean hit(Entity source, Entity target);
}
