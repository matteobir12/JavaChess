import java.util.ArrayList;

public class Pieces {

    public static Piece[][] board = new Piece[8][8];
    static Boolean whiteCanCastleShort, whiteCanCastleLong;
    static Boolean blackCanCastleShort, blackCanCastleLong;
    
    /*{{Piece.BLACK_ROOK, Piece.BLACK_KNIGHT, Piece.BLACK_BISHOP, Piece.BLACK_QUEEN, Piece.BLACK_KING, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT, Piece.BLACK_ROOK},
        {Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN},
        {Piece.WHITE_ROOK, Piece.WHITE_KNIGHT, Piece.WHITE_BISHOP, Piece.WHITE_QUEEN, Piece.WHITE_KING, Piece.WHITE_BISHOP, Piece.WHITE_KNIGHT, Piece.WHITE_ROOK}};*/

    public Pieces(){
        initBoard();
    }

    public static void initBoard(){
        whiteCanCastleShort=true;
        whiteCanCastleLong = true;
        blackCanCastleShort=true;
         blackCanCastleLong = true;
        PColor curColor = null;

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
    }


    public static ArrayList<Square> movement(Square square){

        ArrayList<Square> moveable = new ArrayList<Square>();
        if(board[square.getX()][square.getY()] == null){return moveable;}
        Piece p = board[square.getX()][square.getY()];
        switch(p.type){
            case PAWN:
                if(p.color == PColor.WHITE){
                    Square s = new Square(square.getX(), square.getY() - 1);
                    if(pawnCheckVertical(s)){
                        moveable.add(s);
                        if(square.getY() == 6){
                            s = new Square(square.getX(), square.getY() - 2);
                            if(pawnCheckVertical(s)){
                                moveable.add(s);
                            }
                        }
                    }
                    s = new Square(square.getX() - 1, square.getY() - 1);
                    if(pawnTakes(s, PColor.WHITE)){
                        moveable.add(s);
                    }
                    s = new Square(square.getX() + 1, square.getY() - 1);
                    if(pawnTakes(s, PColor.WHITE)){
                        moveable.add(s);
                    }
                }
                else if(p.color == PColor.BLACK){
                    Square s = new Square(square.getX(), square.getY() + 1);
                    if(pawnCheckVertical(s)){
                        moveable.add(s);
                        if(square.getY() == 1){
                            s = new Square(square.getX(), square.getY() + 2);
                            if(pawnCheckVertical(s)){
                                moveable.add(s);
                            }
                        }
                    }
                     s = new Square(square.getX() - 1, square.getY() + 1);
                    if(pawnTakes(s, PColor.BLACK)){
                        moveable.add(s);
                    }
                     s = new Square(square.getX() + 1, square.getY() + 1);
                    if(pawnTakes(s, PColor.BLACK)){
                        moveable.add(s);
                    }
                }
                break;
            case ROOK:
                if(square.getX() == 0){
                    if(board[square.getX()][square.getY()].color == PColor.WHITE){
                        whiteCanCastleLong = false;
                    }else{
                    blackCanCastleLong = false;
                }
                }
                if(square.getX()==7){
                    if(board[square.getX()][square.getY()].color == PColor.WHITE){
                        whiteCanCastleShort = false;
                    }else{
                    blackCanCastleShort = false;
                }
                }
                moveable = rookMovement(square, p.color);
                break;
            case KNIGHT:
            
                Square s = new Square(square.getX() - 2, square.getY() - 1);
                if(checkIfMoveable(s, p.color)){
                    moveable.add(s);
                }
                s = new Square(square.getX() - 1, square.getY() - 2);
                if(checkIfMoveable(s, p.color)){
                    moveable.add(s);
                }
                s = new Square(square.getX() + 2, square.getY() + 1);
                if(checkIfMoveable(s, p.color)){
                    moveable.add(s);
                }
                s = new Square(square.getX() + 1, square.getY() + 2);
                if(checkIfMoveable(s, p.color)){
                    moveable.add(s);
                }
                s = new Square(square.getX() - 1, square.getY() + 2);
                if(checkIfMoveable(s, p.color)){
                    moveable.add(s);
                }
                s = new Square(square.getX() - 2, square.getY() + 1);
                if(checkIfMoveable(s, p.color)){
                    moveable.add(s);
                }
                s = new Square(square.getX() + 1, square.getY() - 2);
                if(checkIfMoveable(s, p.color)){
                    moveable.add(s);
                }
                s = new Square(square.getX() + 2, square.getY() - 1);
                if(checkIfMoveable(s, p.color)){
                    moveable.add(s);
                }

                break;
                
            case BISHOP:
           
                moveable = bishopMovment(square, p.color);
                break;
            case QUEEN:
            moveable = rookMovement(square, p.color);
            moveable.addAll(bishopMovment(square, p.color));
                break;
            case KING:
            Square sq = new Square(square.getX(), square.getY() - 1);
                if(checkIfMoveable(sq, p.color)){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() + 1, square.getY() - 1);
                if(checkIfMoveable(sq, p.color)){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() + 1, square.getY());
                if(checkIfMoveable(sq, p.color)){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() + 1, square.getY() + 1);
                if(checkIfMoveable(sq, p.color)){
                    moveable.add(sq);
                }
                sq = new Square(square.getX(), square.getY() + 1);
                if(checkIfMoveable(sq, p.color)){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() - 1, square.getY() + 1);
                if(checkIfMoveable(sq, p.color)){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() - 1, square.getY());
                if(checkIfMoveable(sq, p.color)){
                    moveable.add(sq);
                }
                sq = new Square(square.getX() -1, square.getY() - 1);
                if(checkIfMoveable(sq, p.color)){
                    moveable.add(sq);
                }
                if(board[square.getX()][square.getY()].color == PColor.WHITE){
                    if((whiteCanCastleLong&&board[3][7]==null)&&board[2][7]==null&&board[1][7]==null){
                      moveable.add(new Square(2, 7));
                    }
                    if((whiteCanCastleShort&&board[5][7]==null)&&board[6][7]==null){
                      moveable.add(new Square(6, 7));
                    }
                  }else{
                      if((whiteCanCastleLong&&board[3][0]==null&&board[2][0]==null)&&board[1][0]==null){
                          moveable.add(new Square(2, 0));
                        }
                        if((whiteCanCastleShort&&board[5][0]==null)&&board[6][0]==null){
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
        if((board[square.getX()][square.getY()] == null) || (board[square.getX()][square.getY()].color != taker)){
            return true;
        }
        return false;
    }

    public static boolean pawnCheckVertical(Square square){
        if(square.getX() > 7 || square.getY() > 7 || square.getX() < 0 || square.getY() < 0){
            return false;
        }
        if(board[square.getX()][square.getY()] == null){
            return true;
        }
        return false;
    }

    public static boolean pawnTakes(Square square, PColor taker){
        if(square.getX() > 7 || square.getY() > 7 || square.getX() < 0 || square.getY() < 0){
            return false;
        }
        if(board[square.getX()][square.getY()] != null && board[square.getX()][square.getY()].color != taker){
            return true;
        }
        return false;
    }
    
    
    

    private static ArrayList<Square> rookMovement(Square square,PColor color){
        ArrayList<Square> moveable = new ArrayList<Square>();
        int x = square.getX();
        int y = square.getY();
        //y down
        for(int i = y+1;i<8;i++){
            Square s = new Square(x,i);
            if(!checkIfMoveable(s, color)){
                break;
            }else{
                moveable.add(s);
                if (board[x][i]!=null){
                    break;
                }
            }
        }
        //y up
        for(int i =y-1;i>=0;i--){
            Square s = new Square(x,i);
            if(!checkIfMoveable(s, color)){
                break;
            }else{
                moveable.add(s);
                if (board[x][i]!=null){
                    break;
                }
            }
        }
//x right
        for(int i =x+1;i<8;i++){
            Square s = new Square(i,y);
            if(!checkIfMoveable(s, color)){
                break;
            }else{
                moveable.add(s);
                if (board[i][y]!=null){
                    break;
                }
            }
        }
        //x left
        for(int i =x-1;i>=0;i--){
            Square s = new Square(i,y);
            if(!checkIfMoveable(s, color)){
                break;
            }else{
                moveable.add(s);
                if (board[i][y]!=null){
                    break;
                }
            }
            
        }
        return moveable;
    }

    private static ArrayList<Square> bishopMovment(Square square,PColor color){
        ArrayList<Square> moveable = new ArrayList<Square>();
        int x = square.getX();
        int y = square.getY();
        //right diags
        boolean cntUp=true, cntDown=true;
        for(int i = x+1;i<8;i++){
            Square s = new Square(i,y+i-x);
            Square s2 = new Square(i,y-i+x);
        
        if(cntDown){
            if(checkIfMoveable(s, color)){
                moveable.add(s);
                if (board[s.getX()][s.getY()]!=null){
                    cntDown =false;
                }
                
            }else{
                cntDown=false;
            }
        }
        if(cntUp){
            if(checkIfMoveable(s2, color)){
                moveable.add(s2);
                if (board[s2.getX()][s2.getY()]!=null){//////
                    cntUp =false;
                }
               
            }else{
                cntUp=false;
            }
        }
        if (!cntUp&&!cntDown){break;}
        }
    cntUp = true;
    cntDown = true;
        //left diags
        System.out.println("trying left");
        for(int i = x-1;i>=0;i--){
            Square s = new Square(i,y+x-i);
            Square s2 = new Square(i,y-x+i);
        
        if(cntDown){
            if(checkIfMoveable(s, color)){
                moveable.add(s);
                if (board[s.getX()][s.getY()]!=null){
                    cntDown =false;
                }
            }else{
                cntDown=false;
            }
        }
        if(cntUp){
            System.out.println("goin up");
            if(checkIfMoveable(s2, color)){
                System.out.println("that new new");
                moveable.add(s2);
                if (board[s2.getX()][s2.getY()]!=null){
                    cntUp =false;
                }
            }else{ 
                cntUp=false;
            }
        }
        if (!cntUp&&!cntDown){break;}
        }
        return moveable;
    }
}
