import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Color;
//import java.awt.event.*; //imports Event package which listens for button press
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.*;


public class Listener extends JPanel {

    static ArrayList<Square> squares = new ArrayList<Square>();
    static Square clickedSquare;
    
    public Listener(Square square) {
        Border blackline = BorderFactory.createLineBorder(Color.black);
       
        if((square.getX()+square.getY())%2==0){
            this.setBackground( Color.white);  
        }
        else{
            this.setBackground( Color.gray);
        }

        this.setBounds(square.getX()*1000/8, square.getY()*1000/8, 1000/8, 1000/8);//New. 
        this.setBorder(blackline);
    
        addMouseListener(new MouseAdapter() { 
            @Override
            public void mousePressed(MouseEvent me) { 
                Board.removeCircles();
                if(Pieces.board[square.getX()][square.getY()] != null && Pieces.board[square.getX()][square.getY()].color == Main.curTurn){
                    squares = Pieces.movement(square);
                    clickedSquare = square;
                    for(Square s: squares){
                        if(Pieces.board[s.getX()][s.getY()]==null){
                        Board.displayPossibleMoves(s);
                    }else{
                        
                    }
                        System.out.println(s.toString());
                    }
                }else{
                    
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
                            
                            
    
                            //if king moved test if it was a castle and move the rook too
                            if(type.charAt(2)=='n'){
                                
                                if(Pieces.board[clickedSquare.getX()][clickedSquare.getY()].color == PColor.BLACK &&(Pieces.blackCanCastleLong||Pieces.blackCanCastleShort)){
                                    Board.castle(color, clickedSquare, square);
                                }
                                if(Pieces.board[clickedSquare.getX()][clickedSquare.getY()].color == PColor.WHITE &&(Pieces.whiteCanCastleLong||Pieces.whiteCanCastleShort)){
                                    Board.castle(color, clickedSquare, square);
                                }
                              
                            }
                            //switch turns and update move order
                            if(Main.curTurn == PColor.WHITE){
                                Main.curTurn = PColor.BLACK;
                                Board.addToMoveOrder(PColor.WHITE, type, s,clickedSquare);
                               
                            }else{
                                Main.curTurn = PColor.WHITE;
                                Board.addToMoveOrder(PColor.BLACK, type, s,clickedSquare);
                            }
                           //reset things
                            Pieces.board[clickedSquare.getX()][clickedSquare.getY()] = null;
                            squares = new ArrayList<Square>();
                            break;  

                        }
                    }
                }
                Board.updateBoard();
            }
        });

    }

}