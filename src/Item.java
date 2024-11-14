package src;

public abstract class Item implements Comparable<Item> {
    abstract String getName();
    abstract char getIcon();
    abstract int getPriority();

    @Override
    public int compareTo(Item o) {
        if (getPriority() != o.getPriority()) {
            return getPriority() - o.getPriority();
        }
        return getName().compareTo(o.getName());
    }
}
