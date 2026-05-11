/**
 * Student Name - Guancheng Rong
 * Student Id - 1856981
 * Student email - guancheng.rong@student.unimelb.edu.au
 * AI Usage Declaration -
 */

public class History {
    private int warehouseId;

    private OperationType type; // Operation type recorded in history.    private String item;
    private int moves;
    private int hits;
    private int row;
    private int col;

    public History(int warehouseId, OperationType type, String item, int moves, int hits, int row, int col) {
        this.warehouseId = warehouseId;
        this.type = type;
        this.item = item;
        this.moves = moves;
        this.hits = hits;
        this.row = row;
        this.col = col;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public OperationType getType() {
        return type;
    }

    public String getItem() {
        return item;
    }

    public int getMoves() {
        return moves;
    }

    public int getHits() {
        return hits;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
