package src.enemies;

import src.Enemy;
import src.Entity;
import src.GameManager;
import src.Player;

public class GiantRat extends Enemy {
    public static final int CHALLENGE_RATING = 1;
    public static final int BASE_EXPERIENCE = 2;

    public GiantRat(GameManager game, int row, int col) {
        super('R', game, row, col, "giant rat", -1, 4, 2, 1);
    }

    @Override
    public void attack(Entity other) {
        if (other instanceof Player) {
            if (Math.random() * 15 > Math.random() * (other.getArmor() + 20)) {
                other.takeDamage((int) (Math.random() * 2) + 1, this);
            }
        }
    }

    @Override
    public int getThrowToHit() {
        return 1;
    }
}
