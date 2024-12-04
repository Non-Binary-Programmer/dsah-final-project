package src;

import org.jetbrains.annotations.NotNull;

public interface Item extends Comparable<Item> {
    int getCount();

    /**
     * Increases count up to a maximum of <code>STACK_SIZE</code>
     * @param items Number of items to be added
     * @return The amount of items left over
     */
    int addItems(int items);

    String getName();

    int compareTo(@NotNull Item other);

    String toString();

    String toString(boolean capitalized);

    void setCount(int count);

    char getIcon();

    int getPriority();

    int getStackSize();
}
