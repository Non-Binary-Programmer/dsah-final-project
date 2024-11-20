package src;

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

    public GameManager(GamePanel gamePanel) {
        this.panel = gamePanel;
        map = new Tile[HEIGHT][WIDTH];

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (row == 0 || row == HEIGHT - 1 || col == 0 || col == WIDTH - 1){
                    map[row][col] = new Tile(Terrain.WALL);
                } else {
                    map[row][col] = new Tile(Terrain.EMPTY);
                    if (Math.random() < 0.01) {
                        map[row][col].setEntity(new GiantRat());
                    }
                }
                map[row][col].setSeen(true);
                if (row == HEIGHT / 2 && col == WIDTH / 2) {
                    this.player = new Player(row, col);
                    map[row][col].setEntity(player);
                    focusCol = col;
                    focusRow = row;
                }
            }
        }

        gamePanel.updateDisplay(map, HEIGHT / 2, WIDTH / 2);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        boolean actionPerformed = false;
        switch (e.getKeyChar()) {
            case '1' -> {
                if (map[player.getRow() + 1][player.getCol() - 1].receiveEntity(player)) {
                    player.setRow(player.getRow() + 1);
                    player.setCol(player.getCol() - 1);
                    map[player.getRow() - 1][player.getCol() + 1].setEntity(null);
                }
                actionPerformed = true;
            }
            case '2' -> {
                if (map[player.getRow() + 1][player.getCol()].receiveEntity(player)) {
                    player.setRow(player.getRow() + 1);
                    map[player.getRow() - 1][player.getCol()].setEntity(null);
                }
                actionPerformed = true;
            }
            case '3' -> {
                if (map[player.getRow() + 1][player.getCol() + 1].receiveEntity(player)) {
                    player.setRow(player.getRow() + 1);
                    player.setCol(player.getCol() + 1);
                    map[player.getRow() - 1][player.getCol() - 1].setEntity(null);
                }
                actionPerformed = true;
            }
            case '4' -> {
                if (map[player.getRow()][player.getCol() - 1].receiveEntity(player)) {
                    player.setCol(player.getCol() - 1);
                    map[player.getRow()][player.getCol() + 1].setEntity(null);
                }
                actionPerformed = true;
            }
            case '5' -> {
                actionPerformed = true;
            }
            case '6' -> {
                if (map[player.getRow()][player.getCol() + 1].receiveEntity(player)) {
                    player.setCol(player.getCol() + 1);
                    map[player.getRow()][player.getCol() - 1].setEntity(null);
                }
                actionPerformed = true;
            }
            case '7' -> {
                if (map[player.getRow() - 1][player.getCol() - 1].receiveEntity(player)) {
                    player.setRow(player.getRow() - 1);
                    player.setCol(player.getCol() - 1);
                    map[player.getRow() + 1][player.getCol() + 1].setEntity(null);
                }
                actionPerformed = true;
            }
            case '8' -> {
                if (map[player.getRow() - 1][player.getCol()].receiveEntity(player)) {
                    player.setRow(player.getRow() - 1);
                    map[player.getRow() + 1][player.getCol()].setEntity(null);
                }
                actionPerformed = true;
            }
            case '9' -> {
                if (map[player.getRow() - 1][player.getCol() + 1].receiveEntity(player)) {
                    player.setRow(player.getRow() - 1);
                    player.setCol(player.getCol() + 1);
                    map[player.getRow() + 1][player.getCol() - 1].setEntity(null);
                }
                actionPerformed = true;
            }
            default -> {
                System.out.println(e.getKeyChar());
                GamePanel.addMessage("See README if confused.");
                panel.updateDisplay(map, focusRow, focusCol);
            }
        }
        if (actionPerformed) {
            this.panel.updateDisplay(map, focusRow, focusCol);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean actionPerformed = false;
        if (e.getKeyCode() == 38) { // up arrow
            if (map[player.getRow() - 1][player.getCol()].receiveEntity(player)) {
                player.setRow(player.getRow() - 1);
                map[player.getRow() + 1][player.getCol()].setEntity(null);
            }
            actionPerformed = true;
        }
        if (e.getKeyCode() == 37) { // left arrow
            if (map[player.getRow()][player.getCol() - 1].receiveEntity(player)) {
                player.setCol(player.getCol() - 1);
                map[player.getRow()][player.getCol() + 1].setEntity(null);
            }
            actionPerformed = true;
        }
        if (e.getKeyCode() == 40) { // down arrow
            if (map[player.getRow() + 1][player.getCol()].receiveEntity(player)) {
                player.setRow(player.getRow() + 1);
                map[player.getRow() - 1][player.getCol()].setEntity(null);
            }
            actionPerformed = true;
        }
        if (e.getKeyCode() == 39) { // right arrow
            if (map[player.getRow()][player.getCol() + 1].receiveEntity(player)) {
                player.setCol(player.getCol() + 1);
                map[player.getRow()][player.getCol() - 1].setEntity(null);
            }
            actionPerformed = true;
        }
        if (actionPerformed) {
            this.panel.updateDisplay(map, focusRow, focusCol);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
