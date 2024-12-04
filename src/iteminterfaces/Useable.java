package src.iteminterfaces;

import src.Item;
import src.Player;

public interface Useable extends Item {
    /**
     * Use this useable.
     * @param player The player using this useable.
     * @return Whether a turn was used.
     */
    boolean use(Player player);
}
