import javax.swing.JLabel;
public class Piece {

    public PColor color;
    public PType type;
    public JLabel label;

    public Piece(PColor color, PType type){
        this.color = color;
        this.type = type;
    }
    
}
