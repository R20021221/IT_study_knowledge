public class itemShelves {

    public String[] Item;

    public int row;
    public int col;
    public int itemCount;
    public boolean visited;

    public itemShelves(int r, int c, int i){
        this.row = r;
        this.col = c;
        this.itemCount = i; // Max 4
        this.visited = false;

        Item = new String[itemCount];
    }

    //Shelf Menu:
    //Press V to view items.
    //Press P to pick an item.
    //Press Q to exit shelf menu.
    //>



}
