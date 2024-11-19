package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameManager implements KeyListener {
    private final GamePanel panel;
    private Tile[][] map;
    public static final int WIDTH = 200;
    public static final int HEIGHT = 50;

    public GameManager(GamePanel gamePanel) {
        this.panel = gamePanel;
        map = new Tile[HEIGHT][WIDTH];

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (row == 0 || row == HEIGHT - 1 || col == 0 || col == WIDTH - 1){
                    map[row][col] = new Tile(Terrain.WALL);
                } else {
                    map[row][col] = new Tile(Terrain.EMPTY);
                }
            }
        }

        gamePanel.updateDisplay(map, WIDTH / 2, HEIGHT / 2);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
