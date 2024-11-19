package src;

import javax.swing.*;

public class GameDisplay {
    private final JFrame frame;
    private State state = State.GAME;
    private final GamePanel gPanel = new GamePanel();

    public enum State {
        GAME,
        DEAD,
        CHARACTER_CREATION
    }

    public GameDisplay () {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
