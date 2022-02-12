import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Color;
//import java.awt.event.*; //imports Event package which listens for button press
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.*;


public class Listener extends JPanel {

    
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
                OnMouseClick.click(square);
            }
        });

    }
    public Listener(Piece piece, int x, int y,boolean isPromotion) {
        Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBackground( Color.white);
        this.setBounds(x, y, 107, 107);
        this.setBorder(blackline);
    
        addMouseListener(new MouseAdapter() { 
            @Override
            public void mousePressed(MouseEvent me) {
                Board.clickedPieceFromSelector(piece);
                if (isPromotion){
                    OnMouseClick.setPromotionPiece(piece);
                }
            }
        });

    }

}