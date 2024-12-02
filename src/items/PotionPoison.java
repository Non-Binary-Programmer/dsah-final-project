package src.items;

import src.Entity;
import src.Item;
import src.Player;
import src.iteminterfaces.Ammo;
import src.iteminterfaces.Quaffable;
import src.iteminterfaces.Throwable;

public class PotionPoison extends Item implements Quaffable, Throwable, Ammo {
    public PotionPoison () {
        super('?', 0);
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
        return false;
    }

    @Override
    public void quaff(Player player) {

    }

    @Override
    public boolean attack(Entity source, Entity target) {
        return false;
    }

    @Override
    public int getRange() {
        return 0;
    }

    @Override
    public String getName() {
        return "";
    }
}
