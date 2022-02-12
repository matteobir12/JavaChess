import java.util.ArrayList;
import javax.swing.*;

public class OnMouseClick {
    static Square lastClickedSquare;
    static boolean waitingForPromotion = false;
    private static Piece promotionPiece;
    public static void click(Square square){
        
        Board.removeCircles(); //deletes everything in circBoxes
        if(waitingForPromotion) {
            if(promotionPiece != null){
                Pieces.board[lastClickedSquare.getX()][lastClickedSquare.getY()] = promotionPiece;
                Board.boardPanel[lastClickedSquare.getX()][lastClickedSquare.getY()].add(promotionPiece.getLabel());
                waitingForPromotion = false;
                promotionPiece = null;
            }else return;
        } 

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
                    lastClickedSquare = s;
                    Pieces.switchAndPrepareForNextTurn(Main.curTurn, type, s, lastClickedSquare);
                    
                    // System.out.println("white can castle long " + Pieces.whiteCanCastleLong);
                    // System.out.println("white can castle short " + Pieces.whiteCanCastleShort);
                    // System.out.println("black can castle long " + Pieces.blackCanCastleLong);
                    // System.out.println("black can castle short " + Pieces.blackCanCastleShort);

                    break;  

                }
            }
        }
        Board.updateBoard();    
    }
    public static void setPromotionPiece(Piece promotionPiece) {
        OnMouseClick.promotionPiece = promotionPiece;
    }
}
