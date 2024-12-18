package src.iteminterfaces;

import src.Entity;
import src.Item;

public interface Throwable extends Item {
    /**
     * Attempts to hit the target.
     * @param source The entity throwing this Throwable.
     * @param target The entity hit by this Throwable.
     * @return Whether this Throwable breaks.
     */
    boolean attack(Entity source, Entity target);

    int getRange();
}
