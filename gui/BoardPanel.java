package Chess.gui;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

import javax.swing.JButton;
import Chess.model.board.*;
import Chess.model.game.*;

public class BoardPanel {
    private JButton[][] cellButtons;
    private Cell selectedCell;
    private Game game;
    
    
    public void intBoard(){
        cellButtons = new JButton[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cellButtons[i][j] = new JButton();  
            }
        }

        //add mouse listeners to all buttons
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cellButtons[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                    
                    
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {
                        
                    }
                    @Override
                    public void mouseReleased(MouseEvent e) {   
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        
                    }
                });
            }

    }


}

        public void onCellClicked(int row, int col) {
        }

}
