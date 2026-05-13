/**
 * Represents an item stored on a shelf or carried by the forklift.
 */
public class Item {

    private  String name;

    /**
     * Creates an item.
     *
     * @param name item name
     */
    public Item(String name) {
        this.name = name;
    }

    /**
     * Copy constructor for Item
     * @param item Item object to copy from
     */
    public Item(Item item) {
        if(item!= null) {
            this.name = item.name;
        }
    }

    public String getName() { return this.name; } //IMP_NOTE:Returning Strings doesnt cause privacy leaks, strings are immutable

    @Override
    public String toString() {
        return this.name; //IMP_NOTE:Returning Strings doesnt cause privacy leaks, strings are immutable
    }
}
