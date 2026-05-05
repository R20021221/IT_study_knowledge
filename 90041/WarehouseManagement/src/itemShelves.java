/**
 * Student Name - Guancheng Rong
 * Student Id - 1856981
 * Student email - guancheng.rong@student.unimelb.edu.au
 * AI Usage Declaration -
 */

public class ItemShelves {

    private String[] items;

    private int row;
    private int col;
    private int itemCount;
    private boolean visited;

    public ItemShelves(int row, int col, int itemCount) {
        this.row = row;
        this.col = col;
        this.itemCount = itemCount; // Maximum initial shelf capacity is 4. / 货架初始最大容量为 4。
        this.visited = false;

        items = new String[itemCount];
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getItemCount() {
        return itemCount;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public String getItem(int index) {
        return items[index];
    }

    public void setItem(int index, String item) {
        this.items[index] = item;
    }

    public void decrementItemCount() {
        this.itemCount--;
    }

    public String[] getItems() {
        String[] copy = new String[itemCount];
        for (int i = 0; i < itemCount; i++) {
            copy[i] = items[i];
        }
        return copy;
    }
}
