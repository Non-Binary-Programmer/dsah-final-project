package src.items;

import src.*;
import src.iteminterfaces.Ammo;
import src.iteminterfaces.Throwable;

public class Stone extends Item implements Ammo, Throwable {
    public Stone(char ICON, int PRIORITY, int count) {
        super('{', 1, 40, count);
    }

    @Override
    public String getName() {
        return "Stone";
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
        return (int) (Math.random() * 2) + 1;
    }

    @Override
    public boolean hit(Entity source, Entity target) {
        return false;
    }

    @Override
    public boolean attack(Entity source, Entity target) {
        if (source instanceof Player p) {
            if (target instanceof Enemy e) {
                if (Math.random() * (19 + p.getLevel()) > (target.getArmor() + 20) * Math.random()) {
                    GamePanel.addMessage("You hit " + e + " with the stone.");
                    target.setHealth(target.getHealth() - getDamage());
                    return false;
                } else {
                    GamePanel.addMessage("You miss " + e + " with the stone.");
                    return false;
                }
            }
        } else {
            throw new RuntimeException("Arbitrary Entity throwing items not yet implemented.");
        }
        return false;
    }

    @Override
    public int getRange() {
        return 7;
    }
}
