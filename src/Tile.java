package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Tile {
    private Item[] items;
    private int money;
    private Entity entity;
    private Terrain terrain;
    private boolean seen;
    private final int row;
    private final int col;
    private final Room room;
    private final GameManager game;

    public Tile (Terrain terrain, int row, int col, Room room, GameManager game) {
        this.terrain = terrain;
        this.row = row;
        this.col = col;
        this.game = game;
        money = 0;
        seen = false;
        entity = null;
        this.room = room;
    }

    public Optional<Entity> getEntity() {
        return Optional.ofNullable(entity);
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Attempt to move an <code>Entity</code> onto this tile. Causes that entity to attack this <code>Tile</code>'s
     * <code>Entity</code> if it is present.
     * @param moving The <code>Entity</code> that tries to move onto this tile.
     * @return True if the move is successful, false otherwise.
     */
    public boolean receiveEntity(Entity moving) {
        if (this.entity != null) {
            moving.attack(this.entity);
            return false;
        }
        if (this.terrain == Terrain.WALL) {
            GamePanel.addMessage("There's a wall there!");
            setSeen(true);
            return false;
        }
        this.entity = moving;
        entity.getGame().tileAt(entity.getRow(), entity.getCol()).setEntity(null);
        entity.setRow(this.row);
        entity.setCol(this.col);
        return true;
    }

    /**
     * Used to determine the visual for this tile, as displayed on the screen.
     * @return The char representing the visual appearance of this tile.
     */
    public char getVisual () {
        if (entity != null) {
            return entity.ICON;
        }
        if (items != null) {
            if (items.length != 0) {
                return Arrays.stream(this.items).sorted().findFirst().orElseThrow().getIcon();
            }
        }
        switch (this.terrain) {
            case EMPTY -> {
                return '.';
            }
            case WALL -> {
                return '#';
            }
            case STAIRS_UP -> {
                return '<';
            }
            case STAIRS_DOWN -> {
                return '>';
            }
        }
        throw new RuntimeException("This tile's terrain doesn't have an associated display character!");
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String toString() {
        return this.getVisual() + "";
    }

    public Room getRoom() {
        return room;
    }

    /**
     * Returns an unordered list of all tiles adjacent to this one.
     * @return The list of tiles
     */
    public List<Tile> getAdjacent() {
        return getAdjacent(e -> true);
    }

    /**
     * Returns an unordered list of all tiles adjacent to this one matching a given condition.
     * @param condition The condition to check each tile for
     * @return The list of tiles
     */
    public List<Tile> getAdjacent(Predicate<Tile> condition) {
        ArrayList<Tile> adjacent = new ArrayList<>();
        if (this.row != 0) {
            if (this.col != 0) {
                if (condition.test(game.tileAt(row - 1, col - 1))) {
                    adjacent.add(game.tileAt(row - 1, col - 1));
                }
            }
            if (condition.test(game.tileAt(row - 1, col))) {
                adjacent.add(game.tileAt(row - 1, col));
            }
            if (this.col != GameManager.WIDTH - 1) {
                if (condition.test(game.tileAt(row - 1, col + 1))) {
                    adjacent.add(game.tileAt(row - 1, col + 1));
                }
            }
        }
        if (this.col != 0) {
            if (condition.test(game.tileAt(row, col - 1))) {
                adjacent.add(game.tileAt(row, col - 1));
            }
        }
        if (this.col != GameManager.WIDTH - 1) {
            if (condition.test(game.tileAt(row, col + 1))) {
                adjacent.add(game.tileAt(row, col + 1));
            }
        }
        if (this.row != GameManager.HEIGHT - 1) {
            if (this.col != 0) {
                if (condition.test(game.tileAt(row + 1, col - 1))) {
                    adjacent.add(game.tileAt(row + 1, col - 1));
                }
            }
            if (condition.test(game.tileAt(row + 1, col))) {
                adjacent.add(game.tileAt(row + 1, col));
            }
            if (this.col != GameManager.WIDTH - 1) {
                if (condition.test(game.tileAt(row + 1, col + 1))) {
                    adjacent.add(game.tileAt(row + 1, col + 1));
                }
            }
        }
        return adjacent;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }
}
