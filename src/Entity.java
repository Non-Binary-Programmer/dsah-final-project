package src;

public abstract class Entity {
    public final char ICON;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    private int health;
    private int maxHealth;
    private int armor;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    private int row;
    private int col;

    public Entity (char ICON) {
        this.ICON = ICON;
    }

    public Entity (char ICON, int row, int col) {
        this.ICON = ICON;
        this.row = row;
        this.col = col;
    }

    public abstract void attack (Entity other);

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public abstract void die(Tile location, Entity killer);
}
