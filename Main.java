public class Main {

    static PColor curTurn = PColor.WHITE;

    public static void main(String[] args) {
        
       
        Pieces.initBoard();
        Board.makeNewBoard();
        Board.addStartingPosition();
        Board.updateBoard();


    }

}