package src.items;

import src.Entity;
import src.Item;
import src.Player;
import src.iteminterfaces.Ammo;
import src.iteminterfaces.Quaffable;
import src.iteminterfaces.Throwable;
import src.statuses.Poison;

public class PotionPoison extends Item implements Quaffable, Throwable, Ammo {
    private int severity;

    public PotionPoison (int severity) {
        super('?', 0);
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
        target.applyStatus(new Poison(severity / 2));
        return true;
    }

    @Override
    public void quaff(Player player) {
        player.applyStatus(new Poison(severity));
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
