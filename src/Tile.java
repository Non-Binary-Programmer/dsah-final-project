package src;

import java.util.Optional;

public class Tile {
    private Item[] items;
    private int money;
    private Entity entity;
    private Terrain terrain;
    private boolean seen;
    private final int row;
    private final int col;

    public Tile (Terrain terrain, int row, int col) {
        this.terrain = terrain;
        this.row = row;
        this.col = col;
        money = 0;
        seen = false;
        entity = null;
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

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
