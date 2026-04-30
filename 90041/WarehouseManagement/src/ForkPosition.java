public class ForkPosition {

    private int F_row;
    private int F_col;

    public ForkPosition(int F_row, int F_col) {
        this.F_row = F_row;
        this.F_col = F_col;
        initialize();

    }

    private void initialize() {
        F_row = 1;
        F_col = 1;
    }

    private void move(String input, int F_row, int F_col){

        switch(input){
            case "U":
                F_col = F_col + 1;
                break;
            case "D":
                F_col = F_col - 1;
                break;
            case "R":
                F_row = F_row + 1;
                break;
            case "L":
                F_row = F_row - 1;
                break;
            case "T":
                F_row = 1;
                F_col = 1;
                break;
        }

    }



}
