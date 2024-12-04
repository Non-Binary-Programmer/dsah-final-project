package src.iteminterfaces;

import src.Item;
import src.Player;

public interface Wearable extends Item {
    enum Slot {
        HEAD,
        BODY,
        GLOVES,
        WEAPON,
        OFFHAND,
        SHOES,
        CAPE,
        RING,
        AMULET,
        RANGED
    }

    Slot getSlot();

    /**
     * Performs an effect when a player equips this Wearable.
     *
     * @param player The player equipping it.
     * @return True if it equips successfully, false otherwise.
     */
    default boolean equip(Player player) {
        return true;
    }

    /**
     * Performs an effect when a player unequips this Wearable.
     *
     * @param player The player unequipping it.
     * @return True if it unequips successfully, false otherwise.
     */
    default boolean unequip(Player player) {
        return true;
    }
}
