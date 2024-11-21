package src;

public class Player extends Entity {
    private double experience;
    private int level;
    private double toNextLevel;

    public Player (GameManager game) {
        super('@', game);
        setArmor(0);
        setHealth(10);
        setMaxHealth(10);
        this.experience = 0;
        this.level = 1;
        this.toNextLevel = 10;
    }

    public Player(int row, int col, GameManager game) {
        super('@', row, col, game);
        setArmor(0);
        setHealth(10);
        setMaxHealth(10);
        this.experience = 0;
        this.level = 1;
        this.toNextLevel = 10;
    }

    @Override
    public void attack(Entity other) {
        if (other instanceof Enemy enemy){
            if (Math.random() * (19 + level) > (other.getArmor() + 20) * Math.random()) {
                other.takeDamage((int) (Math.random() * 4) + 1, this);
                GamePanel.addMessage("You hit " + enemy + '.');
            } else {
                GamePanel.addMessage("You miss " + enemy + '.');
            }
        }
    }

    @Override
    public void die(Tile location, Entity killer) {

    }

    public void receiveExperience(double experience) {
        this.experience += experience;
        while (this.experience >= toNextLevel) {
            this.level++;
            this.experience -= toNextLevel;
            toNextLevel *= (1.8 + 0.1 * level);
            GamePanel.addMessage("Welcome to level " + level + '.');
        }
    }

    public int getLevel() {
        return level;
    }
}
