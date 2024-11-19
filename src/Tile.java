package src;

import java.util.Optional;

public class Tile {
    private Item[] items;
    private int money;
    private Entity entity;
    private Terrain terrain;
    private boolean seen;

    public Tile (Terrain terrain) {
        this.terrain = terrain;
        money = 0;
        seen = false;
        entity = null;
    }

    public Optional<Entity> getEntity() {
        return Optional.ofNullable(entity);
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
}
