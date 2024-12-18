package src;

import org.jetbrains.annotations.NotNull;
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
import java.util.*;

public class GameManager implements KeyListener {
    private static final int STAIRS_DISTANCE = 50;
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
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
               if (Math.random() < 0.005 && map[row][col].getEntity().isEmpty() && map[row][col].getTerrain() != Terrain.WALL) {
                   map[row][col].setEntity(new GiantRat(this, row, col));
               }
                if (Math.random() < 0.005 && map[row][col].getTerrain() != Terrain.WALL) {
                    map[row][col].setItems(new Item[]{new PotionCureLight(1)});
                } else if (Math.random() < 0.005 && map[row][col].getEntity().isEmpty() && map[row][col].getTerrain() != Terrain.WALL) {
                    map[row][col].setItems(new Item[]{new Sword((int) (Math.random() * 10), (int) (Math.random() * 10))});
                }
            }
        }
        updateSeen();
        placeStairs(tileAt(player.getRow(), player.getCol()));
        gamePanel.updateDisplay(map, HEIGHT / 2, WIDTH / 2);
        System.out.println(Arrays.deepToString(map));
    }

    private void placeStairs(Tile start) {
        HashSet<Tile> seen = new HashSet<>();
        HashSet<Tile> candidates = new HashSet<>();
        LinkedList<WeightedTile> queue = new LinkedList<>();
        queue.offer(new WeightedTile(start, 0));

        while (!queue.isEmpty()) {
            WeightedTile t = queue.poll();
            if (!seen.contains(t.tile)) {
                seen.add(t.tile);
                for (Tile neighbor : t.tile.getAdjacent((e -> e.getTerrain() != Terrain.WALL))) {
                    if (t.weight + 1 == STAIRS_DISTANCE) {
                        candidates.add(neighbor);
                    }
                    queue.offer(new WeightedTile(neighbor, t.weight + 1));
                }
            }
        }

        Tile stairsTile = candidates.stream().toList().get((int) (Math.random() * candidates.size()));
        stairsTile.setTerrain(Terrain.STAIRS_DOWN);
    }

    public void updateSeen() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (row == 0 || row == HEIGHT - 1 || col == 0 || col == WIDTH - 1) {
                    map[row][col].setSeen(true);
                    continue;
                }
                if (map[row][col].getRoom() != null && map[row][col].getRoom() == map[player.getRow()][player.getCol()].getRoom()) {
                    map[row][col].setSeen(true);
                }
            }
        }
        for (int row = 1; row < HEIGHT - 1; row++) {
            for (int col = 1; col < WIDTH - 1; col++) {
                if (map[row][col].getRoom() == null) {
                    if ((map[row - 1][col].isSeen() && map[row - 1][col].getRoom() != null)
                            || (map[row + 1][col].isSeen() && map[row + 1][col].getRoom() != null)
                            || (map[row][col - 1].isSeen() && map[row][col - 1].getRoom() != null)
                            || (map[row][col + 1].isSeen() && map[row][col + 1].getRoom() != null)
                            || (map[row - 1][col - 1].isSeen() && map[row - 1][col - 1].getRoom() != null)
                            || (map[row + 1][col + 1].isSeen() && map[row + 1][col + 1].getRoom() != null)
                            || (map[row + 1][col - 1].isSeen() && map[row + 1][col - 1].getRoom() != null)
                            || (map[row - 1][col + 1].isSeen() && map[row - 1][col + 1].getRoom() != null)) {
                        map[row][col].setSeen(true);
                    }
                }
            }
        }
    }

    public Tile[][] generateMap(int startRow, int startCol) {
        ArrayList<Room> rooms = new ArrayList<>();
        double typeChooser = Math.random();
        if (typeChooser < 2.0/3) { // Vertical walls first
            int[] verticalWalls = new int[WIDTH / 20];
            int[][] horizontalWalls = new int[WIDTH / 20 + 1][HEIGHT / 10];
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
            for (int i = 0; i < WIDTH / 20 + 1; i++) { // Choose horizontal walls btwn each vertical wall
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
            rooms.add(new Room(
                    horizontalWalls[0][horizontalWalls[0].length - 1] + 1,
                    1,
                    verticalWalls[0] - 1,
                    HEIGHT - horizontalWalls[0][horizontalWalls[0].length - 1] + 1
            ));
            for (int i = 1; i < verticalWalls.length; i++) {
                System.out.println(i);
                rooms.add(new Room(
                        1,
                        verticalWalls[i - 1] + 1,
                        verticalWalls[i] - verticalWalls[i - 1] - 1,
                        horizontalWalls[i][0] - 1
                ));
                for (int j = 1; j < horizontalWalls[0].length; j++) {
                    rooms.add(new Room(
                            horizontalWalls[i][j-1] + 1,
                            verticalWalls[i - 1] + 1,
                            verticalWalls[i] - verticalWalls[i - 1] - 1,
                            horizontalWalls[i][j] - horizontalWalls[i][j-1] - 1
                            ));
                }
                rooms.add(new Room(
                   horizontalWalls[i][horizontalWalls[0].length - 1] + 1,
                   verticalWalls[i - 1] + 1,
                   verticalWalls[i] - verticalWalls[i - 1] - 1,
                   HEIGHT - horizontalWalls[i][horizontalWalls[0].length - 1] - 1
                ));
            }
            rooms.add(new Room(
                    1,
                    verticalWalls[verticalWalls.length - 1] + 1,
                    WIDTH - verticalWalls[verticalWalls.length - 1] - 1,
                    horizontalWalls[verticalWalls.length][0] - 1
            ));
            for (int j = 1; j < horizontalWalls[0].length; j++) {
                rooms.add(new Room(
                        horizontalWalls[verticalWalls.length][j-1] + 1,
                        verticalWalls[verticalWalls.length - 1] + 1,
                        WIDTH - verticalWalls[verticalWalls.length - 1] - 1,
                        horizontalWalls[verticalWalls.length][j] - horizontalWalls[verticalWalls.length][j-1] - 1
                ));
            }
            rooms.add(new Room(
                    horizontalWalls[verticalWalls.length][horizontalWalls[0].length - 1] + 1,
                    verticalWalls[verticalWalls.length - 1] + 1,
                    WIDTH - verticalWalls[verticalWalls.length - 1] - 1,
                    HEIGHT - horizontalWalls[verticalWalls.length][horizontalWalls[0].length - 1] - 1
            ));

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
                } else {
                    onVWall = false;
                }
                for (int row = 0; row < HEIGHT; row++) {
                    if (vWall <= verticalWalls.length && hWall < horizontalWalls[0].length) {
                        if (row == horizontalWalls[vWall][hWall]) {
                            onHWall = true;
                            hWall++;
                        } else {
                            onHWall = false;
                        }
                    } else {
                        onHWall = false;
                    }
                    if (row == 0 || row == HEIGHT - 1 || col == 0 || col == WIDTH - 1) { // Border walls
                        map[row][col] = new Tile(Terrain.WALL, row, col, null, this);
                        continue;
                    }

                    if (onHWall || onVWall) { // Non-border walls
                        if (row == startRow && col == startCol) {
                            map[row][col] = new Tile(Terrain.STAIRS_UP, row, col, null, this);
                            map[row][col].setEntity(player);
                            map[row][col].setSeen(true);
                            continue;
                        }
                        if (Math.random() < 0.95) {
                            map[row][col] = new Tile(Terrain.WALL, row, col, null, this);
                            continue;
                        }
                        map[row][col] = new Tile(Terrain.EMPTY, row, col, null, this);
                        continue;
                    }
                    if (row == startRow && col == startCol) {
                        map[row][col] = new Tile(Terrain.STAIRS_UP, row, col, rooms.get(hWall + vWall * (horizontalWalls[0].length + 1)), this);
                        rooms.get(hWall + vWall * (horizontalWalls[0].length + 1)).setTile(row, col, map[row][col]);
                        map[row][col].setEntity(player);
                        continue;
                    }
                    map[row][col] = new Tile(Terrain.EMPTY, row, col, rooms.get(hWall + vWall * (horizontalWalls[0].length + 1)), this); // Tile in a room
                    System.out.println(hWall);
                    System.out.println(vWall);
                    rooms.get(hWall + vWall * (horizontalWalls[0].length + 1)).setTile(row, col, map[row][col]);
                }
            }
        } else { // Horizontal walls first
            int[] horizontalWalls = new int[HEIGHT / 10];
            int[][] verticalWalls = new int[HEIGHT / 10 + 1][WIDTH / 20];
            HashSet<Integer> seenWalls = new HashSet<>();

            for (int i = 0; i < HEIGHT / 10; i++) { // Choose horizontal walls
                int rand = 0;
                do {
                    rand = (int) (Math.random() * (HEIGHT - 2)) + 1;
                } while (seenWalls.contains(rand));
                horizontalWalls[i] = rand;
                seenWalls.add(rand);
            }
            horizontalWalls = Arrays.stream(horizontalWalls).sorted().toArray();
            for (int i = 0; i < HEIGHT / 10 + 1; i++) { // Choose horizontal walls btwn each vertical wall
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
            rooms.add(new Room(
                    1,
                    1,
                    verticalWalls[0][0] - 1,
                    horizontalWalls[0] - 1
            ));
            for (int j = 1; j < verticalWalls[0].length; j++) {
                rooms.add(new Room(
                        1,
                        verticalWalls[0][j-1] + 1,
                        verticalWalls[0][j] - verticalWalls[0][j - 1] - 1,
                        horizontalWalls[0] - 1
                ));
            }
            rooms.add(new Room(
                    1,
                    verticalWalls[0][verticalWalls[0].length - 1] + 1,
                    WIDTH - verticalWalls[0][verticalWalls[0].length - 1] + 1,
                    horizontalWalls[0] - 1
            ));
            for (int i = 1; i < horizontalWalls.length; i++) {
                System.out.println(i);
                rooms.add(new Room(
                        horizontalWalls[i - 1] + 1,
                        1,
                        verticalWalls[i][0] - 1,
                        horizontalWalls[i] - horizontalWalls[i - 1] - 1
                ));
                for (int j = 1; j < verticalWalls[0].length; j++) {
                    rooms.add(new Room(
                            horizontalWalls[i - 1] + 1,
                            verticalWalls[i][j-1] + 1,
                            verticalWalls[i][j] - verticalWalls[i][j-1] - 1,
                            horizontalWalls[i] - horizontalWalls[i - 1] - 1
                    ));
                }
                rooms.add(new Room(
                        horizontalWalls[i - 1] + 1,
                        verticalWalls[i][verticalWalls[0].length - 1] + 1,
                        WIDTH - verticalWalls[i][verticalWalls[0].length - 1] - 1,
                        horizontalWalls[i] - horizontalWalls[i - 1] - 1
                ));
            }
            rooms.add(new Room(
                    horizontalWalls[horizontalWalls.length - 1] + 1,
                    1,
                    verticalWalls[horizontalWalls.length][0] - 1,
                    HEIGHT - horizontalWalls[horizontalWalls.length - 1] - 1
            ));
            for (int j = 1; j < verticalWalls[0].length; j++) {
                rooms.add(new Room(
                        horizontalWalls[horizontalWalls.length - 1] + 1,
                        verticalWalls[horizontalWalls.length][j-1] + 1,
                        verticalWalls[horizontalWalls.length][j] - verticalWalls[horizontalWalls.length][j-1] - 1,
                        HEIGHT - horizontalWalls[horizontalWalls.length - 1] - 1
                ));
            }
            rooms.add(new Room(
                    horizontalWalls[horizontalWalls.length - 1] + 1,
                    verticalWalls[horizontalWalls.length][verticalWalls[0].length - 1] + 1,
                    WIDTH - verticalWalls[horizontalWalls.length][verticalWalls[0].length - 1] - 1,
                    HEIGHT - horizontalWalls[horizontalWalls.length - 1] - 1
            ));

            // Create tiles
            int hWall = 0;
            boolean onHWall;
            for (int row = 0; row < HEIGHT; row++) {
                int vWall = 0;
                boolean onVWall;
                if (hWall < horizontalWalls.length) {
                    if (row == horizontalWalls[hWall]) {
                        onHWall = true;
                        hWall++;
                    } else {
                        onHWall = false;
                    }
                } else {
                    onHWall = false;
                }
                for (int col = 0; col < WIDTH; col++) {
                    if (hWall <= horizontalWalls.length && vWall < verticalWalls[0].length) {
                        if (col == verticalWalls[hWall][vWall]) {
                            onVWall = true;
                            vWall++;
                        } else {
                            onVWall = false;
                        }
                    } else {
                        onVWall = false;
                    }
                    if (row == 0 || row == HEIGHT - 1 || col == 0 || col == WIDTH - 1) { // Border walls
                        map[row][col] = new Tile(Terrain.WALL, row, col, null, this);
                        continue;
                    }

                    if (onHWall || onVWall) { // Non-border walls
                        if (row == startRow && col == startCol) {
                            map[row][col] = new Tile(Terrain.STAIRS_UP, row, col, null, this);
                            map[row][col].setEntity(player);
                            map[row][col].setSeen(true);
                            continue;
                        }
                        if (Math.random() < 0.95) {
                            map[row][col] = new Tile(Terrain.WALL, row, col, null, this);
                            continue;
                        }
                        map[row][col] = new Tile(Terrain.EMPTY, row, col, null, this);
                        continue;
                    }
                    if (row == startRow && col == startCol) {
                        map[row][col] = new Tile(Terrain.STAIRS_UP, row, col, rooms.get(vWall + hWall * (verticalWalls[0].length + 1)), this);
                        rooms.get(vWall + hWall * (verticalWalls[0].length + 1)).setTile(row, col, map[row][col]);
                        map[row][col].setEntity(player);
                        continue;
                    }
                    map[row][col] = new Tile(Terrain.EMPTY, row, col, rooms.get(vWall + hWall * (verticalWalls[0].length + 1)), this); // Tile in a room
                    System.out.println(Arrays.toString(horizontalWalls));
                    System.out.println(Arrays.deepToString(verticalWalls));
                    rooms.get(vWall + hWall * (verticalWalls[0].length + 1)).setTile(row, col, map[row][col]);
                }
            }
        }
        try {
            placeStairs(map[player.getRow()][player.getCol()]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return generateMap(startRow, startCol);
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
                    case 's' -> {
                        GamePanel.addMessage("Health: " + player.getHealth() + "/" + player.getMaxHealth() + '.');
                        GamePanel.addMessage("Level: " + player.getLevel());
                        panel.updateDisplay(map, focusRow, focusCol);
                    }
                    case 'g' -> {
                        Item[] toAdd = map[player.getRow()][player.getCol()].getItems();
                        for (Item i : toAdd) {
                            player.giveItem(i);
                        }
                        map[player.getRow()][player.getCol()].setItems(null);
                        actionPerformed = true;
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
                    case '>' -> {
                        if (map[player.getRow()][player.getCol()].getTerrain() == Terrain.STAIRS_DOWN) {
                            map = generateMap(player.getRow(), player.getCol());
                            for (int row = 0; row < HEIGHT; row++) {
                                for (int col = 0; col < WIDTH; col++) {
                                    if (Math.random() < 0.005 && map[row][col].getEntity().isEmpty() && map[row][col].getTerrain() != Terrain.WALL) {
                                        map[row][col].setEntity(new GiantRat(this, row, col));
                                    }
                                }
                            }
                            updateSeen();
                            placeStairs(tileAt(player.getRow(), player.getCol()));
                            actionPerformed = true;
                        } else {
                            GamePanel.addMessage("There are no stairs down there!");
                        }
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
            pathfindEnemies();
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
                    pathfindEnemies();
                    this.panel.updateDisplay(map, focusRow, focusCol);
                }
            }
            case INVENTORY -> {
                panel.updateDisplay(map, focusRow, focusCol);
                state = State.NORMAL;
            }
        }
    }

    private void pathfindEnemies() {
        ArrayList<Enemy> toMove = new ArrayList<>();
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (map[row][col].isSeen()) {
                    if (map[row][col].getEntity().isPresent()) {
                        if (map[row][col].getEntity().orElseThrow() instanceof Enemy enemy) {
                            toMove.add(enemy);
                        }
                    }
                }
            }
        }
        for (Enemy enemy : toMove) {
            Tile next = pathfindNext(tileAt(enemy.getRow(), enemy.getCol()), tileAt(player.getRow(), player.getCol()));
            System.out.println(next.getRow());
            System.out.println(next.getCol());
            System.out.println(enemy.getRow());
            System.out.println(enemy.getCol());
            next.receiveEntity(enemy);
        }
    }

    public Tile pathfindNext(Tile start, Tile end) {
        HashMap<Tile, Integer> dists = new HashMap<>();
        HashMap<Tile, Tile> previous = new HashMap<>();
        HashSet<Tile> visited = new HashSet<>();
        dists.put(start, 0);
        visited.add(start);
        WeightedTile curr = new WeightedTile(start, 0);
        PriorityQueue<WeightedTile> frontier = new PriorityQueue<>();
        do {
            if (!frontier.isEmpty()) {
                curr = frontier.poll();
                visited.add(curr.tile);
            }
            for (Tile adjacent : curr.tile.getAdjacent(t -> t.getTerrain() != Terrain.WALL && (t.getEntity().stream().allMatch(e -> e == player)))) {

                if (!visited.contains(adjacent)) {
                    if (dists.containsKey(adjacent)) {
                        if (dists.get(curr.tile) + 1 < dists.get(adjacent)) {
                            dists.replace(adjacent, dists.get(curr.tile) + 1);
                            previous.replace(adjacent, curr.tile);
                        }
                        Optional<WeightedTile> inPriorityQueue = frontier.stream().filter(e -> e.tile.equals(adjacent)).findFirst();
                        if (inPriorityQueue.isPresent()) {
                            frontier.remove(inPriorityQueue.get());
                            frontier.offer(new WeightedTile(adjacent,
                                    1 + dists.get(curr.tile) + manhattanDistance(adjacent, end)));
                        }
                    } else {
                        dists.put(adjacent, dists.get(curr.tile) + 1);
                        previous.put(adjacent, curr.tile);
                        frontier.offer(new WeightedTile(adjacent, 1 + dists.get(curr.tile) + manhattanDistance(adjacent, end)));
                    }
                }
            }
        } while (!dists.containsKey(end) && !frontier.isEmpty());
        Tile backtrack = end;
        while (previous.get(backtrack) != start && previous.get(backtrack) != null) {
            backtrack = previous.get(backtrack);
        }
        if (previous.get(backtrack) == null) {
            return start;
        }
        return backtrack;
    }

    private static int manhattanDistance(Tile first, Tile second) {
        return Math.abs(first.getCol() - second.getCol()) + Math.abs(first.getRow() - second.getRow());
    }

    private record WeightedTile(Tile tile, int weight) implements Comparable<WeightedTile> {
        @Override
        public int compareTo(@NotNull WeightedTile o) {
            return weight - o.weight;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void die() {
        state = State.DEAD;
    }
}
