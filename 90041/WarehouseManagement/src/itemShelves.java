public class itemShelves {

    public String[] Item;

    private int row;
    private int col;
    private int itemCount;

    public itemShelves(int r, int c, int i){
        this.row = r;
        this.col = c;
        this.itemCount = i;

        Item = new String[itemCount];
    }




}
