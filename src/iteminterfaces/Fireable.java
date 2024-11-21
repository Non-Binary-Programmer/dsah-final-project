package src.iteminterfaces;

import src.Entity;

public interface Fireable {
    /**
     * Attempts to hit the target.
     * @param source The entity shooting from this Fireable.
     * @param target The entity hit by this Fireable.
     * @param ammo The ammunition shot from this Fireable.
     */
    void attack(Entity source, Entity target, Ammo ammo);

    int getRange(Ammo ammo);

    boolean canFire(Ammo ammo);
}
