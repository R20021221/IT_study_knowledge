public class history {
    public int warehouseId;



    public String type; //"MOVE"    "VIEW_SHELF"    "HIT_WALL" "HIT_RESTRICTED" "PLACE_ITEM" "PICK_ITEM"
    public String item;
    public int moves;
    public int hits;
    public int row;
    public int col;

    public history(int warehouseId, String type, String item, int moves, int hits, int row, int col) {
        this.warehouseId = warehouseId;
        this.type = type;
        this.item = item;
        this.moves = moves;
        this.hits = hits;
        this.row = row;
        this.col = col;
    }

}
