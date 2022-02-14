import java.util.ArrayList;
import javax.swing.*;

public class OnMouseClick {
    static Square lastClickedSquare;
    static boolean waitingForPromotion = false;
    public static void click(Square square){

        if(waitingForPromotion)return;
        

        Board.removeCircles(); //deletes everything in circBoxes
        // if they clicked on their own piece
        if (Pieces.getBoardSquare(square) != null && Pieces.getBoardSquare(square).getColor() == Main.curTurn) {
            
            lastClickedSquare = square;
            for(Square s: Pieces.getBoardSquare(square).getPossibleMoves()){
                if(Pieces.getBoardSquare(s)==null){
                    Board.displayPossibleMoves(s);
                }else{
                //TODO some kind of takes indicator
                }
            
            }
        }else{ //if they didnt click on their piece they must have clicked an enemy piece or a free square
            
             if(lastClickedSquare !=null && Pieces.getBoardSquare(lastClickedSquare)!=null) for(Square s: Pieces.getBoardSquare(lastClickedSquare).getPossibleMoves()){
                if (square.equals(s)){
                    // delete old piece(s) location
                    Board.circBoxes.add(Pieces.getBoardSquare(lastClickedSquare).getLabel());
                    if(Pieces.getBoardSquare(square) != null){
                        Board.circBoxes.add(Pieces.getBoardSquare(square).getLabel()); 
                        Board.ex = true;
                    }
                    Board.removeCircles();
                    
                    // change board in pieces clickedSquare is old square s is new square
                    String string = "./assets/";
                    String color = Pieces.getBoardSquare(lastClickedSquare).getColor().toString().toLowerCase();////
                    Board.afterMove(lastClickedSquare, s, color);
                    String type = Pieces.getBoardSquare(lastClickedSquare).getType().toString().toLowerCase();
                    JLabel l = new JLabel(new ImageIcon(string+color+"_"+type+".png"));
                    Pieces.setBoardSquare(s, Pieces.getBoardSquare(lastClickedSquare));
                    Pieces.getBoardSquare(s).setLabel(l);
                    Board.boardPanel[s.getX()][s.getY()].add(l);

                    //reset things
                    Pieces.setBoardSquare(lastClickedSquare, null);
                    
                    //switch turns and update move order
                    Pieces.pinned = new ArrayList<>();
                    Pieces.pinners = new ArrayList<>();
                    if(Pieces.getBoardSquare(s).getType() == PType.PAWN && (s.getY()==7||s.getY()==0)){
                        lastClickedSquare = s;
                        waitingForPromotion = true;
                        Board.createPieceSelectorJPanel(Main.curTurn,true);
                    }else{
                        Pieces.switchAndPrepareForNextTurn(Main.curTurn, Pieces.getBoardSquare(s).getType(), s, lastClickedSquare);
                        lastClickedSquare = null;
                    }
                    
                    break;  

                }
            }
        }
        Board.updateBoard();    
    }
    public static void setPromotionPiece(Piece promotionPiece) {
        Board.circBoxes.add(Pieces.getBoardSquare(lastClickedSquare).getLabel());
        Pieces.setBoardSquare(lastClickedSquare, promotionPiece);
        Board.boardPanel[lastClickedSquare.getX()][lastClickedSquare.getY()].add(promotionPiece.getLabel());
        waitingForPromotion = false;
        Pieces.switchAndPrepareForNextTurn(Main.curTurn, PType.PAWN, lastClickedSquare, lastClickedSquare);
        lastClickedSquare = null;
        Board.removePieceSelectorPanel();
        Board.removeCircles();
        Board.updateBoard();
    }
}
