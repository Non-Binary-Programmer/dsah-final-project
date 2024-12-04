package src.items;

import src.*;
import src.iteminterfaces.Ammo;
import src.iteminterfaces.Fireable;
import src.iteminterfaces.Quaffable;
import src.iteminterfaces.Wearable;

public class Sling extends ItemBase implements Fireable, Wearable {
    public Sling(char ICON, int PRIORITY) {
        super('}', 2, 1, 1);
    }

    @Override
    public Slot getSlot() {
        return Slot.RANGED;
    }

    @Override
    public boolean attack(Entity source, Entity target, Ammo ammo) {
        if (source instanceof Player p) {
            if (target instanceof Enemy e) {
                if (Math.random() * (19 + p.getLevel() + ammo.getHitMod()) > (target.getArmor() + 20) * Math.random()) {
                    GamePanel.addMessage("You hit " + e + " with the " + ammo + ".");
                    target.setHealth(target.getHealth() - ammo.getDamage() * 2);
                    return ammo.hit(source, target);
                } else {
                    GamePanel.addMessage("You miss " + e + " with the " + ammo + ".");
                    return false;
                }
            } else {
                throw new RuntimeException("Player attacking non-Enemy");
            }
        } else {
            throw new RuntimeException("Arbitrary Entity wielding Sling not yet implemented.");
        }
    }

    @Override
    public int getRange(Ammo ammo) {
        return ammo.getBaseRange() * 2;
    }

    @Override
    public boolean canFire(Ammo ammo) {
        return ammo instanceof Stone || ammo instanceof Quaffable;
    }

    @Override
    public String getName() {
        return "Sling";
    }
}
