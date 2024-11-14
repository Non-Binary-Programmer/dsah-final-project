package src;

public abstract class Entity {
    public final char ICON;
    private int health;
    private int maxHealth;
    private int armor;

    public Entity (char ICON) {
        this.ICON = ICON;
    }

    public void attack (Entity other) {

    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (health < 0) {
            die();
        }
    }

    public abstract void die();
}
