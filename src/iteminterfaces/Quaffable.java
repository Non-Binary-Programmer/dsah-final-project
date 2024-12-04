package src.iteminterfaces;

import src.Item;
import src.Player;

public interface Quaffable extends Item {
    void quaff(Player player);
}
