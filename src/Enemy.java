package src;

public abstract class Enemy extends Entity {
    public final double BASE_EXPERIENCE;
    public final int CHALLENGE_RATING;
    private String name;
    private int armor;
    private int maxHealth;

    public Enemy(char ICON, GameManager game, int row, int col, String name, int armor, int maxHealth, double experience,
                 int cr) {
        super(ICON, row, col, game);
        this.armor = armor;
        this.setHealth(maxHealth);
        this.maxHealth = maxHealth;
        this.BASE_EXPERIENCE = experience;
        this.CHALLENGE_RATING = cr;
        this.name = name;
    }

    @Override
    public void die(Tile location, Entity killer) {
        GamePanel.addMessage("The " + name + " dies.");
        System.out.println(getRow());
        System.out.println(getCol());
        System.out.println(location.getRow());
        System.out.println(location.getCol());
        if (killer instanceof Player player) {
            player.receiveExperience(BASE_EXPERIENCE * ((double) player.getLevel() / CHALLENGE_RATING));
        }
        location.setEntity(null);
    }

    @Override
    public String toString() {
        return "the " + name;
    }

    public String toString(boolean capitalized) {
        if (capitalized) {
            return "The " + name;
        } else {
            return this.toString();
        }
    }

    /**
     * Called by Throwables to determine if the player gets hit after this entity throws something at them.
     * @return The factor of the attacker's attack roll
     */
    public abstract int getThrowToHit();
}
