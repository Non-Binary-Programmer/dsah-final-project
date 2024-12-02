package src.items;

import src.*;
import src.iteminterfaces.Quaffable;
import src.iteminterfaces.Throwable;

public class PotionCureLight extends Item implements Quaffable, Throwable {
    public PotionCureLight(int count) {
        super('?', 0, 40, count);
    }

    @Override
    public String getName() {
        return "Potion of Cure Light Wounds";
    }

    @Override
    public String toString() {
        if (getCount() != 1) {
            return getCount() + " Potions of Cure Light Wounds";
        } else {
            return "A Potion of Cure Light Wounds";
        }
    }

    @Override
    public void quaff(Player player) {
        player.setHealth(Math.min(player.getHealth() + 20, player.getMaxHealth()));
        GamePanel.addMessage("You feel better. You have " + player.getHealth() + " health.");
    }

    @Override
    public boolean attack(Entity source, Entity target) {
        target.setHealth(Math.min(target.getHealth() + 20, target.getMaxHealth()));
        if (target instanceof Player player) {
            GamePanel.addMessage("You feel better. You have " + player.getHealth() + " health.");
        }
        if (target instanceof Enemy enemy) {
            GamePanel.addMessage(enemy.toString(true) + " looks healthier!");
        }
        return true;
    }

    @Override
    public int getRange() {
        return 10;
    }
}