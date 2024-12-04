package src.items;

import src.*;
import src.iteminterfaces.Weapon;
import src.iteminterfaces.Wearable;

public class Sword extends ItemBase implements Wearable, Weapon {
    private int toHit;
    private int dam;

    public Sword(int toHit, int dam) {
        super('|', 2, 1, 1);
        this.toHit = toHit;
        this.dam = dam;
    }

    @Override
    public String getName() {
        StringBuilder name = new StringBuilder();
        name.append("Sword (");
        if (!(toHit < 0)) {
            name.append('+');
        }
        name.append(toHit);
        name.append(", ");
        if (!(dam < 0)) {
            name.append('+');
        }
        name.append(dam);
        name.append(")");
        return name.toString();
    }

    @Override
    public void attack(Player player, Enemy enemy) {
        System.out.println("dam: " + dam);
        System.out.println("toHit: " + toHit);
        if (Math.random() * (19 + player.getLevel() + toHit) > (enemy.getArmor() + 20) * Math.random()) {
            GamePanel.addMessage("You hit " + enemy + '.');
            enemy.takeDamage((int) (Math.random() * 6) + 1 + dam, player);
        } else {
            GamePanel.addMessage("You miss " + enemy + '.');
        }
    }

    @Override
    public void enchantToHit(int mod) {
        this.toHit += mod;
    }

    @Override
    public void enchantDamage(int mod) {
        this.dam += mod;
    }

    @Override
    public Slot getSlot() {
        return Slot.WEAPON;
    }
}
