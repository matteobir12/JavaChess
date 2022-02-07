import java.util.ArrayList;

public class Pieces {

    public static Piece[][] board = new Piece[8][8];
    static boolean whiteCanCastleShort, whiteCanCastleLong;
    static boolean blackCanCastleShort, blackCanCastleLong;
    private static Square enPassentable;
    
    /*{{Piece.BLACK_ROOK, Piece.BLACK_KNIGHT, Piece.BLACK_BISHOP, Piece.BLACK_QUEEN, Piece.BLACK_KING, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT, Piece.BLACK_ROOK},
        {Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN},
        {Piece.WHITE_ROOK, Piece.WHITE_KNIGHT, Piece.WHITE_BISHOP, Piece.WHITE_QUEEN, Piece.WHITE_KING, Piece.WHITE_BISHOP, Piece.WHITE_KNIGHT, Piece.WHITE_ROOK}};*/

     Pieces(){
        initBoard();
    }

    public static void initBoard(){
        whiteCanCastleShort=true;
        whiteCanCastleLong = true;
        blackCanCastleShort=true;
         blackCanCastleLong = true;
        PColor curColor = null;
        Board.bKingSquare = new Square(4, 0);
        Board.wKingSquare = new Square(4, 7);

        for(int i = 0; i < 8; i+=7){
            if(i == 0){
                curColor = PColor.BLACK;
            }else{
                curColor = PColor.WHITE;
            }

            board[0][i] = new Piece(curColor, PType.ROOK);
            board[7][i] = new Piece(curColor, PType.ROOK);
            board[1][i] = new Piece(curColor, PType.KNIGHT);
            board[6][i] = new Piece(curColor, PType.KNIGHT);
            board[2][i] = new Piece(curColor, PType.BISHOP);
            board[5][i] = new Piece(curColor, PType.BISHOP);
            board[3][i] = new Piece(curColor, PType.QUEEN);
            board[4][i] = new Piece(curColor, PType.KING);
        }


        for(int i = 0; i < 8; i++){
            board[i][1] = new Piece(PColor.BLACK, PType.PAWN);
            board[i][6] = new Piece(PColor.WHITE, PType.PAWN);
        }
        updatePiecesPossibleMoves(PColor.WHITE);
    }


    public static ArrayList<Square> movement(Square square){
        ArrayList<Square> moveable = new ArrayList<>();
        if(board[square.getX()][square.getY()] == null){return moveable;}
        Piece p = board[square.getX()][square.getY()];
        switch(p.getType()){
            case PAWN:
                int diff = (p.getColor() == PColor.WHITE) ? -1: 1;
                Square s = new Square(square.getX(), square.getY() + diff);
                if(pawnCheckVertical(s)){
                    moveable.add(s);
                    if((square.getY() == 6 && diff == -1) || (square.getY() == 1 && diff == 1)){
                        s = new Square(square.getX(), square.getY() + (2*diff));
                        if(pawnCheckVertical(s)){
                            moveable.add(s);
                        }
                    }
                }
                boolean enAndYequals = (enPassentable != null && enPassentable.getY() == square.getY());
                s = new Square(square.getX() - 1, square.getY() + diff);
                if((pawnTakes(s, p.getColor()) || (enAndYequals && enPassentable.getX() == square.getX() - 1))){
                    moveable.add(s);
                }
                s = new Square(square.getX() + 1, square.getY() + diff);
                if(pawnTakes(s, p.getColor()) || enAndYequals && enPassentable.getX() == square.getX() + 1){
                    moveable.add(s);
                }
                break;
            case ROOK:
                moveable = rookMovement(square, p.getColor());
                break;
            case KNIGHT:
                moveable = knightMovement(square,p.getColor(),true);
                break;
                
            case BISHOP:
                moveable = bishopMovement(square, p.getColor());
                break;
            case QUEEN:
            moveable = rookMovement(square, p.getColor());
            moveable.addAll(bishopMovement(square, p.getColor()));
                break;
            case KING:
            Square sq = new Square(square.getX(), square.getY() - 1);
                if(checkIfMoveable(sq, p.getColor())&&threatsToPiece(p.getColor(), sq,null,null).isEmpty()){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() + 1, square.getY() - 1);
                if(checkIfMoveable(sq, p.getColor())&&threatsToPiece(p.getColor(), sq,null,null).isEmpty()){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() + 1, square.getY());
                if(checkIfMoveable(sq, p.getColor())&&threatsToPiece(p.getColor(), sq,null,null).isEmpty()){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() + 1, square.getY() + 1);
                if(checkIfMoveable(sq, p.getColor())&&threatsToPiece(p.getColor(), sq,null,null).isEmpty()){
                    moveable.add(sq);
                }
                sq = new Square(square.getX(), square.getY() + 1);
                if(checkIfMoveable(sq, p.getColor())&&threatsToPiece(p.getColor(), sq,null,null).isEmpty()){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() - 1, square.getY() + 1);
                if(checkIfMoveable(sq, p.getColor())&&threatsToPiece(p.getColor(), sq,null,null).isEmpty()){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() - 1, square.getY());
                if(checkIfMoveable(sq, p.getColor())&&threatsToPiece(p.getColor(), sq,null,null).isEmpty()){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() -1, square.getY() - 1);
                if(checkIfMoveable(sq, p.getColor())&&threatsToPiece(p.getColor(), sq,null,null).isEmpty()){
                    moveable.add(sq);
                }
                if(board[square.getX()][square.getY()].getColor() == PColor.WHITE){
                    if((whiteCanCastleLong&&board[3][7]==null)&&board[2][7]==null&&board[1][7]==null&&threatsToPiece(p.getColor(), new Square(2, 7),null,null).isEmpty()&&threatsToPiece(p.getColor(), new Square(1, 7),null,null).isEmpty()&&threatsToPiece(p.getColor(), new Square(3, 7),null,null).isEmpty()&&OnMouseClick.threats.isEmpty()){
                      moveable.add(new Square(2, 7));
                    }
                    if((whiteCanCastleShort&&board[5][7]==null)&&board[6][7]==null&&threatsToPiece(p.getColor(), new Square(5, 7),null,null).isEmpty()&&threatsToPiece(p.getColor(), new Square(6, 7),null,null).isEmpty()&&OnMouseClick.threats.isEmpty()){
                      moveable.add(new Square(6, 7));
                    }
                  }else{
                      if((whiteCanCastleLong&&board[3][0]==null&&board[2][0]==null)&&board[1][0]==null&&OnMouseClick.threats.isEmpty()&&threatsToPiece(p.getColor(), new Square(3, 0),null,null).isEmpty()&&threatsToPiece(p.getColor(), new Square(2, 0),null,null).isEmpty()&&threatsToPiece(p.getColor(), new Square(1, 0),null,null).isEmpty()){
                          moveable.add(new Square(2, 0));
                        }
                        if((whiteCanCastleShort&&board[5][0]==null)&&board[6][0]==null&&OnMouseClick.threats.isEmpty()&&threatsToPiece(p.getColor(), new Square(5, 7),null,null).isEmpty()&&threatsToPiece(p.getColor(), new Square(6, 7),null,null).isEmpty()) {
                          moveable.add(new Square(6, 0));
                        }
              }
                break;
        }
        return moveable;
    }

    public static boolean checkIfMoveable(Square square, PColor taker){
        if(square.getX() > 7 || square.getY() > 7 || square.getX() < 0 || square.getY() < 0){
            return false;
        }
        return ((board[square.getX()][square.getY()] == null) || (board[square.getX()][square.getY()].getColor() != taker));
        
    }

    public static boolean pawnCheckVertical(Square square){
        if(square.getX() > 7 || square.getY() > 7 || square.getX() < 0 || square.getY() < 0){
            return false;
        }
        return (board[square.getX()][square.getY()] == null);
    }

    public static boolean pawnTakes(Square square, PColor taker){
        if(square.getX() > 7 || square.getY() > 7 || square.getX() < 0 || square.getY() < 0){
            return false;
        }
        if(board[square.getX()][square.getY()] != null && board[square.getX()][square.getY()].getColor() != taker){
            return true;
        }
        return false;
    }
    
    private static ArrayList<Square> knightMovement(Square square,PColor color,boolean movement){
        ArrayList<Square> moveable = new ArrayList<>();
        Square s = new Square(square.getX() - 2, square.getY() - 1);
                if(checkIfMoveable(s, color)&&(movement||(board[s.getX()][s.getY()]!=null&&board[s.getX()][s.getY()].getType() == PType.KNIGHT))){
                    moveable.add(s);
                }
                s = new Square(square.getX() - 1, square.getY() - 2);
                if(checkIfMoveable(s, color)&&(movement||(board[s.getX()][s.getY()]!=null&&board[s.getX()][s.getY()].getType() == PType.KNIGHT))){
                    moveable.add(s);
                }
                s = new Square(square.getX() + 2, square.getY() + 1);
                if(checkIfMoveable(s, color)&&(movement||(board[s.getX()][s.getY()]!=null&&board[s.getX()][s.getY()].getType() == PType.KNIGHT))){
                    moveable.add(s);
                }
                s = new Square(square.getX() + 1, square.getY() + 2);
                if(checkIfMoveable(s, color)&&(movement||(board[s.getX()][s.getY()]!=null&&board[s.getX()][s.getY()].getType() == PType.KNIGHT))){
                    moveable.add(s);
                }
                s = new Square(square.getX() - 1, square.getY() + 2);
                if(checkIfMoveable(s, color)&&(movement||(board[s.getX()][s.getY()]!=null&&board[s.getX()][s.getY()].getType() == PType.KNIGHT))){
                    moveable.add(s);
                }
                s = new Square(square.getX() - 2, square.getY() + 1);
                if(checkIfMoveable(s, color)&&(movement||(board[s.getX()][s.getY()]!=null&&board[s.getX()][s.getY()].getType() == PType.KNIGHT))){
                    moveable.add(s);
                }
                s = new Square(square.getX() + 1, square.getY() - 2);
                if(checkIfMoveable(s, color)&&(movement||(board[s.getX()][s.getY()]!=null&&board[s.getX()][s.getY()].getType() == PType.KNIGHT))){
                    moveable.add(s);
                }
                s = new Square(square.getX() + 2, square.getY() - 1);
                if(checkIfMoveable(s, color)&&(movement||(board[s.getX()][s.getY()]!=null&&board[s.getX()][s.getY()].getType() == PType.KNIGHT))){
                    moveable.add(s);
                }
                return moveable;
    }
    
    /**
     * 
     * @param square
     * @param color
     * @param movement
     * @return
     */
    private static ArrayList<Square> rookMovement(Square square,PColor color){
        ArrayList<Square> moveable = new ArrayList<>();
        //y down (int i = y+1;i<8;i++) new Square(x,i)
        moveable.addAll(rookMovementHelper(square, color, false, false));
        //y up (int i =y-1;i>=0;i--) new Square(x,i)
        moveable.addAll(rookMovementHelper(square, color, false, true));
        //x right (int i =x+1;i<8;i++) new Square(i,y)
        moveable.addAll(rookMovementHelper(square, color, true, false));
        //x left (int i =x-1;i>=0;i--) new Square(i,y)
        moveable.addAll(rookMovementHelper(square, color, true, true));
        return moveable;
    }

    private static ArrayList<Square> rookMovementHelper(Square square,PColor color, boolean xDirection, boolean negative) {
        ArrayList<Square> moveable = new ArrayList<>();
        int x = square.getX();
        int y = square.getY();
        int diff = 1;
        if (negative) diff = -1;
        int direction = y;
        if (xDirection) direction = x;
        for(int i = direction+diff;0<=i && i<8;i+=diff){
            Square s = new Square(!xDirection ? x :i, xDirection ? y :i);
            if(!checkIfMoveable(s, color)){
                break;
            }else{
                moveable.add(s);
                if (board[!xDirection ? x :i][xDirection ? y :i]!=null){
                    break;
                }
            }
        }
        return moveable;
    }
    private static ArrayList<Square> veticalNHorizontalThreats(Square square,PColor color, ArrayList<Square> pinners, ArrayList<Square> pinned){
        ArrayList<Square> moveable = new ArrayList<>();
        //y down (int i = y+1;i<8;i++) new Square(x,i)
        Square yd = veticalNHorizontalThreatsHelper(square, color, false, false, pinners, pinned);
        if (yd != null) moveable.add(yd);
        //y up (int i =y-1;i>=0;i--) new Square(x,i)
        Square yu = veticalNHorizontalThreatsHelper(square, color, false, true, pinners, pinned);
        if (yu != null) moveable.add(yu);
        //x right (int i =x+1;i<8;i++) new Square(i,y)
        Square xr = veticalNHorizontalThreatsHelper(square, color, true, false, pinners, pinned);
        if (xr != null) moveable.add(xr);
        //x left (int i =x-1;i>=0;i--) new Square(i,y)
        Square xl = veticalNHorizontalThreatsHelper(square, color, true, true, pinners, pinned);
        if (xl != null) moveable.add(xl);
        return moveable;
    }
    private static Square veticalNHorizontalThreatsHelper(Square square, PColor color, boolean xDirection, boolean negative, ArrayList<Square> pinners, ArrayList<Square> pinned) {
        int x = square.getX();
        int y = square.getY();
        Square possiblePin = null;
        int diff = 1;
        if (negative) diff = -1;
        int direction = y;
        if (xDirection) direction = x;
        for(int i = direction+diff;0<=i && i<8; i+=diff){
            Square s = new Square(!xDirection ? x :i, xDirection ? y :i);
            //is empty or of opposite color
            if(checkIfMoveable(s, color)) {
                // check if its not empty and if the piece is a queen or rook
                if (board[s.getX()][s.getY()] != null){ 
                    if (board[s.getX()][s.getY()].getType() == PType.QUEEN||board[s.getX()][s.getY()].getType() == PType.ROOK) {
                        if (possiblePin != null) { 
                            pinned.add(possiblePin);
                            if (pinners != null) pinners.add(s);
                            return null;
                        }
                        return s;
                    }
                    return null;
                }
            //checks if the square is in bounds and is populated with a piece of the same color and we care about pinned pieces
            }else if (checkIfMoveable(s, pcolorOther(color)) && pinned != null){
                possiblePin = s;
            //walked out of bounds
            } else {
                return null;
            }
        }
        return null;
    }

    private static ArrayList<Square> bishopMovement(Square square,PColor color){
        ArrayList<Square> moveable = new ArrayList<>();
        //up right i = x+1;i<8;i++ new Square(i,y-i+x)
        moveable.addAll(bishopMovementHelper(square, color, true, true));
        // down right i = x+1;i<8;i++ new Square(i,y+i-x)
        moveable.addAll(bishopMovementHelper(square, color, true, false));
        // up left i = x-1 i>=0 i-- new Square(i,y-x+i)
        moveable.addAll(bishopMovementHelper(square, color, false, true));
        // down left i = x-1 i>=0 i-- new Square(i,y+x-i)
        moveable.addAll(bishopMovementHelper(square, color, false, false));
        return moveable;
    }
    private static ArrayList<Square> bishopMovementHelper(Square square, PColor color, boolean right, boolean up){
        ArrayList<Square> moveable = new ArrayList<>();
        int x = square.getX();
        int y = square.getY();
        int xDiff = -1;
        int yDiff = -1;
        if (right) xDiff = 1;
        if (up) yDiff = 1;
        for(int i = x+xDiff;i>=0 && i < 8;i+=xDiff) {
            Square s = new Square(i,y+(x*yDiff)-(i*yDiff));
            if(checkIfMoveable(s, color)){
                moveable.add(s);
                if (board[s.getX()][s.getY()]!=null) {
                    break;
                }
            } else {
                break;
            }
        }
        return moveable;
    }

    private static ArrayList<Square> diagonalThreatsAndPins(Square square, PColor color, ArrayList<Square> pinners, ArrayList<Square> pins){
        ArrayList<Square> possibleThreats = new ArrayList<>();
        //up right i = x+1;i<8;i++ new Square(i,y-i+x)
        Square ur = diagThreatsHelper(square, color, pinners, pins, true, true);
        if (ur != null) possibleThreats.add(ur);
        // down right i = x+1;i<8;i++ new Square(i,y+i-x)
        Square dr = diagThreatsHelper(square, color, pinners, pins, true, false);
        if (dr != null) possibleThreats.add(dr);
        // up left i = x-1 i>=0 i-- new Square(i,y-x+i)
        Square ul = diagThreatsHelper(square, color, pinners, pins, false, true);
        if (ul != null) possibleThreats.add(ul);
        // down left i = x-1 i>=0 i-- new Square(i,y+x-i)
        Square dl = diagThreatsHelper(square, color, pinners, pins, false, false);
        if (dl != null) possibleThreats.add(dl);
        return possibleThreats;
    }
    private static Square diagThreatsHelper(Square square, PColor color, ArrayList<Square> pinners, ArrayList<Square> pinned, boolean right, boolean up){
        int x = square.getX();
        int y = square.getY();
        Square possiblePin = null;
        int xDiff = -1;
        int yDiff = -1;
        if (right) xDiff = 1;
        if (up) yDiff = 1;
        for(int i = x+xDiff;i>=0 && i < 8;i+=xDiff) {
            Square s = new Square(i,y+(x*yDiff)-(i*yDiff));
            if(checkIfMoveable(s, color)){
                if (board[s.getX()][s.getY()] != null){
                    if (board[s.getX()][s.getY()].getType()==PType.QUEEN||board[s.getX()][s.getY()].getType()==PType.BISHOP) {
                        if (possiblePin != null){
                            pinned.add(possiblePin);
                            if (pinners != null) pinners.add(s);
                            return null;
                        }
                        return s;
                    }
                    return null;
                }
            //checks if the square is in bounds and is populated with a piece of the same color and we care about pinned pieces
            }else if (checkIfMoveable(s, pcolorOther(color)) && pinned != null){
                possiblePin = s;
            //walked out of bounds
            } else {
                return null;
            }
            
        }
        return null;
    }

    private static ArrayList<Square> pawnThreats(Square square, PColor color){
        ArrayList<Square> threats = new ArrayList<>();
        int yDiff = (color == PColor.WHITE) ? -1: 1;
        if(square.getY()+yDiff >=0 && square.getY()+yDiff < 8){
            if(square.getX()-1 >=0 && square.getX()-1 < 8){
                Square s = new Square(square.getX()-1,square.getY()+yDiff);
                if (board[s.getX()][s.getY()] != null && board[s.getX()][s.getY()].getType() == PType.PAWN && board[s.getX()][s.getY()].getColor() == pcolorOther(color)) {
                    threats.add(s);
                }
            }
            if(square.getX()+1 >=0 && square.getX()+1 < 8){
                Square s = new Square(square.getX()+1,square.getY()+yDiff);
                if (board[s.getX()][s.getY()] != null && board[s.getX()][s.getY()].getType() == PType.PAWN && board[s.getX()][s.getY()].getColor() == pcolorOther(color)){
                    threats.add(s);
                }
            }
        }
        return threats;
    }

    public static ArrayList<Square> threatsToPiece(PColor color,Square square, ArrayList<Square> pinners, ArrayList<Square> pinned){
        ArrayList<Square> attackers = knightMovement(square, color, false);
        attackers.addAll(veticalNHorizontalThreats(square, color, pinners, pinned));
        attackers.addAll(diagonalThreatsAndPins(square, color, pinners, pinned));
        attackers.addAll(pawnThreats(square, color));
        return attackers;

    }
    public static boolean pieceBlocks(Square toBlock,Square attacker,Square blocker){
        //if they are being attacked in a line eg rook
        if(toBlock.getX()==attacker.getX()){
            return (blocker.getX() ==toBlock.getX()&&((blocker.getY()<toBlock.getY()&&blocker.getY()>attacker.getY())||(blocker.getY()>toBlock.getY()&&blocker.getY()<attacker.getY())));
        }
        if(toBlock.getY()==attacker.getY()){
            return (blocker.getY() ==toBlock.getY()&&((blocker.getX()<toBlock.getX()&&blocker.getX()>attacker.getX())||(blocker.getX()>toBlock.getX()&&blocker.getX()<attacker.getX())));
        }
        //otherwise they must be being attacked by a diagonal
        Square sq = new Square();
        attacker.copy(sq);
        int modx =1;
        int mody = 1;
        if(attacker.getX()>toBlock.getX()){
           modx = -1;
        }
        if(attacker.getY()>toBlock.getY()){
            mody = -1;

        }
    
        while(!sq.equals(toBlock)){
           
            sq.setX(sq.getX()+modx);
            sq.setY(sq.getY()+mody);
    
            if(sq.equals(blocker)){
                return true;
            }
        }

    return false;
    }
    private static PColor pcolorOther(PColor color){
        if (color == PColor.BLACK){
            return PColor.WHITE;
        }
        return PColor.BLACK;
    }
    public static void setEnPassentable(Square enPassentable) {
        Pieces.enPassentable = enPassentable;
  }
    public static Square getEnPassentable() {
        return enPassentable;
  }
    public static void switchAndPrepareForNextTurn(PColor oldTurnColor, String type, Square newSquare, Square oldSquare){
        PColor otherColor = pcolorOther(oldTurnColor);
        Square newTurnKingSquare = (oldTurnColor == PColor.WHITE) ? Board.bKingSquare : Board.wKingSquare;
        Main.curTurn = otherColor;
        Board.addToMoveOrder(oldTurnColor, type, newSquare,oldSquare);
        System.out.println("assessing threats to king on " + newTurnKingSquare.toString());
        OnMouseClick.threats = threatsToPiece(otherColor, newTurnKingSquare,OnMouseClick.pinners,OnMouseClick.pinned);
        if (updatePiecesPossibleMoves(otherColor)) System.out.println("no Moves");
    }
    private static boolean updatePiecesPossibleMoves(PColor color){
        boolean noPossibleMoves = true;
        for (int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) {
            if (board[i][j] != null && board[i][j].getColor() == color){
                ArrayList<Square> possibleMoves = movement(new Square(i,j));
                if(!possibleMoves.isEmpty()){
                    board[i][j].setPossibleMoves(possibleMoves);
                    noPossibleMoves = false;
                }
            }
        }
        return noPossibleMoves;
    }
}
