package src;

import src.enemies.GiantRat;
import src.iteminterfaces.Fireable;
import src.iteminterfaces.Quaffable;
import src.iteminterfaces.Weapon;
import src.iteminterfaces.Wearable;
import src.items.PotionCureLight;
import src.items.PotionPoison;
import src.items.Sword;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

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
        WEAR,
        DEAD
    }

    public GameManager(GamePanel gamePanel) {
        this.panel = gamePanel;
        map = new Tile[HEIGHT][WIDTH];
        player = new Player(HEIGHT / 2, WIDTH / 2, this);
        map = generateMap(HEIGHT / 2, WIDTH / 2);
        updateSeen();
        player.giveItem(new PotionPoison(30, 2));
        player.giveItem(new PotionCureLight(5));
        player.giveItem(new Sword(10, 10));
        gamePanel.updateDisplay(map, HEIGHT / 2, WIDTH / 2);
        System.out.println(Arrays.deepToString(map));
    }

    public void updateSeen() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (map[row][col].getRoom() == null || map[row][col].getRoom() == map[player.getRow()][player.getCol()].getRoom()) {
                    map[row][col].setSeen(true);
                }
            }
        }
    }

    public Tile[][] generateMap(int startRow, int startCol) {
        Tile[][] map = new Tile[HEIGHT][WIDTH];
        ArrayList<Room> rooms = new ArrayList<>();
        double typeChooser = Math.random();
        if (typeChooser < 2.0/3) { // Vertical walls first
            int[] verticalWalls = new int[WIDTH / 20];
            int[][] horizontalWalls = new int[WIDTH / 20][HEIGHT / 10];
            HashSet<Integer> seenWalls = new HashSet<>();

            for (int i = 0; i < WIDTH / 20; i++) { // Choose vertical walls
                int rand = 0;
                do {
                    rand = (int) (Math.random() * (WIDTH - 2)) + 1;
                } while (seenWalls.contains(rand));
                verticalWalls[i] = rand;
                seenWalls.add(rand);
            }
            verticalWalls = Arrays.stream(verticalWalls).sorted().toArray();
            for (int i = 0; i < WIDTH / 20; i++) { // Choose horizontal walls btwn each vertical wall
                seenWalls.clear();
                for (int j = 0; j < HEIGHT / 10; j++) {
                    int rand = (int) (Math.random() * (HEIGHT - 2)) + 1;
                    while (seenWalls.contains(rand)) {
                        rand = (int) (Math.random() * (HEIGHT - 2)) + 1;
                    }
                    horizontalWalls[i][j] = rand;
                    seenWalls.add(rand);
                }
                horizontalWalls[i] = Arrays.stream(horizontalWalls[i]).sorted().toArray();
            }

            // Create rooms
            rooms.add(new Room(1, 1, verticalWalls[0] - 1, horizontalWalls[0][0] - 1));
            for (int j = 1; j < horizontalWalls[0].length; j++) {
                rooms.add(new Room(
                        horizontalWalls[0][j-1] + 1,
                        1,
                        verticalWalls[0] - 1,
                        horizontalWalls[0][j] - horizontalWalls[0][j - 1] - 1
                ));
            }
            for (int i = 1; i < verticalWalls.length; i++) {
                System.out.println(i);
                rooms.add(new Room(
                        1,
                        verticalWalls[i - 1] + 1,
                        verticalWalls[i] - verticalWalls[i - 1] - 1,
                        horizontalWalls[i][0] - 1
                ));
                for (int j = 1; j < horizontalWalls[i].length; j++) {
                    rooms.add(new Room(
                            horizontalWalls[i][j-1] + 1,
                            verticalWalls[i - 1] + 1,
                            verticalWalls[i] - verticalWalls[i - 1] - 1,
                            horizontalWalls[i][j] - horizontalWalls[i][j-1] - 1
                            ));
                }
            }

            // Create tiles
            int vWall = 0;
            boolean onVWall = false;
            for (int col = 0; col < WIDTH; col++) {
                int hWall = 0;
                boolean onHWall = false;
                if (vWall < verticalWalls.length) {
                    if (col == verticalWalls[vWall]) {
                        onVWall = true;
                        vWall++;
                    } else {
                        onVWall = false;
                    }
                }
                for (int row = 0; row < HEIGHT; row++) {
                    if (vWall < verticalWalls.length && hWall < horizontalWalls[0].length) {
                        if (row == horizontalWalls[vWall][hWall]) {
                            onHWall = true;
                            hWall++;
                        } else {
                            onHWall = false;
                        }
                    }
                    if (row == 0 || row == HEIGHT - 1 || col == 0 || col == WIDTH - 1) { // Border walls
                        map[row][col] = new Tile(Terrain.WALL, row, col, null);
                        continue;
                    }

                    if (onHWall || onVWall) { // Non-border walls
                        if (row == startRow && col == startCol) {
                            map[row][col] = new Tile(Terrain.STAIRS_UP, row, col, null);
                            map[row][col].setEntity(player);
                            continue;
                        }
                        if (Math.random() < 0.95) {
                            map[row][col] = new Tile(Terrain.WALL, row, col, null);
                            continue;
                        }
                        map[row][col] = new Tile(Terrain.EMPTY, row, col, null);
                        continue;
                    }
                    if (row == startRow && col == startCol) {
                        map[row][col] = new Tile(Terrain.STAIRS_UP, row, col, rooms.get(hWall + vWall * horizontalWalls[0].length));
                        rooms.get(hWall + vWall * horizontalWalls[0].length).setTile(row, col, map[row][col]);
                        map[row][col].setEntity(player);
                        continue;
                    }
                    map[row][col] = new Tile(Terrain.EMPTY, row, col, rooms.get(hWall + vWall * horizontalWalls[0].length)); // Tile in a room
                    System.out.println(hWall);
                    System.out.println(vWall);
                    rooms.get(hWall + vWall * horizontalWalls[0].length).setTile(row, col, map[row][col]);
                }
            }
        } else { // Horizontal walls first
            int[] horizontalWalls = new int[HEIGHT / 10];
            int[][] verticalWalls = new int[HEIGHT / 10][WIDTH / 20];
            HashSet<Integer> seenWalls = new HashSet<>();

            for (int i = 0; i < HEIGHT / 10; i++) { // Choose horizontal walls
                int rand = (int) (Math.random() * (HEIGHT - 2)) + 1;
                while (seenWalls.contains(rand)) {
                    rand = (int) (Math.random() * (HEIGHT - 2)) + 1;
                }
                horizontalWalls[i] = rand;
                seenWalls.add(rand);
            }
            horizontalWalls = Arrays.stream(horizontalWalls).sorted().toArray();
            for (int i = 0; i < HEIGHT / 10; i++) { // Choose vertical walls btwn each horizontal wall
                seenWalls.clear();
                for (int j = 0; j < WIDTH / 20; j++) {
                    int rand = (int) (Math.random() * (WIDTH - 2)) + 1;
                    while (seenWalls.contains(rand)) {
                        rand = (int) (Math.random() * (WIDTH - 2)) + 1;
                    }
                    verticalWalls[i][j] = rand;
                    seenWalls.add(rand);
                }
                verticalWalls[i] = Arrays.stream(verticalWalls[i]).sorted().toArray();
            }

            // Create rooms
            rooms.add(new Room(1, 1, verticalWalls[0][0] - 1, horizontalWalls[0] - 1));
            for (int j = 1; j < verticalWalls[0].length; j++) {
                rooms.add(new Room(
                        1,
                        verticalWalls[0][j] - verticalWalls[0][j - 1] - 1,
                        horizontalWalls[0] - 1,
                        verticalWalls[0][j-1] + 1
                ));
            }
            for (int i = 1; i < horizontalWalls.length; i++) {
                rooms.add(new Room(
                        1,
                        horizontalWalls[i - 1] + 1,
                        horizontalWalls[i] - horizontalWalls[i - 1] - 1,
                        verticalWalls[i][0] - 1
                ));
                for (int j = 1; j < verticalWalls[i].length; j++) {
                    rooms.add(new Room(
                            horizontalWalls[i - 1] + 1,
                            verticalWalls[i][j-1] + 1,
                            verticalWalls[i][j] - verticalWalls[i][j-1] - 1,
                            horizontalWalls[i] - horizontalWalls[i - 1] - 1
                    ));
                }
            }

            // Create tiles
            int hWall = 0;
            boolean onHWall = false;
            for (int row = 0; row < HEIGHT; row++) {
                int vWall = 0;
                boolean onVWall = false;
                if (hWall < horizontalWalls.length) {
                    if (row == horizontalWalls[hWall]) {
                        onHWall = true;
                        hWall++;
                    } else {
                        onHWall = false;
                    }
                }
                for (int col = 0; col < WIDTH; col++) {
                    if (hWall < horizontalWalls.length && vWall < verticalWalls[0].length) {
                        if (col == verticalWalls[hWall][vWall]) {
                            onVWall = true;
                            vWall++;
                        } else {
                            onVWall = false;
                        }
                    }
                    if (row == 0 || row == HEIGHT - 1 || col == 0 || col == WIDTH - 1) { // Border walls
                        map[row][col] = new Tile(Terrain.WALL, row, col, null);
                        continue;
                    }
                    if (onHWall || onVWall) { // Non-border walls
                        if (row == startRow && col == startCol) {
                            map[row][col] = new Tile(Terrain.STAIRS_UP, row, col, rooms.get(hWall + vWall * verticalWalls[0].length));
                            map[row][col].setEntity(player);
                            continue;
                        }
                        if (Math.random() < 0.95) {
                            map[row][col] = new Tile(Terrain.WALL, row, col, null);
                            continue;
                        }
                        map[row][col] = new Tile(Terrain.EMPTY, row, col, null);
                        continue;
                    }
                    if (row == startRow && col == startCol) {
                        map[row][col] = new Tile(Terrain.STAIRS_UP, row, col, rooms.get(hWall + vWall * verticalWalls[0].length));
                        map[row][col].setEntity(player);
                        rooms.get(vWall + hWall * verticalWalls[0].length).setTile(row, col, map[row][col]);
                        continue;
                    }
                    map[row][col] = new Tile(Terrain.EMPTY, row, col, rooms.get(hWall + vWall * verticalWalls[0].length)); // Tile in a room
                    rooms.get(vWall + hWall * verticalWalls[0].length).setTile(row, col, map[row][col]);
                }
            }
        }
        return map;
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
                    case 'w' -> {
                        GamePanel.addMessage("Select an item to wear or wield.");
                        panel.displayInventory(player.getItems(), item -> item instanceof Wearable);
                        state = State.WEAR;
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
                if (player.getItemAt(e.getKeyChar()) instanceof Quaffable potion) {
                    potion.quaff(player);
                    ItemBase potions = (ItemBase) potion;
                    potions.setCount(potions.getCount() - 1);
                    if (potions.getCount() == 0) {
                        player.getItems().remove(potions);
                    }
                    actionPerformed = true;
                } else {
                    GamePanel.addMessage("See README if confused.");
                    panel.updateDisplay(map, focusRow, focusCol);
                }
                state = State.NORMAL;
            }
            case WEAR -> {
                if (player.getItemAt(e.getKeyChar()) instanceof Wearable equip) {
                    if (player.equip(equip)) {
                        actionPerformed = true;
                        player.getItems().remove(equip);
                        if (equip instanceof Weapon || equip instanceof Fireable) {
                            GamePanel.addMessage("You wield " + equip.toString(false));
                        } else {
                            GamePanel.addMessage("You wear " + equip.toString(false));
                        }
                    }
                } else {
                    GamePanel.addMessage("See README if confused.");
                    panel.updateDisplay(map, focusRow, focusCol);
                }
                state = State.NORMAL;
            }
        }
        if (actionPerformed) {
            player.eachTurn();
            updateSeen();
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
                    updateSeen();
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
