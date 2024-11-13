package src;

import javax.swing.*;

public class GameDisplay {
    private final JFrame frame;

    public enum State {
        GAME,
        DEAD,
        CHARACTER_CREATION
    }

    public GameDisplay () {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
