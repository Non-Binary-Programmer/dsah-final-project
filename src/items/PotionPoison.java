package src.items;

import src.*;
import src.iteminterfaces.Ammo;
import src.iteminterfaces.Quaffable;
import src.iteminterfaces.Throwable;
import src.statuses.Poison;

public class PotionPoison extends Item implements Quaffable, Throwable, Ammo {
    private int severity;

    public PotionPoison (int severity, int count) {
        super('?', 0, 40, count);
        this.severity = severity;
    }

    @Override
    public int getBaseRange() {
        return getRange();
    }

    @Override
    public int getHitMod() {
        return 0;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public boolean hit(Entity source, Entity target) {
        target.applyStatus(new Poison(severity / 2, source));
        return true;
    }

    @Override
    public void quaff(Player player) {
        player.applyStatus(new Poison(severity, player));
    }

    @Override
    public boolean attack(Entity source, Entity target) {
        if (source instanceof Player p) {
            if (target instanceof Enemy e) {
                if (Math.random() * (19 + p.getLevel()) > (target.getArmor() + 20) * Math.random()) {
                    GamePanel.addMessage("You hit " + e + " with the poison potion.");
                    return hit(source, target);
                } else {
                    GamePanel.addMessage("You miss " + e + " with the poison potion.");
                    return false;
                }
            }
        } else {
            throw new RuntimeException("Arbitrary Entity throwing potions not yet implemented.");
        }
        return false;
    }

    @Override
    public int getRange() {
        return 5;
    }

    @Override
    public String getName() {
        return "Potion of Poison";
    }

    @Override
    public String toString() {
        if (getCount() != 1) {
            return getCount() + " Potions of Poison";
        } else {
            return "A Potion of Poison";
        }
    }
}
