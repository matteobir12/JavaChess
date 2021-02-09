public class Square {
    private int y;
    private int x;

    public Square(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getY(){
        return y;
    }
    public int getX(){
        return x;
    }

    @Override
    public String toString(){
        return "" + x + ", " + y;
    }
    @Override
    public boolean equals(Object o){
        Square s = (Square) o;
        if(this.x == s.getX() && this.y == s.getY()){
            return true;
        }
        return false;
    }

}
