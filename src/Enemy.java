package src;

public abstract class Enemy extends Entity {
    public final double BASE_EXPERIENCE;
    public final int CHALLENGE_RATING;

    private String name;
    private int armor;
    private int maxHealth;

    public Enemy(char ICON, GameManager game, String name, int armor, int maxHealth, double experience, int cr) {
        super(ICON, game);
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
        if (killer instanceof Player player) {
            player.receiveExperience(BASE_EXPERIENCE * ((double) player.getLevel() / CHALLENGE_RATING));
        }
        location.setEntity(null);
    }

    @Override
    public String toString() {
        return "the " + name;
    }
}
