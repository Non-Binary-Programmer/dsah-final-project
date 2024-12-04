package src;

import org.jetbrains.annotations.NotNull;

public abstract class ItemBase implements Item {
    private final char ICON;
    private final int PRIORITY; // Lower = more priority
    private final int STACK_SIZE;
    private int count;

    public ItemBase(char ICON, int PRIORITY, int STACK_SIZE, int count) {
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
    public int compareTo(@NotNull Item o) {
        if (PRIORITY != o.getPriority()) {
            return o.getPriority() - PRIORITY;
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

    public String toString(boolean capitalized) {
        if (capitalized) {
            if (count != 1) {
                return count + " " + getName() + "s";
            } else {
                return "A " + getName();
            }
        } else {
            if (count != 1) {
                return count + " " + getName() + "s";
            } else {
                return "a " + getName();
            }
        }
    }

    public void setCount (int count) {
        this.count = count;
    }

    public char getIcon() {
        return ICON;
    }

    public int getPriority() {
        return PRIORITY;
    }

    public int getStackSize() {
        return STACK_SIZE;
    }
}
