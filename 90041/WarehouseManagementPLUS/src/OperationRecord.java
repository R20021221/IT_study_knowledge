//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;

/**
 * Represents a single operation record for history logging.
 */
public class OperationRecord {

    private final int warehouseId;
    private final OperationType type;
    private final String itemName;
    private final int row;
    private final int col;
    private final int moves;
    private final int hits;

    /**
     * Creates an immutable operation record.
     */
    public OperationRecord(int warehouseId,
                           OperationType type,
                           String itemName,
                           int row,
                           int col,
                           int moves,
                           int hits) {
        this.warehouseId = warehouseId;
        this.type = type;
        this.itemName = itemName;
        this.row = row;
        this.col = col;
        this.moves = moves;
        this.hits = hits;
    }

    /**
     * Prints the record in the summary table format.
     */
    public void printSummary() {
        String position = "(" + this.row + "," + this.col + ")";
        System.out.printf(Constants.HISTORY_ROW_FORMATTER,
                this.warehouseId, this.type.name(), this.itemName, this.moves, this.hits, position);
    }
}
