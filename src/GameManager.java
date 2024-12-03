package src;

import src.enemies.GiantRat;
import src.iteminterfaces.Quaffable;
import src.items.PotionCureLight;
import src.items.PotionPoison;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameManager implements KeyListener {
    private final GamePanel panel;
    private Tile[][] map;
    public static final int WIDTH = 200;
    public static final int HEIGHT = 50;
    private Player player;
    private int focusRow;
    private int focusCol;
    private State state = State.NORMAL;

    private enum State {
        NORMAL,
        QUAFF,
        INVENTORY,
        DEAD
    }

    public GameManager(GamePanel gamePanel) {
        this.panel = gamePanel;
        map = new Tile[HEIGHT][WIDTH];

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (row == 0 || row == HEIGHT - 1 || col == 0 || col == WIDTH - 1){
                    map[row][col] = new Tile(Terrain.WALL, row, col);
                } else {
                    map[row][col] = new Tile(Terrain.EMPTY, row, col);
                    if (Math.random() < 0.01) {
                        map[row][col].setEntity(new GiantRat(this, row, col));
                    }
                }
                map[row][col].setSeen(true);
                if (row == HEIGHT / 2 && col == WIDTH / 2) {
                    this.player = new Player(row, col, this);
                    map[row][col].setEntity(player);
                    focusCol = col;
                    focusRow = row;
                }
            }
        }
        player.giveItem(new PotionPoison(30, 2));
        player.giveItem(new PotionCureLight(5));
        gamePanel.updateDisplay(map, HEIGHT / 2, WIDTH / 2);
    }

    public Tile tileAt(int row, int col) {
        return map[row][col];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        boolean actionPerformed = false;
        switch (state) {
            case NORMAL -> {
                switch (e.getKeyChar()) {
                    case 'q' -> {
                        GamePanel.addMessage("Select a potion to quaff.");
                        panel.displayInventory(player.getItems(), item -> item instanceof Quaffable);
                        state = State.QUAFF;
                    }
                    case 'i' -> {
                        GamePanel.addMessage("Your inventory:");
                        panel.displayInventory(player.getItems());
                        state = State.INVENTORY;
                    }
                    case '1' -> {
                        map[player.getRow() + 1][player.getCol() - 1].receiveEntity(player);
                        actionPerformed = true;
                    }
                    case '2' -> {
                        map[player.getRow() + 1][player.getCol()].receiveEntity(player);
                        actionPerformed = true;
                    }
                    case '3' -> {
                        map[player.getRow() + 1][player.getCol() + 1].receiveEntity(player);
                        actionPerformed = true;
                    }
                    case '4' -> {
                        map[player.getRow()][player.getCol() - 1].receiveEntity(player);
                        actionPerformed = true;
                    }
                    case '5' -> {
                        actionPerformed = true;
                    }
                    case '6' -> {
                        map[player.getRow()][player.getCol() + 1].receiveEntity(player);
                        actionPerformed = true;
                    }
                    case '7' -> {
                        map[player.getRow() - 1][player.getCol() - 1].receiveEntity(player);
                        actionPerformed = true;
                    }
                    case '8' -> {
                        map[player.getRow() - 1][player.getCol()].receiveEntity(player);
                        actionPerformed = true;
                    }
                    case '9' -> {
                        map[player.getRow() - 1][player.getCol() + 1].receiveEntity(player);
                        actionPerformed = true;
                    }
                    default -> {
                        System.out.println(e.getKeyChar());
                        GamePanel.addMessage("See README if confused.");
                        panel.updateDisplay(map, focusRow, focusCol);
                    }
                }
            }
            case QUAFF -> {
                if (player.getItems().get((e.getKeyChar() - 'a')) instanceof Quaffable potion) {
                    potion.quaff(player);
                    Item potions = (Item) potion;
                    potions.setCount(potions.getCount() - 1);
                    if (potions.getCount() == 0) {
                        player.getItems().remove(potions);
                    }
                    actionPerformed = true;
                    state = State.NORMAL;
                } else {
                    GamePanel.addMessage("See README if confused.");
                    panel.updateDisplay(map, focusRow, focusCol);
                    state = State.NORMAL;
                }
            }
        }
        if (actionPerformed) {
            player.eachTurn();
            this.panel.updateDisplay(map, focusRow, focusCol);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean actionPerformed = false;
        switch (state) {
            case NORMAL -> {
                if (e.getKeyCode() == 38) { // up arrow
                    map[player.getRow() - 1][player.getCol()].receiveEntity(player);
                    actionPerformed = true;
                }
                if (e.getKeyCode() == 37) { // left arrow
                    map[player.getRow()][player.getCol() - 1].receiveEntity(player);
                    actionPerformed = true;
                }
                if (e.getKeyCode() == 40) { // down arrow
                    map[player.getRow() + 1][player.getCol()].receiveEntity(player);
                    actionPerformed = true;
                }
                if (e.getKeyCode() == 39) { // right arrow
                    map[player.getRow()][player.getCol() + 1].receiveEntity(player);
                    actionPerformed = true;
                }
                if (actionPerformed) {
                    player.eachTurn();
                    this.panel.updateDisplay(map, focusRow, focusCol);
                }
            }
            case INVENTORY -> {
                panel.updateDisplay(map, focusRow, focusCol);
                state = State.NORMAL;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void die() {
        state = State.DEAD;
    }
}
