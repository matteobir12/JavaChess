import java.util.ArrayList;
import javax.swing.*;
import java.util.Iterator;

public class OnMouseClick {
     static Square clickedSquare;
    static ArrayList<Square> squares;
    static ArrayList<Square> threats,pinned,attackers;
    public static void click(Square square){
        
        Board.removeCircles(); //deletes everything in circBoxes

        // if they clicked on their own piece
        if (Pieces.board[square.getX()][square.getY()] != null && Pieces.board[square.getX()][square.getY()].color == Main.curTurn) {
            squares = Pieces.movement(square);
            //if the king is under attack
            if (threats!=null && !threats.isEmpty() && Pieces.board[square.getX()][square.getY()].type != PType.KING) {
                //by one attacker
               if (threats.size()==1){

                   Square curking;
                   if(Main.curTurn == PColor.BLACK) {
                       curking = Board.bkinSquare;
                   } else {
                curking = Board.wKingSquare;
                    }
                boolean isKnight = (Pieces.board[threats.get(0).getX()][threats.get(0).getY()].type==PType.KNIGHT);
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
                    if(!square.equals(Board.bkinSquare) && !square.equals(Board.wKingSquare)){
                        for(int i = 0; i<squares.size(); i++){
                            squares.remove(i);
                        }
                    }
                }
                
            }
            //is a piece pinned?
            if(pinned != null && !pinned.isEmpty()){
                System.out.println("there is a pinned piece");
                for(int i=0; i<pinned.size();i++){
                    //is it the piece they clicked
                    if(square.equals(pinned.get(i))){
                        System.out.println("I am the pinned piece");
                        Square curking;
                         if(Main.curTurn == PColor.BLACK){
                            curking = Board.bkinSquare;
                        }else{
                            curking = Board.wKingSquare;
                             }       
                        Iterator<Square> iter = squares.iterator(); 
                        while(iter.hasNext()){
                        System.out.println("what can I do");
                        Square tmpS = iter.next();
                        //if the move doesnt break the pin
                        if(Pieces.pieceBlocks(curking, attackers.get(i), tmpS) || tmpS.equals(attackers.get(i))){
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
            
            for(Square s: squares){
                
                if (square.equals(s)){
                    Board.circBoxes.add(Pieces.board[clickedSquare.getX()][clickedSquare.getY()].label);
                    if(Pieces.board[square.getX()][square.getY()]!=null){
                        Board.circBoxes.add(Pieces.board[square.getX()][square.getY()].label); 
                        Board.ex = true;
                    }
                    Board.removeCircles();

                    // change board in pieces
                    String string = "./assets/";
                    String color = Pieces.board[clickedSquare.getX()][clickedSquare.getY()].color.toString().toLowerCase();////
                    String type = Pieces.board[clickedSquare.getX()][clickedSquare.getY()].type.toString().toLowerCase();
                    JLabel l = new JLabel(new ImageIcon(string+color+"_"+type+".png"));
                    Pieces.board[s.getX()][s.getY()] = Pieces.board[clickedSquare.getX()][clickedSquare.getY()];
                    Pieces.board[s.getX()][s.getY()].label = l;
                    Board.boardPanel[s.getX()][s.getY()].add(l);
                    
                    

                    //if king moved test if it was a castle and move the rook too. also update the king square.
                    if(type.charAt(2)=='n'){
                        
                        if(Pieces.board[clickedSquare.getX()][clickedSquare.getY()].color == PColor.BLACK){
                        Board.bkinSquare = new Square(clickedSquare.getX(), clickedSquare.getY());
                        if (Pieces.blackCanCastleLong||Pieces.blackCanCastleShort){
                            Board.castle(color, clickedSquare, square);
                        }
                    }
                        if(Pieces.board[clickedSquare.getX()][clickedSquare.getY()].color == PColor.WHITE ){
                            Board.bkinSquare = new Square(clickedSquare.getX(), clickedSquare.getY());
                            if(Pieces.whiteCanCastleLong||Pieces.whiteCanCastleShort){
                                Board.castle(color, clickedSquare, square);
                            }
                        }
                    }
                    //switch turns and update move order
                    pinned = null;
                    attackers = null;
                    if(Main.curTurn == PColor.WHITE){
                        Main.curTurn = PColor.BLACK;
                        Board.addToMoveOrder(PColor.WHITE, type, s,clickedSquare);
                        //king in check?
                         threats = Pieces.threatsToPiece(PColor.BLACK, Board.bkinSquare);
                        for(Square t:threats) {
                            System.out.println(t.toString());
                        }
                    }else{
                        Main.curTurn = PColor.WHITE;
                        Board.addToMoveOrder(PColor.BLACK, type, s,clickedSquare);
                        //king in check?
                        threats = Pieces.threatsToPiece(PColor.WHITE, Board.wKingSquare);
                        for(Square t:threats){
                            System.out.println(t.toString());
                        }
                    }
                   //reset things
                    Pieces.board[clickedSquare.getX()][clickedSquare.getY()] = null;
                    squares = new ArrayList<>();
                    
                    System.out.println("white can castle long " + Pieces.whiteCanCastleLong);
                    System.out.println("white can castle short " + Pieces.whiteCanCastleShort);
                    System.out.println("black can castle long " + Pieces.blackCanCastleLong);
                    System.out.println("black can castle short " + Pieces.blackCanCastleShort);

                    break;  

                }
            }
        }
        Board.updateBoard();
    
    }
}
