package src;

public class Room {
    private final int width;
    private final int height;
    private final int startRow;
    private final int startCol;
    private final Tile[][] tiles;

    public Room (int startRow, int startCol, int width, int height) {
        this.width = width;
        this.height = height;
        this.startRow = startRow;
        this.startCol = startCol;
        this.tiles = new Tile[height][width];
    }

    public void setTile(int row, int col, Tile tile) {
        tiles[row - startRow][col - startCol] = tile;
    }
}
