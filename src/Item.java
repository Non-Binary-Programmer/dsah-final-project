package src;

public abstract class Item implements Comparable<Item> {
    public final char ICON;
    public final int PRIORITY; // Lower = more priority

    public Item(char ICON, int PRIORITY) {
        this.ICON = ICON;
        this.PRIORITY = PRIORITY;
    }

    public abstract String getName();

    @Override
    public int compareTo(Item o) {
        if (PRIORITY != o.PRIORITY) {
            return o.PRIORITY - PRIORITY;
        }
        return getName().compareTo(o.getName());
    }
}
