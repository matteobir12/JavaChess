import java.io.Console;
import java.util.ArrayList;
import java.util.Iterator;

public class Pieces {

    public static Piece[][] board = new Piece[8][8];
    static boolean whiteCanCastleLong;
    static boolean whiteCanCastleShort;
    static boolean blackCanCastleLong;
    static boolean blackCanCastleShort;
    static ArrayList<Square> threats;
    static ArrayList<Square> pinned = new ArrayList<>();
    static ArrayList<Square> pinners = new ArrayList<>();
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
        if(board[square.getX()][square.getY()] == null)return moveable;
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
                boolean isWhite = board[square.getX()][square.getY()].getColor() == PColor.WHITE;
                boolean castleShort = isWhite ? whiteCanCastleShort : blackCanCastleShort;
                boolean castleLong = isWhite ? whiteCanCastleLong : blackCanCastleLong;
                int yVal = isWhite ? 7 : 0;
                if((castleLong&&board[3][yVal]==null)&&board[2][yVal]==null&&board[1][yVal]==null&&threats.isEmpty()&&threatsToPiece(p.getColor(), new Square(3, yVal),null,null).isEmpty()&&threatsToPiece(p.getColor(), new Square(2, yVal),null,null).isEmpty()&&threatsToPiece(p.getColor(), new Square(1, yVal),null,null).isEmpty()){
                    moveable.add(new Square(2, yVal));
                }
                if((castleShort&&board[5][yVal]==null)&&board[6][yVal]==null&&threats.isEmpty()&&threatsToPiece(p.getColor(), new Square(5, yVal),null,null).isEmpty()&&threatsToPiece(p.getColor(), new Square(6, yVal),null,null).isEmpty()){
                    moveable.add(new Square(6, yVal));
                }
                break;
        }
        restrictMovementIfInCheck(square, moveable);
        restrictMovementIfPinned(square, moveable);
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
        return (board[square.getX()][square.getY()] != null && board[square.getX()][square.getY()].getColor() != taker);
    
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

    private static ArrayList<Square> rookMovement(Square square,PColor color){
        ArrayList<Square> moveable = new ArrayList<>();
        //y down (int i = y+1;i<8;i++) new Square(x,i)
        moveable.addAll(movementHelper(square, color, 0, 1));
        //y up (int i =y-1;i>=0;i--) new Square(x,i)
        moveable.addAll(movementHelper(square, color, 0, -1));
        //x right (int i =x+1;i<8;i++) new Square(i,y)
        moveable.addAll(movementHelper(square, color, 1, 0));
        //x left (int i =x-1;i>=0;i--) new Square(i,y)
        moveable.addAll(movementHelper(square, color, -1, 0));
        return moveable;
    }

    private static ArrayList<Square> bishopMovement(Square square,PColor color){
        ArrayList<Square> moveable = new ArrayList<>();
        //up right
        moveable.addAll(movementHelper(square, color, 1, 1));
        // down right
        moveable.addAll(movementHelper(square, color, 1, -1));
        // up left
        moveable.addAll(movementHelper(square, color, -1, 1));
        // down left
        moveable.addAll(movementHelper(square, color, -1, -1));
        return moveable;
    }

    private static ArrayList<Square> movementHelper(Square square, PColor color, int xDiff, int yDiff){
        ArrayList<Square> moveable = new ArrayList<>();
        Square s = new Square(square.getX()+xDiff,square.getY()+yDiff);
        while(checkIfMoveable(s, color)){
            moveable.add(s);
            if (board[s.getX()][s.getY()]!=null) {
                break;
            }
            s = new Square(s.getX()+xDiff,s.getY()+yDiff);
        }
        return moveable;
    }

    private static ArrayList<Square> veticalNHorizontalThreatsAndPins(Square square,PColor color, ArrayList<Square> pinners, ArrayList<Square> pinned){
        ArrayList<Square> moveable = new ArrayList<>();
        //y down (int i = y+1;i<8;i++) new Square(x,i)
        Square yd = threatsHelper(square, color, pinners, pinned, 0, -1);
        if (yd != null) moveable.add(yd);
        //y up (int i =y-1;i>=0;i--) new Square(x,i)
        Square yu = threatsHelper(square, color, pinners, pinned, 0, 1);
        if (yu != null) moveable.add(yu);
        //x right (int i =x+1;i<8;i++) new Square(i,y)
        Square xr = threatsHelper(square, color, pinners, pinned, 1, 0);
        if (xr != null) moveable.add(xr);
        //x left (int i =x-1;i>=0;i--) new Square(i,y)
        Square xl = threatsHelper(square, color, pinners, pinned, -1, 0);
        if (xl != null) moveable.add(xl);
        return moveable;
    }

    private static ArrayList<Square> diagonalThreatsAndPins(Square square, PColor color, ArrayList<Square> pinners, ArrayList<Square> pins){
        ArrayList<Square> possibleThreats = new ArrayList<>();
        //up right
        Square ur = threatsHelper(square, color, pinners, pins, 1, 1);
        if (ur != null) possibleThreats.add(ur);
        // down right
        Square dr = threatsHelper(square, color, pinners, pins, 1, -1);
        if (dr != null) possibleThreats.add(dr);
        // up left
        Square ul = threatsHelper(square, color, pinners, pins, -1, 1);
        if (ul != null) possibleThreats.add(ul);
        // down left
        Square dl = threatsHelper(square, color, pinners, pins, -1, -1);
        if (dl != null) possibleThreats.add(dl);
        return possibleThreats;
    }
    private static Square threatsHelper(Square square, PColor color, ArrayList<Square> pinners, ArrayList<Square> pinned, int xDiff, int yDiff){
        Square possiblePin = null;
        Square s = new Square(square.getX()+xDiff,square.getY()+yDiff);
        while(true){
            boolean squareHasTheKing = checkIfMoveable(s, pcolorOther(color)) && (board[s.getX()][s.getY()] != null && board[s.getX()][s.getY()].getType() == PType.KING && board[s.getX()][s.getY()].getColor() == color);
            //is empty or of opposite color and is not the current color king
            if(checkIfMoveable(s, color) || squareHasTheKing){
                // check if its not empty and if the piece is a piece that can pin in this situation
                if (board[s.getX()][s.getY()] != null && !squareHasTheKing){
                        if (board[s.getX()][s.getY()].getType()==PType.QUEEN|| ((xDiff!=0 && yDiff !=0) ? board[s.getX()][s.getY()].getType()==PType.BISHOP: board[s.getX()][s.getY()].getType()==PType.ROOK)) {
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
                if (possiblePin!=null) return null;
                possiblePin = s;
            //walked out of bounds or we don't care about pinned pieces
            } else {
                return null;

            }
            s = new Square(s.getX()+xDiff,s.getY()+yDiff);
        }
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
        attackers.addAll(veticalNHorizontalThreatsAndPins(square, color, pinners, pinned));
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
        threats = threatsToPiece(otherColor, newTurnKingSquare,pinners,pinned);
        if (updatePiecesPossibleMoves(otherColor)) System.out.println(threats.isEmpty()?"Stalemate":"checkmate");
    }
    private static boolean updatePiecesPossibleMoves(PColor color){
        long start = System.nanoTime();
        boolean noPossibleMoves = true;
        for (int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) {
            if (board[i][j] != null && board[i][j].getColor() == color){
                ArrayList<Square> possibleMoves = movement(new Square(i,j));
                board[i][j].setPossibleMoves(possibleMoves);
                if(!possibleMoves.isEmpty()){
                    noPossibleMoves = false;
                }
            }
        }
        // System.out.println("took "+((System.nanoTime()-start)/1000)+" microseconds to calculate all possible moves for " + ((color == PColor.BLACK) ? "black" : "white"));
        return noPossibleMoves;
    }
    private static ArrayList<Square> restrictMovementIfInCheck(Square square, ArrayList<Square> movement){
        if (threats!=null && !threats.isEmpty() && Pieces.board[square.getX()][square.getY()].getType() != PType.KING) {
            //by one attacker
            if (threats.size()==1){
                Square curking = (Main.curTurn == PColor.BLACK) ? Board.bKingSquare : Board.wKingSquare;
            boolean isKnight = (Pieces.board[threats.get(0).getX()][threats.get(0).getY()].getType()==PType.KNIGHT);
            Iterator<Square> iter = movement.iterator(); 
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
                    for(int i = 0; i<movement.size(); i++){
                        movement.remove(i);
                    }
                }
            }
            
        }
        return movement;
    }
    private static ArrayList<Square> restrictMovementIfPinned(Square square, ArrayList<Square> movement){
        if(pinned != null && !pinned.isEmpty()){
            for(int i=0; i<pinned.size();i++){
                //is it the piece they clicked
                if(square.equals(pinned.get(i))){
                    Square curking = (Main.curTurn == PColor.BLACK) ? Board.bKingSquare : Board.wKingSquare ; 
                    Iterator<Square> iter = movement.iterator(); 
                    while(iter.hasNext()){
                        Square tmpS = iter.next();
                        //if the move doesnt break the pin
                        if(pieceBlocks(curking, pinners.get(i), tmpS) || tmpS.equals(pinners.get(i))){
                            continue;
                    }
                    iter.remove();
                    }   
                }
            }
        }
        return movement;
    }
}
