/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postlab_minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author emani
 */
public class Cell implements MouseListener{
    
        private int x;   // row
    private int y;   // col
    private Boolean hasFlag;
    private int Value;
    private Boolean isOpened;
    private Boolean hasMine;
    private JButton cellButton;
    private Board board;
    private MouseAdapter ma;
    
    public Cell(int x, int y, Boolean hasFlag, Boolean isOpened, Boolean hasMine, int Value, Board board)
    {
        this.x = x;
        this.y = y;
        this.hasFlag = hasFlag;
        this.hasMine = hasMine;
        this.isOpened = isOpened;
        this.Value = Value;
        this.board = board;
        this.cellButton = new JButton();
        cellButton.setBackground(new Color(102,102,255));
        cellButton.setBorder(BorderFactory.createEtchedBorder());
        cellButton.setBorder(BorderFactory.createBevelBorder(1,Color.BLUE, Color.BLACK));
        cellButton.setFocusable(false);
    }
    public void setHasFlag(Boolean f)
    {
        this.hasFlag = f;
    }
    public int getRow()
    {
        return this.x;
    }
    public int getCol()
    {
        return this.y;
        
    }
    public int getValue()
    {
        return this.Value;
    }
    public void setIsOpened(Boolean o)
    {
        this.isOpened = o;
    }
    public void setHasMine(Boolean m)
    {
        this.hasMine = m;
    }
    public void setValue(int v)
    {
        this.Value = v;
    }
    public Boolean checkHasMine()
    {
        return this.hasMine;
    }
    public Boolean checkIsOpened()
    {
        return this.isOpened;
    }
    public Boolean checkHasFlag()
    {
        return this.hasFlag;
    }
    public JButton getButton() 
    {
		return cellButton;
    }
    
    public void removeMouselistener()
    {

                for(int i=0; i< 9; i++)
                {
                    for(int j=0; j<9; j++)
                    {
                        board.getGrid()[i][j].getButton().removeMouseListener(ma);
                      //  System.out.println("mouse listener removed!");
//                        board.getGrid()[i][j].getButton().
                    }
                }
    }
    public void createListener() {
        
                ma = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
                                    board.getFaceJLabel().setIcon(board.getIconsArray()[14]);//wow
                                int modifiers = e.getModifiers();
                                if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                                                   //             System.out.println("Left button pressed.");
                                                                
                                    LeftClickFunction(x, y);
                                    //return;
                                }

                                if ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
                                           //                     System.out.println("Right button pressed.");
                                     
                                     RightClickFunction();
                                //     return;
                                }
                                if (((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) && ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)){
                                        //                        System.out.println("BOTH LEFT AND RIGHT BUTTONS ARE PRESSED!.");
                                                                
                                }
                             
                        }
                        @Override
                        public void mouseReleased(MouseEvent e)
                        {
                           // System.out.println("mouse released");
                            
                            board.getFaceJLabel().setIcon(board.getIconsArray()[13]);//smiley
       
                            
                        }
                };
                
                    cellButton.addMouseListener(ma);
    }
    
    public void LeftClickFunction(int x, int y)
    {
        
        if(board.getGameStatus())  // if game has been won or lost then do not perform click functions
                {
                    return;
                }
        if(!board.getStartTime()) //  timer not started yet so start it and set starttime = true
        {
            board.startTimer();
        }
        if(!checkIsOpened())  // button not opened yet
        {
            
                                        if(checkHasMine())  // game lost
                                        {
                                            board.stopTimer(); // stop timer
                                            board.setGameWon(false);   // set gameWon bool to false
                                            board.setGameStatus(true);  //game ended
                                            cellButton.setBackground(new Color(204,204,204));
                                            board.showMinesAtlosingGame();   // show all mines at losing game
                                            board.getFaceJLabel().setIcon(board.getIconsArray()[12]);//smiley
//                                            String msg="Sorry! you lost the game :(";
//                                            JOptionPane.showMessageDialog(null, msg, "STATUS BOX: ", JOptionPane.INFORMATION_MESSAGE);
//                                            
                                            removeMouselistener();
                                            board.incGamePlayedCount();
                                            
                                            board.getMSAcess().setDataInDB();
                                            showStatiscticsBoard();
                                            
                                        }
                                        else  // button doesnt have mine
                                        {
                                            int val = getValue();
                                            if(val > 0 ) // neighbor cells have mine/mines
                                            {
                                                setIsOpened(true); // set opened true
                                                cellButton.setBackground(new Color(204,204,204));
                                                setIconOnCell(x,y,val);
                                                
                                                 /// check for winner
                                                 if(board.checkWinner())
                                                 {
                                                     board.stopTimer();
                                                     board.setGameWon(true);
                                                     board.setGameStatus(true); //game ended
//                                                     String msg="YOU WIN :D";
//                                                     JOptionPane.showMessageDialog(null, msg, "STATUS BOX: ", JOptionPane.INFORMATION_MESSAGE);
                                                     removeMouselistener();
                                                     board.incGamePlayedCount();
                                                     board.incGameWonCount();
                                                     
                                                     board.getMSAcess().setDataInDB();
                                                     showStatiscticsBoard();
                                                     
                                                     
                                                  
                                                 }
                                            }
                                            else// neighbor cells dont have mine/mines
                                            {
                                                setIsOpened(true); // set opened true
                                                cellButton.setBackground(new Color(204,204,204)); // 204,204,255
                                                if(x == 0 && y == 0) // upper left corner
                                                {
                                                    if(!board.getGrid()[x+1][y].checkHasFlag() && !board.getGrid()[x+1][y].checkIsOpened())
                                                        board.getGrid()[x+1][y].LeftClickFunction(x+1, y);
                                                    if(!board.getGrid()[x+1][y+1].checkHasFlag() && !board.getGrid()[x+1][y+1].checkIsOpened())
                                                        board.getGrid()[x+1][y+1].LeftClickFunction(x+1, y+1);
                                                    if(!board.getGrid()[x][y+1].checkHasFlag() && !board.getGrid()[x][y+1].checkIsOpened())
                                                        board.getGrid()[x][y+1].LeftClickFunction(x, y+1);
                                                }
                                                else if(x == 0 && y == 8)
                                                {
                                                    if(!board.getGrid()[x+1][y].checkHasFlag() && !board.getGrid()[x+1][y].checkIsOpened())
                                                        board.getGrid()[x+1][y].LeftClickFunction(x+1, y);
                                                    if(!board.getGrid()[x+1][y-1].checkHasFlag() && !board.getGrid()[x+1][y-1].checkIsOpened())
                                                        board.getGrid()[x+1][y-1].LeftClickFunction(x+1, y-1);
                                                    if(!board.getGrid()[x][y-1].checkHasFlag() && !board.getGrid()[x][y-1].checkIsOpened())
                                                        board.getGrid()[x][y-1].LeftClickFunction(x, y-1);
                                                }
                                                else if(x == 8 && y == 0)
                                                {
                                                    if(!board.getGrid()[x-1][y].checkHasFlag() && ! board.getGrid()[x-1][y].checkIsOpened())
                                                        board.getGrid()[x-1][y].LeftClickFunction(x-1, y);
                                                    if(!board.getGrid()[x-1][y+1].checkHasFlag() && ! board.getGrid()[x-1][y+1].checkIsOpened())
                                                        board.getGrid()[x-1][y+1].LeftClickFunction(x-1, y+1);
                                                    if(!board.getGrid()[x][y+1].checkHasFlag() && !board.getGrid()[x][y+1].checkIsOpened())
                                                        board.getGrid()[x][y+1].LeftClickFunction(x, y+1);
                                                }
                                                else if(x == 8 && y== 8)
                                                {
                                                    if(!board.getGrid()[x][y-1].checkHasFlag() && !  board.getGrid()[x][y-1].checkIsOpened())
                                                        board.getGrid()[x][y-1].LeftClickFunction(x, y-1);
                                                    if(!board.getGrid()[x-1][y-1].checkHasFlag() && ! board.getGrid()[x-1][y-1].checkIsOpened())
                                                        board.getGrid()[x-1][y-1].LeftClickFunction(x-1, y-1);
                                                    if(!board.getGrid()[x-1][y].checkHasFlag() && !  board.getGrid()[x-1][y].checkIsOpened())
                                                        board.getGrid()[x-1][y].LeftClickFunction(x-1, y);
                                                }
                                                else if(x == 0)
                                                {
                                                    if(!board.getGrid()[x][y-1].checkHasFlag() && ! board.getGrid()[x][y-1].checkIsOpened())
                                                        board.getGrid()[x][y-1].LeftClickFunction(x, y-1);
                                                    if(!board.getGrid()[x][y+1].checkHasFlag() && !board.getGrid()[x][y+1].checkIsOpened())
                                                        board.getGrid()[x][y+1].LeftClickFunction(x, y+1);
                                                    if(!board.getGrid()[x+1][y].checkHasFlag() && ! board.getGrid()[x+1][y].checkIsOpened())
                                                        board.getGrid()[x+1][y].LeftClickFunction(x+1, y);
                                                    if(!board.getGrid()[x+1][y+1].checkHasFlag() && !board.getGrid()[x+1][y+1].checkIsOpened())
                                                        board.getGrid()[x+1][y+1].LeftClickFunction(x+1, y+1);
                                                    if(!board.getGrid()[x+1][y-1].checkHasFlag() && ! board.getGrid()[x+1][y-1].checkIsOpened())
                                                        board.getGrid()[x+1][y-1].LeftClickFunction(x+1, y-1);
                                                     
                                                     
                                                }
                                                else if(x ==8)
                                                {
                                                    if(!board.getGrid()[x][y-1].checkHasFlag() && !board.getGrid()[x][y-1].checkIsOpened())
                                                        board.getGrid()[x][y-1].LeftClickFunction(x, y-1);
                                                    if(!board.getGrid()[x][y+1].checkHasFlag() && !board.getGrid()[x][y+1].checkIsOpened())
                                                        board.getGrid()[x][y+1].LeftClickFunction(x, y+1);
                                                    if(!board.getGrid()[x-1][y].checkHasFlag() && !board.getGrid()[x-1][y].checkIsOpened())
                                                        board.getGrid()[x-1][y].LeftClickFunction(x-1, y);
                                                    if(!board.getGrid()[x-1][y+1].checkHasFlag() && !board.getGrid()[x-1][y+1].checkIsOpened())
                                                        board.getGrid()[x-1][y+1].LeftClickFunction(x-1, y+1);
                                                    if(!board.getGrid()[x-1][y-1].checkHasFlag() && !board.getGrid()[x-1][y-1].checkIsOpened())
                                                        board.getGrid()[x-1][y-1].LeftClickFunction(x-1, y-1);
                                                     
                                                }
                                                else if(y ==0 )
                                                {
                                                    if(!board.getGrid()[x-1][y].checkHasFlag() && !board.getGrid()[x-1][y].checkIsOpened())
                                                        board.getGrid()[x-1][y].LeftClickFunction(x-1, y);
                                                    if(!board.getGrid()[x+1][y].checkHasFlag() && !board.getGrid()[x+1][y].checkIsOpened())
                                                        board.getGrid()[x+1][y].LeftClickFunction(x+1, y);
                                                    if(!board.getGrid()[x][y+1].checkHasFlag() && ! board.getGrid()[x][y+1].checkIsOpened())
                                                        board.getGrid()[x][y+1].LeftClickFunction(x, y+1);
                                                    if(!board.getGrid()[x-1][y+1].checkHasFlag() && !board.getGrid()[x-1][y+1].checkIsOpened())
                                                        board.getGrid()[x-1][y+1].LeftClickFunction(x-1, y+1);
                                                    if(!board.getGrid()[x+1][y+1].checkHasFlag() && !board.getGrid()[x+1][y+1].checkIsOpened())
                                                        board.getGrid()[x+1][y+1].LeftClickFunction(x+1, y+1); 
                                                }
                                                else if(y == 8)
                                                {
                                                    if(!board.getGrid()[x-1][y].checkHasFlag() && ! board.getGrid()[x-1][y].checkIsOpened())
                                                        board.getGrid()[x-1][y].LeftClickFunction(x-1, y);
                                                    if(!board.getGrid()[x+1][y].checkHasFlag() && !board.getGrid()[x+1][y].checkIsOpened())
                                                        board.getGrid()[x+1][y].LeftClickFunction(x+1, y);
                                                    if(!board.getGrid()[x][y-1].checkHasFlag() && !board.getGrid()[x][y-1].checkIsOpened())
                                                        board.getGrid()[x][y-1].LeftClickFunction(x, y-1);
                                                    if(!board.getGrid()[x-1][y-1].checkHasFlag() && !board.getGrid()[x-1][y-1].checkIsOpened())
                                                        board.getGrid()[x-1][y-1].LeftClickFunction(x-1, y-1);
                                                    if(!board.getGrid()[x+1][y-1].checkHasFlag() && ! board.getGrid()[x+1][y-1].checkIsOpened())
                                                        board.getGrid()[x+1][y-1].LeftClickFunction(x+1, y-1); 
                                                }
                                                else
                                                {
                                                    if(!board.getGrid()[x-1][y-1].checkHasFlag() && !board.getGrid()[x-1][y-1].checkIsOpened())
                                                        board.getGrid()[x-1][y-1].LeftClickFunction(x-1, y-1);
                                                    if(!board.getGrid()[x-1][y].checkHasFlag() && !board.getGrid()[x-1][y].checkIsOpened())
                                                        board.getGrid()[x-1][y].LeftClickFunction(x-1, y);
                                                    if(!board.getGrid()[x-1][y+1].checkHasFlag() && !board.getGrid()[x-1][y+1].checkIsOpened())
                                                        board.getGrid()[x-1][y+1].LeftClickFunction(x-1, y+1);
                                                    if(!board.getGrid()[x][y-1].checkHasFlag() && !board.getGrid()[x][y-1].checkIsOpened())
                                                        board.getGrid()[x][y-1].LeftClickFunction(x, y-1);
                                                    if(!board.getGrid()[x][y+1].checkHasFlag() && !board.getGrid()[x][y+1].checkIsOpened())
                                                        board.getGrid()[x][y+1].LeftClickFunction(x, y+1); 
                                                    if(!board.getGrid()[x+1][y-1].checkHasFlag() && !board.getGrid()[x+1][y-1].checkIsOpened())
                                                        board.getGrid()[x+1][y-1].LeftClickFunction(x+1, y-1);
                                                    if(!board.getGrid()[x+1][y].checkHasFlag() && !board.getGrid()[x+1][y].checkIsOpened())
                                                        board.getGrid()[x+1][y].LeftClickFunction(x+1, y);
                                                    if(!board.getGrid()[x+1][y+1].checkHasFlag() && !board.getGrid()[x+1][y+1].checkIsOpened())
                                                        board.getGrid()[x+1][y+1].LeftClickFunction(x+1, y+1); 
                                                }
                                            }
                                        }
        }
    }
    public void RightClickFunction()
    {
        if(board.getGameStatus())  // if game has been won or lost then do not perform click functions
                {
                    return;
                }
        if(!board.getStartTime()) //  timer not started yet so start it and set starttime = true
        {
            board.startTimer();
        }
        if(checkHasFlag()) // flag aready set   // remove flaf
        {
            setHasFlag(false);  // set flag false // remove flag 
            board.getGrid()[x][y].setIcon(null); // removing flag icon 
            board.incMinetextField();
            return;
        }
        if(!checkIsOpened())  // button not opened yet  //set flag
        {
                setHasFlag(true);  // set flag true
                board.getGrid()[x][y].setIcon(board.getIconsArray()[0]); // setting flag icon 
                board.decMinetextField();                   
        }
        
    }
    public void setIcon(ImageIcon i)
    {
        this.cellButton.setIcon(i);
    }
    
    public void setIconOnCell(int x, int y, int value) // set icon on cell
    {
         board.getGrid()[x][y].setIcon(board.getIconsArray()[value]);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void showStatiscticsBoard()
    {
        JFrame stats = new JFrame("STATISTICS");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel ST = new JPanel();
        JPanel GP = new JPanel();
        JPanel GW = new JPanel();
        JPanel BT = new JPanel();
        JPanel Date = new JPanel();
        
        JLabel status = new JLabel();
        status.setText("STATUS: ");
        status.setFont(new Font("DigtalFont.TTF", Font.BOLD, 15));
        JLabel gamePlayedLabel = new JLabel();
        gamePlayedLabel.setText("GAME PLAYED  : ");
        gamePlayedLabel.setFont(new Font("DigtalFont.TTF", Font.BOLD, 15));
        JLabel gameWonLabel  =new JLabel();
        gameWonLabel.setText("GAME WON  : ");
        gameWonLabel.setFont(new Font("DigtalFont.TTF", Font.BOLD, 15));
        JLabel BestTimeLabel = new JLabel();
        BestTimeLabel.setText("BEST TIME : ");
        BestTimeLabel.setFont(new Font("DigtalFont.TTF", Font.BOLD, 15));
        JLabel DateLabel = new JLabel();
        DateLabel.setText("DATE : ");
        DateLabel.setFont(new Font("DigtalFont.TTF", Font.BOLD, 15));
        
        JTextField tf_status = new JTextField();
        tf_status.setEditable(false);
        JTextField tf_gamePlayed = new JTextField();
        tf_gamePlayed.setEditable(false);
        JTextField tf_gameWon = new JTextField();
        tf_gameWon.setEditable(false);
        JTextField tf_bestTime = new JTextField();
        tf_bestTime.setEditable(false);
        JTextField tf_date = new JTextField();
        tf_date .setEditable(false);
        
        if(board.getGameWon()) // game won then show win status
        {
//            System.out.println("win");
            tf_status.setText("CONGRATULATIONS! YOU WON THE GAME");
        }
        else
        {
//            System.out.println("lose");
            tf_status.setText("YOU LOSE ! BETTER LUCK NEXT TIME");
           
        }
        tf_gamePlayed.setText(Integer.toString(board.getGamePlayedCount()));
        tf_gameWon.setText(Integer.toString(board.getGameWonCount()));
        tf_bestTime.setText(Integer.toString(board.getBestTime()));
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        String date = df.format(d);
        tf_date.setText(date);
        
        ST.add(status, BorderLayout.WEST);
        ST.add(tf_status, BorderLayout.EAST);
        GP.add(gamePlayedLabel, BorderLayout.WEST);
        GP.add(tf_gamePlayed, BorderLayout.EAST);
        GW.add(gameWonLabel,BorderLayout.WEST);
        GW.add(tf_gameWon,BorderLayout.EAST);
        BT.add(BestTimeLabel,BorderLayout.WEST );
        BT.add(tf_bestTime,BorderLayout.EAST );
        Date.add(DateLabel,BorderLayout.WEST );
        Date.add(tf_date, BorderLayout.EAST);
       
        mainPanel.add(ST);
        mainPanel.add(GP);
        mainPanel.add(GW);
        mainPanel.add(BT);
        mainPanel.add(Date);
        
        
        stats.getContentPane().add(mainPanel);
        stats.pack();
        stats.setSize(400,300);
        stats.setResizable(false);
        stats.setLocationRelativeTo(null); 
        stats.setVisible(true);
        
        
                
    }
}
