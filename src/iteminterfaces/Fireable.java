package src.iteminterfaces;

import src.Entity;
import src.Item;
import src.Player;

public interface Fireable extends Wearable {
    /**
     * Attempts to hit the target.
     * @param source The entity shooting from this Fireable.
     * @param target The entity hit by this Fireable.
     * @param ammo The ammunition shot from this Fireable.
     * @return true if the ammo breaks, false otherwise
     */
    boolean attack(Entity source, Entity target, Ammo ammo);

    int getRange(Ammo ammo);

    boolean canFire(Ammo ammo);

    @Override
    default boolean equip (Player player) {
        player.setRanged(this);
        return true;
    }
}
