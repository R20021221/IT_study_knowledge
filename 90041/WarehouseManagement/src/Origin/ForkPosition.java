public class ForkPosition {

    public int F_row;
    public int F_col;
    public String F_item;

    public int F_moves; //move__counter only for the fork
    public int F_hits; //hit__counter only for the fork




    public void initialize(){ // initialize the fork's position
        int r = 1;
        int c = 1;

        this.F_moves = 0;
        this.F_hits = 0;
        this.F_item = null;
        this.F_row = r;
        this.F_col = c;
    }


}
