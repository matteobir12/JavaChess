import javax.swing.*; //imports Swing package which creates form and button
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.Container;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.awt.Dimension;


public class Board {
    static int h = 1000;
    static int l = 1000;
    static int moveNumb = 1;
    static boolean ex = false;
    static boolean didCastleShort = false;
    static boolean didCastleLong = false;
    static JPanel[][] boardPanel = new JPanel[8][8];
    static JFrame frame;
    static JPanel jp;
    static JTextArea textLabel;
    static ArrayList<JLabel> circBoxes = new ArrayList<>();
    static String moveOrder="";
    static Piece enpassant;
    static ArrayList<JPanel> pieceSelectorPanel;

    public static void makeNewBoard(){
        frame = new JFrame();
        jp = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ends program when JFrame closed

        

        frame.setSize(h+300,l+300); //pixel size of frame in width then height
       
        jp.setLayout(null);//Out.  Back in.
        jp.setBackground(new java.awt.Color(100, 255, 100));
        frame.add(jp); 

        //Move order to txt button
        jp.add(createMoveOrderJButton());  
        //move order jp
        jp.add(createMoveOrderJLabel());
        // piece selector
        //createPieceSelectorJPanel(PColor.WHITE,false);
       
        
        //textLabel.setFont(new Font("Verdana",1,20));



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
                if(Pieces.getBoardXY(j, i) == null){continue;}
                String s = "./assets/";
                String color = Pieces.getBoardXY(j, i).getColor().toString().toLowerCase();
                String type = Pieces.getBoardXY(j, i).getType().toString().toLowerCase();
                JLabel l = new JLabel(new ImageIcon(s + color + "_" + type + ".png"));
                boardPanel[j][i].add(l);
                Pieces.getBoardXY(j, i).setLabel(l);
                
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


    public static void addToMoveOrder(PColor c,PType type,Square s,Square clickedSquare){
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
        if(type != PType.PAWN) {
           if(type==PType.KNIGHT) {
                moveOrder += "n";
                otherPieceCanMove( s,  clickedSquare);
           }else{
           moveOrder += type.toString().charAt(0);
            }
            if(type==PType.ROOK) {
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
            if(s == null || Pieces.getBoardSquare(s) == null || s.equals(originalSquare)){
                continue;
            }
            if(Pieces.getBoardSquare(s).getType() == Pieces.getBoardSquare(destinationSquare).getType() && Pieces.getBoardSquare(s).getColor() == Pieces.getBoardSquare(destinationSquare).getColor()){
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
    public static void createGameEndText(String text){
        JLabel label = new JLabel(text);
        Dimension size = label.getPreferredSize();
        label.setBounds(l+50, h-900, size.width, size.height);
        jp.add(label);
        updateBoard();
    }
    public static void afterMove(Square pieceOldSquare, Square pieceNewSquare, String color){
        //if king moved test if it was a castle and move the rook too. also update the king square.
        if(Pieces.getBoardSquare(pieceOldSquare).getType()==PType.KING){
            if(Pieces.getBoardSquare(pieceOldSquare).getColor() == PColor.BLACK){
                Pieces.bKingSquare = new Square(pieceNewSquare.getX(), pieceNewSquare.getY());
                if (Pieces.blackCanCastleLong||Pieces.blackCanCastleShort){
                    castle(color, pieceOldSquare, pieceNewSquare);
                    Pieces.blackCanCastleLong = false;
                    Pieces.blackCanCastleShort = false;
                }
            }
            if(Pieces.getBoardSquare(pieceOldSquare).getColor() == PColor.WHITE ){
                Pieces.wKingSquare = new Square(pieceNewSquare.getX(), pieceNewSquare.getY());
                if(Pieces.whiteCanCastleLong||Pieces.whiteCanCastleShort){
                    castle(color, pieceOldSquare, pieceNewSquare);
                    Pieces.whiteCanCastleLong = false;
                    Pieces.whiteCanCastleShort = false;
                }
            }
        } 
        //if a rook was moved check if we want to stop castling
        if (Pieces.getBoardSquare(pieceOldSquare).getType() == PType.ROOK){
            noCastle(pieceOldSquare);
        }
        // if we just enpassented
        if(Pieces.getBoardSquare(pieceOldSquare).getType() == PType.PAWN && pieceOldSquare.getX()!=pieceNewSquare.getX() && Pieces.getBoardSquare(pieceNewSquare) == null){
            Container parent = Pieces.getBoardXY(pieceNewSquare.getX(), pieceOldSquare.getY()).getLabel().getParent();
            if(parent!=null){
                parent.remove(Pieces.getBoardXY(pieceNewSquare.getX(), pieceOldSquare.getY()).getLabel());
            }
            //remove pawn
            Pieces.setBoardXY(pieceNewSquare.getX(), pieceOldSquare.getY(), null);
        }
        Pieces.setEnPassentable(null);
        if (Pieces.getBoardSquare(pieceOldSquare).getType() == PType.PAWN && (pieceNewSquare.getY()==pieceOldSquare.getY() + 2 || pieceNewSquare.getY()==pieceOldSquare.getY() - 2)){
            Pieces.setEnPassentable(pieceNewSquare);
        }
    }

    static void castle(String color,Square clickedSquare,Square square) {
        String string = "./assets/";
        JLabel lab = new JLabel(new ImageIcon(string+color+"_rook.png"));
        if(clickedSquare.getX()-square.getX()==2){
            //queenside
            didCastleLong = true;
            Pieces.setBoardXY(3, square.getY(), Pieces.getBoardXY(0, square.getY()));
            Container parent = Pieces.getBoardXY(0, square.getY()).getLabel().getParent();
            
            if(parent!=null){
                parent.remove(Pieces.getBoardXY(0, square.getY()).getLabel());
            }
            Pieces.getBoardXY(3, square.getY()).setLabel(lab);
            Board.boardPanel[3][square.getY()].add(lab);

            //remove rook
            Pieces.setBoardXY(0, square.getY(), null);
        }
        if(clickedSquare.getX()-square.getX()==-2) {
            //kingside
            didCastleShort = true;
            Pieces.setBoardXY(5, square.getY(), Pieces.getBoardXY(7, square.getY()));
            Container parent = Pieces.getBoardXY(7, square.getY()).getLabel().getParent();
            if(parent!=null) {
                parent.remove(Pieces.getBoardXY(7, square.getY()).getLabel());
            }
            Pieces.getBoardXY(5, square.getY()).setLabel(lab);
            Board.boardPanel[5][square.getY()].add(lab);
            Pieces.setBoardXY(7, square.getY(), null);
        }
    }
    private static void noCastle(Square square){
        if(square.getX() == 0){
            if(Pieces.getBoardSquare(square).getColor() == PColor.WHITE){
                Pieces.whiteCanCastleLong = false;
            }else{
                Pieces.blackCanCastleLong = false;
            }
        }
        if(square.getX()==7){
            if(Pieces.getBoardSquare(square).getColor() == PColor.WHITE){
                Pieces.whiteCanCastleShort = false;
            }else{
                Pieces.blackCanCastleShort = false;
            }
        }
    }
    private static JPanel createMoveOrderJLabel(){
        JPanel moveOrderJP = new JPanel();
        moveOrderJP.setBounds(l+50, h-300, 215, 200);
        textLabel = new JTextArea( "", 6, 20);
        textLabel.setLineWrap(true);
        textLabel.setWrapStyleWord(true);
        textLabel.setOpaque(false);
        textLabel.setEditable(false);
        moveOrderJP.add(textLabel);
        return moveOrderJP;

    }
    private static JButton createMoveOrderJButton(){
        JButton b = new JButton("Save mv Order");  
        b.setBounds(l +50,h-50,150,30);  
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){  
                try (PrintWriter out = new PrintWriter("pgn.txt")) {
                    out.println(moveOrder);
                }catch (FileNotFoundException x){
                    System.out.println("no file");
                }
            }  
        });
        return b;
    }
    public static ArrayList<JPanel> createPieceSelectorJPanel(PColor color, boolean isPromotion){
        ArrayList<JPanel> panels = new ArrayList<>();
        int height = isPromotion ? 200 : 300;
        int squareSize = 107;
        
        Piece[] pieceOrder = {new Piece(color,PType.QUEEN), new Piece(color,PType.ROOK), new Piece(color,PType.KNIGHT), new Piece(color,PType.BISHOP), new Piece(color, PType.KING), new Piece(color,PType.PAWN)}; 
        for(int i=0;i< (isPromotion ? 2 :3);i++) for(int j=0;j<2;j++){
                JPanel rectangle = new Listener(pieceOrder[j+(i*2)],l+50+(squareSize*(j%2)), h-((350+height)-(squareSize*(i))),isPromotion);
                String s = "./assets/";
                String col = pieceOrder[j+(i*2)].getColor().toString().toLowerCase();
                String type = pieceOrder[j+(i*2)].getType().toString().toLowerCase();
                JLabel l = new JLabel(new ImageIcon(s + col + "_" + type + ".png"));
                rectangle.add(l);
                jp.add(rectangle);
                panels.add(rectangle);
            }
        pieceSelectorPanel = panels;
        return panels;

    }
    public static void clickedPieceFromSelector(Piece piece){
        String string = "./assets/";
        String color = piece.getColor().toString().toLowerCase();
        String type = piece.getType().toString().toLowerCase();
        JLabel l = new JLabel(new ImageIcon(string+color+"_"+type+".png"));
        piece.setLabel(l);
    }
    public static void removePieceSelectorPanel(){
        if(pieceSelectorPanel!=null) for(JPanel j:pieceSelectorPanel) jp.remove(j);
    }

}
  