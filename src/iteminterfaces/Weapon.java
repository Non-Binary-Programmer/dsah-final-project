package src.iteminterfaces;

import src.Enemy;
import src.Item;
import src.Player;

public interface Weapon extends Wearable {
    void attack (Player player, Enemy enemy);

    void enchantToHit (int mod);

    void enchantDamage (int mod);

    @Override
    default boolean equip(Player player) {
        player.setWeapon(this);
        return true;
    }
}
