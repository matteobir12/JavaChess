import java.util.ArrayList;
import javax.swing.*;

public class OnMouseClick {
    static Square lastClickedSquare;
    static boolean waitingForPromotion = false;
    public static void click(Square square){

        if(waitingForPromotion)return;
        

        Board.removeCircles(); //deletes everything in circBoxes
        // if they clicked on their own piece
        if (Pieces.board[square.getX()][square.getY()] != null && Pieces.board[square.getX()][square.getY()].getColor() == Main.curTurn) {
            
            lastClickedSquare = square;
            for(Square s: Pieces.board[square.getX()][square.getY()].getPossibleMoves()){
                if(Pieces.board[s.getX()][s.getY()]==null){
                    Board.displayPossibleMoves(s);
                }else{
                //TODO some kind of takes indicator
                }
            
            }
        }else{ //if they didnt click on their piece they must have clicked an enemy piece or a free square
            
             if(lastClickedSquare !=null && Pieces.board[lastClickedSquare.getX()][lastClickedSquare.getY()]!=null) for(Square s: Pieces.board[lastClickedSquare.getX()][lastClickedSquare.getY()].getPossibleMoves()){
                if (square.equals(s)){
                    // delete old piece(s) location
                    Board.circBoxes.add(Pieces.board[lastClickedSquare.getX()][lastClickedSquare.getY()].getLabel());
                    if(Pieces.board[square.getX()][square.getY()] != null){
                        Board.circBoxes.add(Pieces.board[square.getX()][square.getY()].getLabel()); 
                        Board.ex = true;
                    }
                    Board.removeCircles();
                    
                    // change board in pieces clickedSquare is old square s is new square
                    String string = "./assets/";
                    String color = Pieces.board[lastClickedSquare.getX()][lastClickedSquare.getY()].getColor().toString().toLowerCase();////
                    Board.afterMove(lastClickedSquare, s, color);
                    String type = Pieces.board[lastClickedSquare.getX()][lastClickedSquare.getY()].getType().toString().toLowerCase();
                    JLabel l = new JLabel(new ImageIcon(string+color+"_"+type+".png"));
                    Pieces.board[s.getX()][s.getY()] = Pieces.board[lastClickedSquare.getX()][lastClickedSquare.getY()];
                    Pieces.board[s.getX()][s.getY()].setLabel(l);
                    Board.boardPanel[s.getX()][s.getY()].add(l);

                    //reset things
                    Pieces.board[lastClickedSquare.getX()][lastClickedSquare.getY()] = null;
                    
                    //switch turns and update move order
                    Pieces.pinned = new ArrayList<>();
                    Pieces.pinners = new ArrayList<>();
                    if(Pieces.board[s.getX()][s.getY()].getType() == PType.PAWN && (s.getY()==7||s.getY()==0)){
                        lastClickedSquare = s;
                        waitingForPromotion = true;
                        Board.createPieceSelectorJPanel(Main.curTurn,true);
                    }else{
                        Pieces.switchAndPrepareForNextTurn(Main.curTurn, Pieces.board[s.getX()][s.getY()].getType(), s, lastClickedSquare);
                        lastClickedSquare = null;
                    }
                    
                    break;  

                }
            }
        }
        Board.updateBoard();    
    }
    public static void setPromotionPiece(Piece promotionPiece) {
        Board.circBoxes.add(Pieces.board[lastClickedSquare.getX()][lastClickedSquare.getY()].getLabel());
        Pieces.board[lastClickedSquare.getX()][lastClickedSquare.getY()] = promotionPiece;
        Board.boardPanel[lastClickedSquare.getX()][lastClickedSquare.getY()].add(promotionPiece.getLabel());
        waitingForPromotion = false;
        Pieces.switchAndPrepareForNextTurn(Main.curTurn, PType.PAWN, lastClickedSquare, lastClickedSquare);
        lastClickedSquare = null;
        Board.removePieceSelectorPanel();
        Board.removeCircles();
        Board.updateBoard();
    }
}
