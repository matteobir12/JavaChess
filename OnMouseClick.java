import java.util.ArrayList;
import javax.swing.*;
import java.util.Iterator;

public class OnMouseClick {
    static Square clickedSquare;
    static ArrayList<Square> squares;
    static ArrayList<Square> threats;
    static ArrayList<Square> pinned = new ArrayList<>();
    static ArrayList<Square> pinners = new ArrayList<>();
    public static void click(Square square){
        
        Board.removeCircles(); //deletes everything in circBoxes

        // if they clicked on their own piece
        if (Pieces.board[square.getX()][square.getY()] != null && Pieces.board[square.getX()][square.getY()].getColor() == Main.curTurn) {
            squares = Pieces.board[square.getX()][square.getY()].getPossibleMoves();
            //if the king is under attack
            if (threats!=null && !threats.isEmpty() && Pieces.board[square.getX()][square.getY()].getType() != PType.KING) {
                //by one attacker
                if (threats.size()==1){
                    Square curking;
                    if(Main.curTurn == PColor.BLACK) {
                        curking = Board.bKingSquare;
                    } else {
                    curking = Board.wKingSquare;
                }
                boolean isKnight = (Pieces.board[threats.get(0).getX()][threats.get(0).getY()].getType()==PType.KNIGHT);
                Iterator<Square> iter = squares.iterator(); 
                while(iter.hasNext()){
                    Square tmpS = iter.next();
                    // if it can take the piece that put it in check or that piece blocks the check
                    if(tmpS.equals(threats.get(0)) || (!isKnight && Pieces.pieceBlocks(curking,threats.get(0),tmpS))){
                        continue;
                    }
                    iter.remove();
                }
                }else{
                    if(!square.equals(Board.bKingSquare) && !square.equals(Board.wKingSquare)){
                        for(int i = 0; i<squares.size(); i++){
                            squares.remove(i);
                        }
                    }
                }
                
            }
            //is a piece pinned?
            if(pinned != null && !pinned.isEmpty()){
                for(int i=0; i<pinned.size();i++){
                    //is it the piece they clicked
                    if(square.equals(pinned.get(i))){
                        Square curking;
                        if(Main.curTurn == PColor.BLACK){
                            curking = Board.bKingSquare;
                        }else{
                            curking = Board.wKingSquare;
                        }       
                        Iterator<Square> iter = squares.iterator(); 
                        while(iter.hasNext()){
                            Square tmpS = iter.next();
                            //if the move doesnt break the pin
                            if(Pieces.pieceBlocks(curking, pinners.get(i), tmpS) || tmpS.equals(pinners.get(i))){
                                continue;
                        }
                        iter.remove();
                        }   
                    }
                }
            }
            clickedSquare = square;
            for(Square s: squares){
                if(Pieces.board[s.getX()][s.getY()]==null){
                    Board.displayPossibleMoves(s);
            }else{
                
            }
            
            }
        }else{ //if they didnt click on their piece they must have clicked an enemy piece or a free square
            
             if(squares != null) for(Square s: squares){
                
                if (square.equals(s)){
                    // delete old piece(s) location
                    Board.circBoxes.add(Pieces.board[clickedSquare.getX()][clickedSquare.getY()].getLabel());
                    if(Pieces.board[square.getX()][square.getY()]!=null){
                        Board.circBoxes.add(Pieces.board[square.getX()][square.getY()].getLabel()); 
                        Board.ex = true;
                    }
                    Board.removeCircles();
                    
                    // change board in pieces clickedSquare is old square s is new square
                    String string = "./assets/";
                    String color = Pieces.board[clickedSquare.getX()][clickedSquare.getY()].getColor().toString().toLowerCase();////
                    Board.afterMove(clickedSquare, s, color);
                    String type = Pieces.board[clickedSquare.getX()][clickedSquare.getY()].getType().toString().toLowerCase();
                    JLabel l = new JLabel(new ImageIcon(string+color+"_"+type+".png"));
                    Pieces.board[s.getX()][s.getY()] = Pieces.board[clickedSquare.getX()][clickedSquare.getY()];
                    Pieces.board[s.getX()][s.getY()].setLabel(l);
                    Board.boardPanel[s.getX()][s.getY()].add(l);

                    //reset things
                    Pieces.board[clickedSquare.getX()][clickedSquare.getY()] = null;
                    squares = new ArrayList<>();
                    
                    //switch turns and update move order
                    pinned = new ArrayList<>();
                    pinners = new ArrayList<>();
                    Pieces.switchAndPrepareForNextTurn(Main.curTurn, type, s, clickedSquare);
                    
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
}
