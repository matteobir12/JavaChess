import java.util.ArrayList;

import javax.swing.JLabel;
public class Piece {

    private PColor color;
    private PType type;
    private JLabel label;
    private ArrayList<Square> possibleMoves;

    public Piece(PColor color, PType type){
        this.color = color;
        this.type = type;
    }
    public void setColor(PColor color) {
        this.color = color;
    }
    public void setLabel(JLabel label) {
        this.label = label;
    }
    public void setType(PType type) {
        this.type = type;
    }
    public PColor getColor() {
        return color;
    }
    public JLabel getLabel() {
        return label;
    }
    public PType getType() {
        return type;
    }
    public void setPossibleMoves(ArrayList<Square> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }
    public ArrayList<Square> getPossibleMoves() {
        return possibleMoves;
    }
    public Square getPossibleMovesAtIndex(int i) {
        return possibleMoves.get(i);
        
    }
}
