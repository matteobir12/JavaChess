import javax.swing.*; //imports Swing package which creates form and button
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.Container;
import java.io.FileNotFoundException;
import java.io.PrintWriter;



public class Board {
    static int h = 1000;
    static int l = 1000;
    static int moveNumb = 1;
    static boolean ex = false;
    static boolean didCastleShort, didCastleLong = false;
    static JPanel[][] boardPanel = new JPanel[8][8];
    static JFrame frame;
    static JPanel jp;
    static JTextArea textLabel;
    static ArrayList<JLabel> circBoxes = new ArrayList<>();
    static String moveOrder="";
    static Piece enpassant;
    static ArrayList<Square> kingThreats;
    static Square wKingSquare;
    static Square bKingSquare;

    public static void makeNewBoard(){
        frame = new JFrame();
        jp = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ends program when JFrame closed

        

        frame.setSize(h+100,l+100); //pixel size of frame in width then height
       
        jp.setLayout(null);//Out.  Back in.
        jp.setBackground(new java.awt.Color(100, 255, 100));
        frame.add(jp); 

        //Move order to txt button
        JButton b = new JButton("Save mv Order");  
        b.setBounds(l +50,h-50,115,30);  
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){  
                try (PrintWriter out = new PrintWriter("pgn.txt")) {
                    out.println(moveOrder);
                }catch (FileNotFoundException x){
                    System.out.println("no file");
                }
            }  
        });
        jp.add(b);  
        //move order jp
        JPanel moveOrderJP = new JPanel();
        moveOrderJP.setBounds(l+50, h-300, 215, 200);
        textLabel = new JTextArea( "", 6, 20);
        textLabel.setLineWrap(true);
        textLabel.setWrapStyleWord(true);
        textLabel.setOpaque(false);
        textLabel.setEditable(false);

        moveOrderJP.add(textLabel);
        
        //textLabel.setFont(new Font("Verdana",1,20));
        moveOrderJP.add(textLabel);
        jp.add(moveOrderJP);


        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                JPanel rectangle = new Listener(new Square(i,j));
                
                boardPanel[i][j] = rectangle;
                jp.add(rectangle);
                
            }
        }

        frame.setVisible(true); //if false then frame will be invisible
    }

    public static void addStartingPosition(){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(Pieces.board[j][i] == null){continue;}
                String s = "./assets/";
                String color = Pieces.board[j][i].getColor().toString().toLowerCase();
                String type = Pieces.board[j][i].getType().toString().toLowerCase();
                JLabel l = new JLabel(new ImageIcon(s + color + "_" + type + ".png"));
                boardPanel[j][i].add(l);
                Pieces.board[j][i].setLabel(l);
                
            }
        }
    }

    public static void updateBoard(){
        
       // SwingUtilities.updateComponentTreeUI(frame);
        frame.invalidate();
        frame.validate();
        frame.repaint();
        

    }
    public static void removeCircles(){
        for(JLabel s: circBoxes){
           
            Container parent = s.getParent();
            if(parent!=null){
            parent.remove(s);}
        }
        circBoxes = new ArrayList<>();

}
    public static void displayPossibleMoves(Square s){
      
        JLabel circle = new JLabel(new ImageIcon("./assets/Circle.png"));
        boardPanel[s.getX()][s.getY()].add(circle);
        circBoxes.add(circle);
        updateBoard();
    }


    public static void addToMoveOrder(PColor c,String type,Square s,Square clickedSquare){
        if(c ==PColor.WHITE ){
            moveOrder += moveNumb + ".";
        
        }else{
            moveNumb++;
           
        }
        if(didCastleShort) {
            moveOrder += "O-O ";
            didCastleShort = false;
            return;
        }
        if(didCastleLong) {
            moveOrder += "O-O-O ";
            didCastleLong = false; 
            return;
        }
        char x = (char)(s.getX()+97);
        char y = (char)((8-s.getY())+48);
        //not a pawn
        if(type.charAt(0)!='p') {
           if(type.equals("knight")) {
                moveOrder += "n";
                otherPieceCanMove( s,  clickedSquare);
           }else{
           moveOrder += type.charAt(0);
            }
            if(type.charAt(0)=='r') {
                otherPieceCanMove( s,  clickedSquare);
            }
            //was it takes?
           if(ex) {
            moveOrder += "x";
            ex = false;
            }
        //location of move
        moveOrder += ""+x+y;
        }else{
            //takes?
            if(s.getX()!=clickedSquare.getX()){
                moveOrder += ""+(char)(clickedSquare.getX()+97)+'x';
            }
            moveOrder += ""+x+y;
        }
    moveOrder+=' ';
    textLabel.setText(moveOrder);
    }

    private static void otherPieceCanMove(Square destinationSquare, Square originalSquare){
        ArrayList<Square> moveable = Pieces.movement(destinationSquare);
        for(Square s: moveable){
            if(s == null || Pieces.board[s.getX()][s.getY()] == null || s.equals(originalSquare)){
                continue;
            }
            if(Pieces.board[s.getX()][s.getY()].getType() == Pieces.board[destinationSquare.getX()][destinationSquare.getY()].getType() && Pieces.board[s.getX()][s.getY()].getColor() == Pieces.board[destinationSquare.getX()][destinationSquare.getY()].getColor()){
                if(s.getX()==destinationSquare.getX()) {
                    char y = (char)((8-originalSquare.getY())+48);
                    moveOrder+= y;
                }else{
                    char x = (char)(originalSquare.getX()+97);
                    moveOrder+=x;
                }
                break;
            }
        }
       
        
    }
    public static void afterMove(Square pieceOldSquare, Square pieceNewSquare, String color){
        //if king moved test if it was a castle and move the rook too. also update the king square.
        if(Pieces.board[pieceOldSquare.getX()][pieceOldSquare.getY()].getType()==PType.KING){
            if(Pieces.board[pieceOldSquare.getX()][pieceOldSquare.getY()].getColor() == PColor.BLACK){
                bKingSquare = new Square(pieceNewSquare.getX(), pieceNewSquare.getY());
                if (Pieces.blackCanCastleLong||Pieces.blackCanCastleShort){
                    castle(color, pieceOldSquare, pieceNewSquare);
                }
            }
            if(Pieces.board[pieceOldSquare.getX()][pieceOldSquare.getY()].getColor() == PColor.WHITE ){
                wKingSquare = new Square(pieceNewSquare.getX(), pieceNewSquare.getY());
                if(Pieces.whiteCanCastleLong||Pieces.whiteCanCastleShort){
                    castle(color, pieceOldSquare, pieceNewSquare);
                }
            }
        } 
        //if a rook was moved check if we want to stop castling
        if (Pieces.board[pieceOldSquare.getX()][pieceOldSquare.getY()].getType() == PType.ROOK){
            noCastle(pieceOldSquare);
        }
        // if we just enpassented
        if(Pieces.board[pieceOldSquare.getX()][pieceOldSquare.getY()].getType() == PType.PAWN && pieceOldSquare.getX()!=pieceNewSquare.getX() && Pieces.board[pieceNewSquare.getX()][pieceNewSquare.getY()] == null){
            Container parent = Pieces.board[pieceNewSquare.getX()][pieceOldSquare.getY()].getLabel().getParent();
            if(parent!=null){
                parent.remove(Pieces.board[pieceNewSquare.getX()][pieceOldSquare.getY()].getLabel());
            }
            //remove pawn
            Pieces.board[pieceNewSquare.getX()][pieceOldSquare.getY()] = null;
        }
        Pieces.setEnPassentable(null);
        if (Pieces.board[pieceOldSquare.getX()][pieceOldSquare.getY()].getType() == PType.PAWN && (pieceNewSquare.getY()==pieceOldSquare.getY() + 2 || pieceNewSquare.getY()==pieceOldSquare.getY() - 2)){
            System.out.println(pieceNewSquare.toString() + " pessanable");
            Pieces.setEnPassentable(pieceNewSquare);
        }
    }

    static void castle(String color,Square clickedSquare,Square square) {
        String string = "./assets/";
        JLabel lab = new JLabel(new ImageIcon(string+color+"_rook.png"));
        if(clickedSquare.getX()-square.getX()==2){
            //queenside
            didCastleLong = true;
            Pieces.board[3][square.getY()] = Pieces.board[0][square.getY()];
            Container parent = Pieces.board[0][square.getY()].getLabel().getParent();
            
            if(parent!=null){
                parent.remove(Pieces.board[0][square.getY()].getLabel());
            }
            Pieces.board[3][square.getY()].setLabel(lab);
            Board.boardPanel[3][square.getY()].add(lab);

            //remove rook
            
            Pieces.board[0][square.getY()] = null;
        }
        if(clickedSquare.getX()-square.getX()==-2) {
            //kingside
            didCastleShort = true;
            Pieces.board[5][square.getY()] = Pieces.board[7][square.getY()];
            Container parent = Pieces.board[7][square.getY()].getLabel().getParent();
            if(parent!=null) {
                parent.remove(Pieces.board[7][square.getY()].getLabel());
            }
            Pieces.board[5][square.getY()].setLabel(lab);
            Board.boardPanel[5][square.getY()].add(lab);
            Pieces.board[7][square.getY()] = null;
        }
    }
    private static void noCastle(Square square){
        if(square.getX() == 0){
            if(Pieces.board[square.getX()][square.getY()].getColor() == PColor.WHITE){
                Pieces.whiteCanCastleLong = false;
            }else{
                Pieces.blackCanCastleLong = false;
            }
        }
        if(square.getX()==7){
            if(Pieces.board[square.getX()][square.getY()].getColor() == PColor.WHITE){
                Pieces.whiteCanCastleShort = false;
            }else{
                Pieces.blackCanCastleShort = false;
            }
        }
    }

}
  