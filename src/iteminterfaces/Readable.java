package src.iteminterfaces;

import src.Item;
import src.Player;

public interface Readable extends Item {
    /**
     * Reads this readable.
     * @param player The player reading it.
     * @return `true` if the readable is consumed, `false` otherwise.
     */
    boolean read(Player player);
}
