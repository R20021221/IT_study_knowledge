/**
 * Student Name - Guancheng Rong
 * Student Id - 1856981
 * Student email - guancheng.rong@student.unimelb.edu.au
 * AI Usage Declaration -
 */

public class ForkPosition {

    private int row;
    private int col;
    private String carriedItem;

    private int moves; // Forklift move counter. / 叉车移动次数计数。
    private int hits; // Forklift blocked-move counter. / 叉车受阻次数计数。

    public void initialize() { // Reset forklift state to START. / 将叉车状态重置到起点。
        int r = Constants.START_ROW;
        int c = Constants.START_COL;

        this.moves = 0;
        this.hits = 0;
        this.carriedItem = null;
        this.row = r;
        this.col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getCarriedItem() {
        return carriedItem;
    }

    public int getMoves() {
        return moves;
    }

    public int getHits() {
        return hits;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setCarriedItem(String item) {
        this.carriedItem = item;
    }

    public void incrementMoves() {
        this.moves++;
    }

    public void incrementHits() {
        this.hits++;
    }
}
