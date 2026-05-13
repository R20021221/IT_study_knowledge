/**
 * Represents a shelf that stores items in the warehouse.
 *
 * Internally, the shelf uses a resizable one-dimensional array
 * to manage items, similar to how an ArrayList works.
 */
public class Shelf {

    /**
     * Internal array holding the items on the shelf.
     * The array may have unused capacity beyond {@code size}.
     */
    private Item[] items;

    /**
     * The number of items currently stored on the shelf.
     */
    private int size;

    /**
     * Creates an empty shelf with an initial fixed capacity.
     */
    public Shelf() {
        this.items = new Item[Constants.INITIAL_SHELF_CAPACITY];
        this.size = 0;
    }

    /**
     * Copy constructor
     * @param shelf Shelf object to copy from
     */
    public Shelf(Shelf shelf) {
        this.items = new Item[shelf.items.length];
        for(int i = 0; i < shelf.items.length; i++) {
            if(shelf.items[i] != null) {
                this.items[i] = new Item(shelf.items[i]);
                this.size++;
            }
        }
    }

    /**
     * Adds an item to the shelf.
     * If the internal array is full, it is resized before insertion.
     *
     * @param item the item to add to the shelf
     */
    public void addItem(Item item) {
        // Resize the array if it is full
        if (this.size == this.items.length) {
            this.items = resizeItemArray(this.items, this.items.length * Constants.SIZE_MULTIPLIER);
        }

        // Insert item and increment the size
        this.items[this.size++] = item;
    }

    /**
     * Removes an item from the shelf using a user-facing 1-based index.
     *
     * @param userIndex index entered by the user (1-based)
     * @return the removed item, or {@code null} if the index is invalid
     */
    public Item removeItemByUserIndex(int userIndex) {
        // Convert user input to zero-based index
        int index = userIndex - 1;

        // Validate index
        if (index < 0 || index >= this.size) {
            return null;
        }

        // Store the item to return
        Item removed = new Item(this.items[index]); //IMP_NOTE: copy constructor to avoid privacy leaks

        // Shift remaining items left to fill the gap
        shiftLeft(index);

        // Decrease size and clear last reference
        this.size--;
        this.items[this.size] = null;

        return removed;
    }

    /**
     * Returns a snapshot copy of the items currently on the shelf.
     *
     * This method prevents external classes from modifying
     * the internal array directly (privacy-safe design).
     *
     * @return a new array containing the current items
     */
    public Item[] getItemsSnapshot() {
        Item[] snapshot = new Item[this.size];

        // Copy only valid items into the snapshot
        for (int i = 0; i < this.size; i++) {
            snapshot[i] = new Item(this.items[i]); //IMP_NOTE: copy constructor to avoid privacy leaks
        }

        return snapshot;
    }

    /**
     * Returns the number of items currently on the shelf.
     *
     * @return item count
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Resizes the internal item array to a new capacity.
     *
     * @param oldArray the original array
     * @param newSize the new desired capacity
     * @return the resized array
     */
    private Item[] resizeItemArray(Item[] oldArray, int newSize) {
        Item[] newArray = new Item[newSize];

        // Copy existing elements into the new array
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

        return newArray;
    }

    /**
     * Shifts all items one position to the left starting from a given index.
     * This is used after an item is removed to maintain array continuity.
     *
     * @param fromIndex index from which shifting begins
     */
    private void shiftLeft(int fromIndex) {
        for (int i = fromIndex; i < this.size - 1; i++) {
            this.items[i] = this.items[i + 1];
        }
    }
}
