package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel {
    private int charWidth = 200;
    private int charHeight = 50;

    private final JTextArea display;
    private final JLabel message;

    public GamePanel () {
        setLayout(new BorderLayout());

        display = new JTextArea();
        display.setEditable(false);
        display.setFont(new Font("Monospaced", Font.PLAIN, 12));
        display.setText((".".repeat(charWidth) + "\n").repeat(charHeight));
        add(display, BorderLayout.CENTER);

        message = new JLabel("Welcome to the dungeon! Click on the map to begin");
        add(message, BorderLayout.NORTH);

        display.addKeyListener(null);

        setVisible(true);
    }

    /**
     * Updates the display. Should be called only when the map or camera position changes. The camera position
     * is represented with x and y coordinates, which should point to the center of the area wished to be displayed.
     * @param map A row-major 2D array representing the map. Should include all tiles in the map.
     * @param centerX The x-coordinate that should be centered.
     * @param centerY The y-coordinate that should be centered.
     */
    public void updateDisplay (Tile[][] map, int centerX, int centerY) {
        int startX = Math.max(centerX - charWidth / 2, 0);
        int endX = Math.min(startX + charWidth, map[0].length);
        int startY = Math.max(centerY - charHeight / 2, 0);
        int endY = Math.min(startY + charWidth, map[0].length);
        StringBuilder newDisplayText = new StringBuilder();

        for (int row = startY; row < endY; row++) {
            for (int col = startX; col < endX; col++) {
                if (map[row][col].isSeen()) {
                    newDisplayText.append(map[row][col].getVisual());
                } else {
                    newDisplayText.append(' ');
                }
            }
            newDisplayText.append('\n');
        }

        display.setText(newDisplayText.toString());
    }
}
