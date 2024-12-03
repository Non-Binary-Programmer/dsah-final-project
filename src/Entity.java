package src;

import java.util.HashMap;

public abstract class Entity {
    public final char ICON;
    private final GameManager game;
    private HashMap<String, Status> statuses = new HashMap<>();

    public int getHealth() {
        return health;
    }

    public GameManager getGame() {
        return game;
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

    public Entity (char ICON, GameManager game) {
        this.ICON = ICON;
        this.game = game;
    }

    public Entity (char ICON, int row, int col, GameManager game) {
        this.ICON = ICON;
        this.row = row;
        this.col = col;
        this.game = game;
    }

    public abstract void attack (Entity other);

    public void takeDamage(int damage, Entity source) {
        this.health -= damage;
        System.out.println(damage);
        System.out.println(health);
        if (this.health <= 0) {
            die(game.tileAt(row, col), source);
        }
    }

    public abstract void die(Tile location, Entity killer);

    public void applyStatus(Status s) {
        if (statuses.containsKey(s.getName())){
            s.repeatApply(this, statuses.get(s.getName()));
        } else {
            s.onApply(this);
            statuses.put(s.getName(), s);
        }
    }

    public void removeStatus(Status s) {
        s.onEnd(this);
        this.statuses.remove(s.getName());
    }

    public void eachTurn() {
        statuses.forEach((k, v) -> v.eachTurn(this));
    }

    public HashMap<String, Status> getStatuses() {
        return statuses;
    }
}
