package src;

public class GiantRat extends Enemy {
    public static final int CHALLENGE_RATING = 1;
    public static final int BASE_EXPERIENCE = 2;

    public GiantRat() {
        super('R', "giant rat", 2, 4, 2, 1);
    }

    @Override
    public void attack(Entity other) {
        if (other instanceof Player) {
            if (Math.random() * 15 > Math.random() * (other.getArmor() + 20)) {
                other.takeDamage((int) (Math.random() * 2) + 1);
            }
        }
    }
}
