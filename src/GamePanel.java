package src;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private int charWidth = 200;
    private int charHeight = 50;

    private final JTextArea display;
    private final JLabel message;

    private static String messageText = "Welcome to the dungeon! Click on the map to begin";

    public GamePanel () {
        setLayout(new BorderLayout());

        display = new JTextArea();
        display.setEditable(false);
        display.setFont(new Font("Monospaced", Font.PLAIN, 12));
        display.setText((".".repeat(charWidth) + "\n").repeat(charHeight));
        add(display, BorderLayout.CENTER);

        message = new JLabel(messageText);
        add(message, BorderLayout.NORTH);

        display.addKeyListener(new GameManager(this));

        setVisible(true);
    }

    /**
     * Updates the display. Should be called only when the map or camera position changes. The camera position
     * is represented with x and y coordinates, which should point to the center of the area wished to be displayed.
     *
     * @param map       A row-major 2D array representing the map. Should include all tiles in the map.
     * @param centerRow The y-coordinate that should be centered.
     * @param centerCol The x-coordinate that should be centered.
     */
    public void updateDisplay (Tile[][] map, int centerRow, int centerCol) {
        int startX = Math.min(Math.max(centerCol - charWidth / 2, 0), map[0].length - charWidth);
        int endX = Math.min(startX + charWidth, map[0].length);
        int startY = Math.min(Math.max(centerRow - charHeight / 2, 0), map.length - charHeight);
        int endY = Math.min(startY + charHeight, map.length);
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
        message.setText(messageText);
        messageText = " ";
    }

    /**
     * Adds a message to the message that the player will see at the start of their next turn. Automatically spaces.
     * @param messageText The message to be added.
     */
    public static void addMessage(String messageText) {
        if (!GamePanel.messageText.isBlank()) {
            GamePanel.messageText += " ";
        } else {
            GamePanel.messageText = "";
        }
        GamePanel.messageText += messageText;
    }
}
