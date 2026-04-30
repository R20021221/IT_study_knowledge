public class ForkPosition {

    public int F_row;
    public int F_col;
    private String F_order;

    private WarehouseMap map;

    public void setF_order(String order) {
        this.F_order = order;
    }

    public void initialize(){ // initialize the fork's position
        int r = 1;
        int c = 1;

        this.F_row = r;
        this.F_col = c;
    }

    private void printPosition(int r, int c){
        System.out.printf("(%d,%d)", r, c);
    }

    public void movement(int r, int c, String order){
        switch(order){
            case "U": // go up
                c += 1;
                if(map.check(r, c)){
                    break;
                }
                else{
                    c -= 1;
                    break;
                }

            case "D":
                c -= 1;
                if(map.check(r, c)){
                    break;
                }
                else{
                    c += 1;
                    break;
                }

            case "L":
                r -= 1;
                if(map.check(r, c)){
                    break;
                }
                else{
                    r += 1;
                    break;
                }

            case "R":
                r += 1;
                if(map.check(r, c)){
                    break;
                }
                else{
                    r -= 1;
                    break;
                }

        }
    }
}
