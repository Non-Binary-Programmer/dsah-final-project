package src.iteminterfaces;

import src.Enemy;
import src.Player;

public interface Weapon {
    void attack (Player player, Enemy enemy);

    void enchantToHit (int mod);

    void enchantDamage (int mod);
}
