package src;

public abstract class Item implements Comparable<Item> {
    public final char ICON;
    public final int PRIORITY; // Lower = more priority
    public final int STACK_SIZE;
    private int count;

    public Item(char ICON, int PRIORITY, int STACK_SIZE, int count) {
        this.ICON = ICON;
        this.PRIORITY = PRIORITY;
        this.STACK_SIZE = STACK_SIZE;
        this.count = count;
    }

    public int getCount () {
        return count;
    }

    /**
     * Increases count up to a maximum of <code>STACK_SIZE</code>
     * @param items Number of items to be added
     * @return The amount of items left over
     */
    public int addItems (int items) {
        if (count + items > STACK_SIZE) {
            int temp = count + items - STACK_SIZE;
            count = STACK_SIZE;
            return temp;
        }
        count += items;
        return 0;
    }

    public abstract String getName();

    @Override
    public int compareTo(Item o) {
        if (PRIORITY != o.PRIORITY) {
            return o.PRIORITY - PRIORITY;
        }
        return getName().compareTo(o.getName());
    }

    public String toString() {
        if (count != 1) {
            return count + " " + getName() + "s";
        } else {
            return "A " + getName();
        }
    }

    public void setCount (int count) {
        this.count = count;
    }
}
