package src;

import src.iteminterfaces.Fireable;
import src.iteminterfaces.Weapon;
import src.items.Sword;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private double experience;
    private int level;
    private double toNextLevel;
    private ArrayList<Item> items;
    private double fed;
    private Weapon weapon;
    private Fireable ranged;

    public Player(int row, int col, GameManager game) {
        super('@', row, col, game);
        setArmor(0);
        setHealth(10);
        setMaxHealth(10);
        this.experience = 0;
        this.level = 1;
        this.toNextLevel = 10;
        this.fed = 1.0;
        this.weapon = new Sword(0, 0);

        items = new ArrayList<>();
    }

    @Override
    public void attack(Entity other) {
        if (other instanceof Enemy enemy){
            if (this.weapon != null) {
                weapon.attack(this, enemy);
            } else {
                if (Math.random() * (19 + level) > (other.getArmor() + 20) * Math.random()) {
                    GamePanel.addMessage("You hit " + enemy + '.');
                    other.takeDamage((int) (Math.random() * 4) + 1, this);
                } else {
                    GamePanel.addMessage("You miss " + enemy + '.');
                }
            }
        }
    }

    @Override
    public void die(Tile location, Entity killer) {
        GamePanel.addMessage("You are dead.");
        getGame().die();
    }

    public void receiveExperience(double experience) {
        this.experience += experience;
        while (this.experience >= toNextLevel) {
            this.level++;
            this.experience -= toNextLevel;
            toNextLevel *= (1.8 + 0.1 * level);
            GamePanel.addMessage("Welcome to level " + level + '.');
        }
    }

    public int getLevel() {
        return level;
    }

    /**
     * Called when the player consumes food. Applies partial feeding if player is very fed.
     * @param foodValue The caloric value of the food from 0-1.
     */
    public void feed(double foodValue) {
        if (fed < 0.9 - foodValue) {
            fed += foodValue;
            return;
        } else if (0.9 - foodValue < fed && fed < 0.9) {
            foodValue -= 0.9 - fed;
            fed = 0.9;
        }
        fed += foodValue / 2.0;
        fed = Math.min(fed, 1.0);
    }

    public List<Item> getItems () {
        items.sort(Item::compareTo);
        return items;
    }

    public void giveItem (Item item) {
        for (Item owned : items) {
            if (owned.getName().equals(item.getName())) {
                int leftover = owned.addItems(item.getCount());
                if (leftover > 0) {
                    item.setCount(leftover);
                    items.add(item);
                    return;
                }
            }
        }
        items.add(item);
        items.sort(Item::compareTo);
    }

    @Override
    public void takeDamage(int damage, Entity source) {
        super.takeDamage(damage, source);
        GamePanel.addMessage("You have " + getHealth() + " health left.");
    }
}
