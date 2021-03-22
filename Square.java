public class Square {
    private int y;
    private int x;

    public Square(int x, int y){
        this.x = x;
        this.y = y;
    }
    Square(){
        x =0;
        y=0;
    }

    public int getY(){
        return y;
    }
    public int getX(){
        return x;
    }
     public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString(){
        return "" + x + ", " + y;
    }
    @Override
    public boolean equals(Object o){
        Square s = (Square) o;
        return (this.x == s.getX() && this.y == s.getY());
    }
    public void copy(Square s){
        s.setX(x);
        s.setY(y);
    }

}
