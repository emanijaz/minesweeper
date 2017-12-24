/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postlab_minesweeper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Random;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
//import javax.swing.Icon;
import javax.swing.ImageIcon;
//import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JFrame;

/**
 *
 * @author emani
 */
public class Board extends JFrame implements ActionListener, ContainerListener {
    
    private int rows;
    private int cols;
    private Cell Grid[][];
 //   private JFrame mainFrame;
    private ImageIcon icons[];
    private Boolean GameEnd= false;
    
    private JPanel panelb = new JPanel();
    private JPanel panelmt = new JPanel();
    private JLabel face = new JLabel();
    private JTextField tf_mine, tf_time;
    private Timer t;
    private Boolean startTime=false;
    private MsAccess ma;
    private int GamePlayedCount=0;
    private int GameWonCount=0;
    private int BestTime=0;
    private Boolean GameWon ;
    
    public Board(int rows, int cols)
    {
        
        setLocation(450, 200);
        this.rows = rows;
        this.cols = cols;
        this.Grid = new Cell[rows][cols];
        this.icons = new ImageIcon[15];
        setmenu();
        DrawBoard(rows,cols, Grid);
        setIcons();
        plantMinesRandomly();
        setNumbersInCells();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        t = new Timer();
        t.setBoardinTimer(this);
        
        
    }
     public void DrawBoard(int rows, int cols, Cell Grid[][])
    {
        setSize(280,380);
        setResizable(false);
 
        getContentPane().removeAll();
        panelb.removeAll();
        tf_mine = new JTextField("" + 10, 3);
        tf_mine.setEditable(false);
        tf_mine.setFont(new Font("DigtalFont.TTF", Font.BOLD, 25));
        tf_mine.setBackground(new Color(102,102,255));
        tf_mine.setForeground(Color.WHITE);
        tf_mine.setBorder(BorderFactory.createLoweredBevelBorder());
        tf_time = new JTextField("000", 3);
        tf_time.setEditable(false);
        tf_time.setFont(new Font("DigtalFont.TTF", Font.BOLD, 25));
        tf_time.setBackground(new Color(102,102,255));
        tf_time.setForeground(Color.WHITE);
        tf_time.setBorder(BorderFactory.createLoweredBevelBorder());
        
        
        panelmt.removeAll();
        panelmt.setLayout(new BoxLayout(panelmt, BoxLayout.X_AXIS));
        
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JLabel clock = new JLabel();
        JLabel  mine = new JLabel();
        
        clock.setIcon(new ImageIcon("clock2.png"));
        mine.setIcon(new ImageIcon("mineSideIcon.png"));
        face.setIcon(new ImageIcon("smiley.gif"));
        face.setSize(100,30);
        panel1.add(tf_mine,BorderLayout.EAST);
        panel1.add(mine,BorderLayout.WEST);
        panel2.add(clock,BorderLayout.WEST);
        panel2.add(tf_time,BorderLayout.EAST);
        panelmt.add(panel2);
        panelmt.add(face);
         panelmt.add(panel1);
        
        panelmt.setBorder(BorderFactory.createLoweredBevelBorder());
 
        panelb.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30), BorderFactory.createLoweredSoftBevelBorder()));

       panelb.setPreferredSize(new Dimension(280,300));
        panelb.setLayout(new GridLayout(9, 9));
 
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = new Cell(i,j,false, false, false, -1, this);// x,y, hasFlag, isOpened, hasMine, value, Board are parameters
                cell.createListener();
                Grid[i][j] =cell; 
               panelb.add(Grid[i][j].getButton());
            }
        }
        
        panelb.revalidate();
        panelb.repaint();
        getContentPane().setLayout(new BorderLayout());
        
        getContentPane().repaint();
        getContentPane().add(panelb, BorderLayout.CENTER);
        getContentPane().add(panelmt, BorderLayout.SOUTH);
        setVisible(true);
        
        face.addMouseListener(new MouseListener(){
            
            @Override
            public void mouseClicked(MouseEvent e) {
             
            }

            @Override
            public void mousePressed(MouseEvent e) {
                   reset();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
              //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
              //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            
        });
        
    }
    public void setmenu()
    {
        JMenuBar bar = new JMenuBar();
 
        JMenu game = new JMenu("GAME");
 
        JMenuItem menuitem = new JMenuItem("new game");
        final JMenuItem exit = new JMenuItem("Exit");
        final JMenu help = new JMenu("HELP");
        final JMenuItem helpitem = new JMenuItem("Help");
        menuitem.addActionListener(
                new ActionListener() {
 
                    public void actionPerformed(ActionEvent e) {
 
                        reset();
                    }
                });
        exit.addActionListener(new ActionListener() {
 
            
            public void actionPerformed(ActionEvent e) {
              //  t.stop();
                System.exit(0);
            }
        });
        helpitem.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "instruction");
 
            }
        });
        setJMenuBar(bar);
        game.add(menuitem);
        game.addSeparator();
        game.add(exit);
        help.add(helpitem);
        bar.add(game);
        bar.add(help);
        
    }
    public void setNumbersInCells()
    {
        int mineCount = 0;
        for(int i=0; i< 9; i++)
        {
            for(int j=0; j<9; j++)
            {
                if(!Grid[i][j].checkHasMine())  // cell doesnt contain mine
                {
                    if (i == 0 && j==0) // upper left corner
                    {
                        if(Grid[i+1][j].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i+1][j+1].checkHasMine())
                            mineCount++;
                        if(Grid[i][j+1].checkHasMine())
                            mineCount++;
                        
                        Grid[i][j].setValue(mineCount);
                    }
                    else if( i==0 && j == 8) // upper right corner
                    {
                        if(Grid[i][j-1].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i+1][j-1].checkHasMine())
                            mineCount++;
                        if(Grid[i+1][j].checkHasMine())
                            mineCount++;
                        
                        Grid[i][j].setValue(mineCount);
                        
                    }
                    else if(i==8 && j == 0) // lower left corner
                    {
                        
                        if(Grid[i-1][j].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i-1][j+1].checkHasMine())
                            mineCount++;
                        if(Grid[i][j+1].checkHasMine())
                            mineCount++;
                        
                        Grid[i][j].setValue(mineCount);
                    }
                    else if( i == 8 && j == 8) // lower right corner
                    {
                        if(Grid[i][j-1].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i-1][j-1].checkHasMine())
                            mineCount++;
                        if(Grid[i-1][j].checkHasMine())
                            mineCount++;
                        
                        Grid[i][j].setValue(mineCount);
                    }
                    else if( i == 0) // upper egde
                    {
                        if(Grid[i][j-1].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i][j+1].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i+1][j].checkHasMine())
                            mineCount++;
                        if(Grid[i+1][j+1].checkHasMine())
                            mineCount++;
                        if(Grid[i+1][j-1].checkHasMine())
                            mineCount++;
                        
                        Grid[i][j].setValue(mineCount);
                    }
                    else if( i == 8) // lower edge
                    {
                        if(Grid[i][j-1].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i][j+1].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i-1][j].checkHasMine())
                            mineCount++;
                        if(Grid[i-1][j+1].checkHasMine())
                            mineCount++;
                        if(Grid[i-1][j-1].checkHasMine())
                            mineCount++;
                        
                        Grid[i][j].setValue(mineCount);
                    }
                    else if ( j == 0) // left edge
                    {
                        if(Grid[i-1][j].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i+1][j].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i][j+1].checkHasMine())
                            mineCount++;
                        if(Grid[i-1][j+1].checkHasMine())
                            mineCount++;
                        if(Grid[i+1][j+1].checkHasMine())
                            mineCount++;
                        
                        Grid[i][j].setValue(mineCount);
                        
                    }
                    else if( j == 8) // right edge
                    {
                        if(Grid[i-1][j].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i+1][j].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i][j-1].checkHasMine())
                            mineCount++;
                        if(Grid[i-1][j-1].checkHasMine())
                            mineCount++;
                        if(Grid[i+1][j-1].checkHasMine())
                            mineCount++;
                        
                        Grid[i][j].setValue(mineCount);
                        
                    }
                    else
                    {
                        if(Grid[i-1][j-1].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i-1][j].checkHasMine())   // checking neighbors either they have mine or not then set value of cell equals to mineCount
                            mineCount++;
                        if(Grid[i-1][j+1].checkHasMine())
                            mineCount++;
                        if(Grid[i][j-1].checkHasMine())
                            mineCount++;
                        if(Grid[i][j+1].checkHasMine())
                            mineCount++;
                        if(Grid[i+1][j-1].checkHasMine())
                            mineCount++;
                        if(Grid[i+1][j].checkHasMine())
                            mineCount++;
                        if(Grid[i+1][j+1].checkHasMine())
                            mineCount++;
                        Grid[i][j].setValue(mineCount);
                    }

                }
                mineCount = 0;
            }
        }
    }
    public void setIcons()
    {
        this.icons[0] = new ImageIcon("flag1.png");
        this.icons[1] = new ImageIcon("1_.gif");
        this.icons[2] = new ImageIcon("2_.gif");
        this.icons[3] = new ImageIcon("3_.gif");
        this.icons[4] = new ImageIcon("4_.gif");
        this.icons[5] = new ImageIcon("5_.gif");
        this.icons[6] = new ImageIcon("6_.gif");
        this.icons[7] = new ImageIcon("7_.gif");
        this.icons[8] = new ImageIcon("8_.gif");
        this.icons[9] = new ImageIcon("mine_blasted.png");
        this.icons[10] = new ImageIcon("mine_blasted.gif");
        this.icons[11]= new ImageIcon("flagRemoved.gif");
        this.icons[12]= new ImageIcon("sad.gif");
        this.icons[13]= new ImageIcon("smiley.gif");
        this.icons[14]= new ImageIcon("wow.gif");
        
    }
    
     public void plantMinesRandomly()
    {
        int plantedMines =0;
        while(plantedMines != 10) // plant 10 mines ranomly
        {
               Random rand = new Random();
               int r = rand.nextInt(8) + 0;
               int c = rand.nextInt(8) + 0;
               if(!Grid[r][c].checkHasMine())
               {
                      Grid[r][c].setHasMine(true);
                //      System.out.println("mine set at row " + r + " col " + c);
                      plantedMines++;
               }
               
        }
    }
    public Cell[][] getGrid()
    {
        return Grid;
    }
    public ImageIcon[] getIconsArray()
    {
        return icons;
    }
    
    public Boolean checkWinner()
    {
        
        for(int i=0; i< 9 ;i++)
        {
            for(int j=0; j<9; j++)
            {
                if(!Grid[i][j].checkHasMine() && !Grid[i][j].checkIsOpened()) // if any place that doent have mine and still not opened so not win
                {
                    return false;
                }
            }
        }
        return true ;// win
    }
    
    public Boolean getGameStatus()
    {
        return GameEnd;
    }
    public void setGameStatus(Boolean w)
    {
        GameEnd = w;
    }
    
    public void showMinesAtlosingGame()
    {
        for(int i=0; i<9 ;i++)
        {
            for(int j=0; j<9; j++)
            {
                if(Grid[i][j].checkHasMine())
                {
                     Grid[i][j].setIcon(icons[9]);
                }
            }
        }
    }
    
    public void reset()
    {
        startTime=false;
        this.dispose();
        Board b = new Board(9,9);
        MsAccess ms = new MsAccess(b);
        b.setMSAcess(ms);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentAdded(ContainerEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentRemoved(ContainerEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public JTextField get_tf_time()
    {
        return tf_time;
    }
    public JTextField get_tf_mine()
    {
        return tf_mine;
    }
    public Boolean getStartTime()
    {
        return startTime;
    }
    public Timer getTimer()
    {
        return t;
    }
    
    public void setStartTime(Boolean s)
    {
        startTime=s;
    }
    public JLabel getFaceJLabel()
    {
        return face;
    }
    public void setMSAcess(MsAccess  ma)
    {
        this.ma = ma;
    }
    public MsAccess getMSAcess()
    {
        return this.ma;
    }
    public void loadStatsAtStart(int gp, int gw)
    {
        this.GamePlayedCount = gp;
        this.GameWonCount  = gw;
    }
    public int[][] setStatsAtEnd()
    {
        int data[][]=  new int[3][1];
        data[0][0] = this.GamePlayedCount;
        data[1][0] = this.GameWonCount;
        data[2][0] = this.BestTime;
        return data;
    }
    public Boolean getGameWon()
    {
        return this.GameWon;
    }
    public void setGameWon(Boolean gw)
    {
        this.GameWon = gw;
    }
    public void incGamePlayedCount()
    {
        this.GamePlayedCount++;
    }
    public void incGameWonCount()
    {
        this.GameWonCount++;
    }
    public int getGamePlayedCount()
    {
        return this.GamePlayedCount;
    }
    public int getGameWonCount()
    {
        return this.GameWonCount;
    }
    
    public void setBestTime(int bt)
    {
        this.BestTime = bt;
    }
    public int getBestTime()
    {
        return this.BestTime;
    }
    
    public void decMinetextField()
    {
        int mines = Integer.parseInt(tf_mine.getText());
        if(mines>0)
        {
            mines--;
        }
        tf_mine.setText(Integer.toString(mines));
    }
    public void incMinetextField()
    {
        int mines = Integer.parseInt(tf_mine.getText());
        mines++;
        tf_mine.setText(Integer.toString(mines));
    }
    public void startTimer() // start time and set bool start= true
    {
        t.Start();
        startTime=true;
    }
    public void stopTimer() // stop time and set bool start= false
    {
        t.stop();
        startTime=false;
    }
}
