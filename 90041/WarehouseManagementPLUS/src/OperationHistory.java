/**
 * Stores operation records using a resizable 1D array.
 */
public class OperationHistory {

    private OperationRecord[] records;
    private int count;

    /**
     * Creates an empty operation history.
     */
    public OperationHistory() {
        this.records = new OperationRecord[Constants.INITIAL_HISTORY_CAPACITY];
        this.count = 0;
    }

    /**
     * Adds an operation record.
     *
     * @param record record to add
     */
    public void add(OperationRecord record) {
        if (this.count == this.records.length) {
            this.records = resizeRecordArray(this.records, this.records.length * Constants.SIZE_MULTIPLIER);
        }
        this.records[this.count++] = record;
    }

    /**
     * Prints the operation history summary.
     */
    public void printSummary() {
        if (this.count == 0) {
            System.out.println(Messages.NO_HISTORY);
            return;
        }

        System.out.printf(Constants.HISTORY_HEADER_FORMATTER,
                "Warehouse", "Type", "Item", "Moves", "Hits", "Position");
        System.out.println(Constants.HISTORY_DIVIDER);

        for (int i = 0; i < this.count; i++) {
            this.records[i].printSummary();
        }
    }

    private OperationRecord[] resizeRecordArray(OperationRecord[] oldArray, int newSize) {
        OperationRecord[] newArray = new OperationRecord[newSize];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        return newArray;
    }
}
